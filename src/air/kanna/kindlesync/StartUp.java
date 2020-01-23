package air.kanna.kindlesync;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import air.kanna.kindlesync.compare.FileListComparer;
import air.kanna.kindlesync.compare.ListComparer;
import air.kanna.kindlesync.compare.Operation;
import air.kanna.kindlesync.compare.OperationItem;
import air.kanna.kindlesync.config.SyncConfig;
import air.kanna.kindlesync.config.SyncConfigService;
import air.kanna.kindlesync.config.impl.SyncConfigServicePropertiesImpl;
import air.kanna.kindlesync.execute.JaveExecute;
import air.kanna.kindlesync.execute.OperationExecute;
import air.kanna.kindlesync.scan.PathScanner;
import air.kanna.kindlesync.scan.filter.FileNameExcludeFilter;
import air.kanna.kindlesync.scan.filter.FileNameIncludeFilter;
import air.kanna.kindlesync.scan.filter.FileNameRegexExcludeFilter;
import air.kanna.kindlesync.scan.filter.FileNameRegexIncludeFilter;
import air.kanna.kindlesync.scan.filter.FileTypeFilter;
import air.kanna.kindlesync.scan.filter.NullDirectoryFilter;
import air.kanna.kindlesync.scan.filter.ScanFilter;
import air.kanna.kindlesync.util.Nullable;

public class StartUp {
    private static final Logger logger = Logger.getLogger(StartUp.class);
    
    private static final String TITLE = "文件夹同步工具";
    private static final String CONFIG_FILE = "config.cfg";
    
    private static final int MODE_START = 1;
    private static final int MODE_SCANED = 2;
    private static final int MODE_PROCESS = 3;
    
    private JFrame frame;
    private JList<OperationItem<File>> operResultList;
    private JButton baseSelectPathBtn;
    private JButton destSelectPathBtn;
    private JButton destToBaseBtn;
    private JButton baseToDestBtn;
    private JCheckBox scanFileCb;
    private JCheckBox scanDirCb;
    private JTextField scanIncludeTf;
    private JTextField scanExcludeTf;
    private JCheckBox excludeNullDirCb;
    private JCheckBox scanExcludeRegexCb;
    private JCheckBox scanIncludeRegexCb;
    private JCheckBox undeleteCb;
    private JButton deleteOperBtn;
    private JButton resetOperBtn;
    private JButton executeBtn;
    
    private JFileChooser chooser;
    private DefaultListModel<OperationItem<File>> listModel;
    private SyncConfig config;
    private SyncConfigService configService;
    private PathScanner scanner;
    private ListComparer<File> comparer;
    private OperationExecute executer;
    
    private File pathA;
    private File pathB;
    private List<File> fileListA;
    private List<File> fileListB;
    private List<OperationItem<File>> result;
    private JProgressBar executeProcess;
    private JLabel processTextTb;
    private JComboBox<String> basePathCb;
    private JComboBox<String> destPathCb;
    
    /**
     * Create the application.
     */
    public StartUp() {
        initialize();
        initData();
        initControl();
    }
    
    private void setStringsToComboBox(String strings, JComboBox<String> box) {
        if(box == null || Nullable.isNull(strings)) {
            return;
        }
        String[] paths = strings.split(";");
        if(paths == null) {
            paths = new String[] {strings};
        }
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)box.getModel();
        model.removeAllElements();
        for(int i=0; i<paths.length; i++) {
            if(Nullable.isNull(paths[i])) {
                continue;
            }
            model.addElement(paths[i]);
        }
        box.setSelectedIndex(0);
    }
    
    private String getStringsFromComboBox(JComboBox<String> box) {
        if(box == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(box.getSelectedIndex() >= 0) {
            sb.append(box.getSelectedItem()).append(';');
        }
        for(int i=0; i<box.getItemCount(); i++) {
            if(i == box.getSelectedIndex()) {
                continue;
            }
            sb.append(box.getItemAt(i)).append(';');
        }
        return sb.toString();
    }
    
    private void setFromConfig() {
        
        setStringsToComboBox(config.getBasePath(), basePathCb);
        setStringsToComboBox(config.getDescPath(), destPathCb);

        if(!Nullable.isNull(config.getIncludeStr())) {
            scanIncludeTf.setText(config.getIncludeStr());
        }
        if(!Nullable.isNull(config.getExcludeStr())) {
            scanExcludeTf.setText(config.getExcludeStr());
        }

        scanFileCb.setSelected(config.isScanFile());
        scanDirCb.setSelected(config.isScanDir());
        
        scanIncludeRegexCb.setSelected(config.isIncludeRegex());
        scanExcludeRegexCb.setSelected(config.isExcludeRegex());
        
        excludeNullDirCb.setSelected(config.isExcludeNullDir());
        undeleteCb.setSelected(config.isUnExecuteDelete());
    }
    
    private void saveToConfig() {
        config.setBasePath(getStringsFromComboBox(basePathCb));
        config.setDescPath(getStringsFromComboBox(destPathCb));
        
        config.setIncludeStr(scanIncludeTf.getText());
        config.setExcludeStr(scanExcludeTf.getText());
        
        config.setScanFile(scanFileCb.isSelected());
        config.setScanDir(scanDirCb.isSelected());
        
        config.setIncludeRegex(scanIncludeRegexCb.isSelected());
        config.setExcludeRegex(scanExcludeRegexCb.isSelected());
        
        config.setExcludeNullDir(excludeNullDirCb.isSelected());
        config.setUnExecuteDelete(undeleteCb.isSelected());
    }
    
    private void setMode(int mode) {
        if(mode == MODE_SCANED) {
            deleteOperBtn.setEnabled(true);
            resetOperBtn.setEnabled(true);
            executeBtn.setEnabled(true);
        }else {
            deleteOperBtn.setEnabled(false);
            resetOperBtn.setEnabled(false);
            executeBtn.setEnabled(false);
        }
    }
    
    private void reset() {
        setMode(MODE_START);
        listModel.clear();
    }
    
    private void initData() {
        File configFile = new File(CONFIG_FILE);
        logger.info("config file: " + configFile.getAbsolutePath());
        configService = new SyncConfigServicePropertiesImpl(configFile);
        
        config = configService.getConfig();
        if(config == null) {
            config = new SyncConfig();
        }
        
        setFromConfig();
    }
    
    private void selectAndAddPath(JComboBox<String> box) {
        File old = null;
        if(box.getSelectedIndex() >= 0) {
            if(!Nullable.isNull((String)box.getSelectedItem())) {
                old = new File((String)box.getSelectedItem());
            }
        }
        if(old == null) {
            old = new File(".");
        }
        
        chooser.setSelectedFile(old);
        if(JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(frame)){
            File selected = chooser.getSelectedFile();
            String path = selected.getAbsolutePath();
            int findIdx = -1;
            for(int i=0; i<box.getItemCount(); i++) {
                if(path.equals(box.getItemAt(i))) {
                    findIdx = i;
                    break;
                }
            }
            if(findIdx >= 0) {
                box.setSelectedIndex(findIdx);
            }else {
                box.addItem(path);
                box.setSelectedItem(path);
            }
            reset();
        }
    }
    
    private void initControl() {
        setMode(MODE_START);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveToConfig();
                if(!configService.saveConfig(config)) {
                    logger.error("Save Config to file error");
                }
                System.exit(0);
            }
        });
        
        baseSelectPathBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                selectAndAddPath(basePathCb);
            }
        });
        
        destSelectPathBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                selectAndAddPath(destPathCb);
            }
        });
        
        DocumentListener docListener = new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent e){
                reset();
            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                reset();
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
                reset();
            }
        };
        
        baseToDestBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(Nullable.isNull((String)basePathCb.getSelectedItem())) {
                    JOptionPane.showMessageDialog(frame, "请选择源目录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(Nullable.isNull((String)destPathCb.getSelectedItem())) {
                    JOptionPane.showMessageDialog(frame, "请选择目标目录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                pathA = new File((String)basePathCb.getSelectedItem());
                pathB = new File((String)destPathCb.getSelectedItem());
                
                if(!pathA.exists() || !pathA.isDirectory()) {
                    JOptionPane.showMessageDialog(frame, "源目录不存在或者不是一个目录，请重新选择", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(!pathB.exists() || !pathB.isDirectory()) {
                    JOptionPane.showMessageDialog(frame, "目标目录不存在或者不是一个目录，请重新选择", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                frame.setTitle(TITLE + " 源 --->> 目标");
                startScan();
            }
        });
        
        destToBaseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(Nullable.isNull((String)basePathCb.getSelectedItem())) {
                    JOptionPane.showMessageDialog(frame, "请选择源目录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(Nullable.isNull((String)destPathCb.getSelectedItem())) {
                    JOptionPane.showMessageDialog(frame, "请选择目标目录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                pathA = new File((String)destPathCb.getSelectedItem());
                pathB = new File((String)basePathCb.getSelectedItem());
                
                if(!pathA.exists() || !pathA.isDirectory()) {
                    JOptionPane.showMessageDialog(frame, "目标目录不存在或者不是一个目录，请重新选择", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(!pathB.exists() || !pathB.isDirectory()) {
                    JOptionPane.showMessageDialog(frame, "源目录不存在或者不是一个目录，请重新选择", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                frame.setTitle(TITLE + " 目标 --->> 源");
                startScan();
            }
        });
        
        deleteOperBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                List<OperationItem<File>> list = operResultList.getSelectedValuesList();
                if(list == null || list.size() <= 0) {
                    return;
                }
                for(OperationItem<File> item : list) {
                    listModel.removeElement(item);
                }
            }
        });
        
        resetOperBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                listModel.clear();
                for(OperationItem item : result) {
                    listModel.addElement(item);
                }
            }
        });
        
        executeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(listModel.size() <= 0) {
                    JOptionPane.showMessageDialog(frame, "列表中没有可用的操作", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                executeBtn.setEnabled(false);
                final List<OperationItem> executeList = new ArrayList<>();
                
                for(int i=0; i<listModel.size(); i++) {
                    executeList.add(listModel.get(i));
                }
                
                (new Thread() {
                    @Override
                    public void run() {
                        try {
                            List<String> execResult = executer.execute(pathA, pathB, executeList);
                            StringBuilder sb = new StringBuilder();
                            String clipText = null;
                            boolean isAllSuccess = false;
                            int count = 0;
                            
                            for(int i=0; i<execResult.size(); i++) {
                                String res = execResult.get(i);
                                if(res == null || res.length() <= 0) {
                                    count++;
                                    continue;
                                }
                                sb.append("第").append(i + 1).append("个文件失败：").append(res).append('\n');
                            }
                            
                            if(sb.length() <= 0) {
                                sb.append("所有文件同步成功");
                                isAllSuccess = true;
                            }else {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("同步成功").append(count)
                                    .append("个文件，失败").append(execResult.size() - count)
                                    .append("个文件，失败原因：\n");
                                sb.insert(0, sb2.toString());
                            }
                            
                            clipText = sb.toString();
                            JOptionPane.showMessageDialog(frame, clipText, "同步结果", JOptionPane.INFORMATION_MESSAGE);
                            
                            if(!isAllSuccess) {
                                setSysClipboardText(clipText);
                                JOptionPane.showMessageDialog(frame, "已将失败记录复制到剪切板", "提示", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }catch(Exception e) {
                            JOptionPane.showMessageDialog(frame, "同步出现错误：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                            logger.error("同步出现错误", e);
                        }finally {
                            executeBtn.setEnabled(true);
                        }
                    }
                }).start();
            }
        });
        
        ((JaveExecute)executer).setListener(new ProcessListener() {
            private int current = 0;
            private int max = 100;
            
            @Override
            public void setMax(int max) {
                if(max <= 0){
                    throw new IllegalArgumentException("ProcessBar's max must > 0");
                }
                this.max = max;
                this.current = 0;
                
                executeProcess.setMaximum(max);
                executeProcess.setMinimum(0);
                executeProcess.setValue(0);
                processTextTb.setText("--");
            }

            @Override
            public void next(String message) {
                current++;
                if(current > max){
                    executeProcess.setValue(max);
                }else{
                    executeProcess.setValue(current);
                }
                showFile();
            }

            @Override
            public void setPosition(int current, String message) {
                if(current <= 0){
                    executeProcess.setValue(0);
                    this.current = 0;
                }else
                if(current > max){
                    executeProcess.setValue(max);
                    this.current = max;
                }else{
                    executeProcess.setValue(current);
                    this.current = current;
                }
                showFile();
            }

            @Override
            public void finish(String message) {
                executeProcess.setValue(max);
                processTextTb.setText("--");
            }
            
            private void showFile() {
                if(current >= 0 && current < listModel.size()) {
                    StringBuilder sb = new StringBuilder();
                    
                    sb.append(current).append('/').append(max).append("，正在处理：");
                    sb.append(listModel.get(current).getItem().getName());
                    processTextTb.setText(sb.toString());
                }
            }
        });
    }
    
    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }
    
    private void startScan() {
        resetScanFilter();
        
        fileListA = scanner.scan(pathA);
        fileListB = scanner.scan(pathB);
        ((FileListComparer)comparer).setBasePath(pathA);
        ((FileListComparer)comparer).setDestPath(pathB);
        result = comparer.getCompareResult(fileListA, fileListB);
        
        boolean isUnDelete = undeleteCb.isSelected();
        
        listModel.clear();
        for(OperationItem item : result) {
            if(isUnDelete) {
                if(item.getOperation() != null && item.getOperation() == Operation.DEL) {
                    continue;
                }
            }
            listModel.addElement(item);
        }

        setMode(MODE_SCANED);
    }
    
    private void resetScanFilter() {
        scanner.getFilters().clear();
        saveToConfig();

        if(!config.isScanDir() || !config.isScanFile()) {
            FileTypeFilter filter = new FileTypeFilter();
            if(config.isScanDir()) {
                filter.addType(FileTypeFilter.TYPE_DIRECTORY);
            }
            if(config.isScanFile()) {
                filter.addType(FileTypeFilter.TYPE_FILE);
            }
            scanner.getFilters().add(filter);
        }
        
        if(!Nullable.isNull(config.getIncludeStr())) {
            FileNameIncludeFilter filter;
            if(config.isIncludeRegex()) {
                filter = new FileNameRegexIncludeFilter(config.getIncludeStr());
            }else {
                filter = new FileNameIncludeFilter(config.getIncludeStr());
            }
            scanner.getFilters().add(filter);
        }
        if(!Nullable.isNull(config.getExcludeStr())) {
            ScanFilter filter;
            if(config.isIncludeRegex()) {
                filter = new FileNameRegexExcludeFilter(config.getExcludeStr());
            }else {
                filter = new FileNameExcludeFilter(config.getExcludeStr());
            }
            scanner.getFilters().add(filter);
        }
        
        if(config.isExcludeNullDir()) {
            scanner.getFilters().add(new NullDirectoryFilter());
        }
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle(TITLE);
        frame.setBounds(100, 100, 800, 450);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        
        JPanel settingPanel = new JPanel();
        frame.getContentPane().add(settingPanel, BorderLayout.WEST);
        settingPanel.setLayout(new GridLayout(7, 1, 0, 0));
        
        JPanel filePanel = new JPanel();
        frame.getContentPane().add(filePanel, BorderLayout.NORTH);
        filePanel.setLayout(new BorderLayout(0, 0));
        
        JPanel panel01 = new JPanel();
        JLabel label01 = new JLabel("源目录：");
        filePanel.add(panel01, BorderLayout.WEST);
        panel01.add(label01);
        
        basePathCb = new JComboBox<String>();
        basePathCb.setModel(new DefaultComboBoxModel<String>(new String[] {"5555555555555555555"}));
        basePathCb.setEditable(true);
        panel01.add(basePathCb);
        
        baseSelectPathBtn = new JButton("...");
        panel01.add(baseSelectPathBtn);
        
        JPanel panel02 = new JPanel();
        JLabel label02 = new JLabel("目标目录：");
        filePanel.add(panel02, BorderLayout.EAST);
        panel02.add(label02);
        
        destPathCb = new JComboBox<String>();
        destPathCb.setModel(new DefaultComboBoxModel<String>(new String[] {"5555555555555555555"}));
        destPathCb.setEditable(true);
        panel02.add(destPathCb);
        
        destSelectPathBtn = new JButton("...");
        panel02.add(destSelectPathBtn);
        
        JPanel panel03 = new JPanel();
        filePanel.add(panel03, BorderLayout.CENTER);
        panel03.setLayout(new GridLayout(1, 2, 0, 0));
        
        destToBaseBtn = new JButton("<<<");
        panel03.add(destToBaseBtn);
        
        baseToDestBtn = new JButton(">>>");
        panel03.add(baseToDestBtn);
        
        
        JPanel panel04 = new JPanel();
        settingPanel.add(panel04);
        
        JLabel label = new JLabel("扫描参数");
        panel04.add(label);
        JPanel panel05 = new JPanel();
        settingPanel.add(panel05);
        
        scanFileCb = new JCheckBox("文件");
        scanFileCb.setSelected(true);
        panel05.add(scanFileCb);
        
        scanDirCb = new JCheckBox("目录");
        scanDirCb.setSelected(true);
        panel05.add(scanDirCb);
        JPanel panel06 = new JPanel();
        settingPanel.add(panel06);
        
        JLabel label_1 = new JLabel("包含：");
        panel06.add(label_1);
        
        scanIncludeTf = new JTextField();
        scanIncludeTf.setToolTipText("多个关键字使用英文分号隔开");
        panel06.add(scanIncludeTf);
        scanIncludeTf.setColumns(10);
        
        scanIncludeRegexCb = new JCheckBox("正则");
        panel06.add(scanIncludeRegexCb);
        JPanel panel07 = new JPanel();
        settingPanel.add(panel07);
        
        JLabel label_2 = new JLabel("剔除：");
        panel07.add(label_2);
        
        scanExcludeTf = new JTextField();
        scanExcludeTf.setToolTipText("多个关键字使用英文分号隔开");
        panel07.add(scanExcludeTf);
        scanExcludeTf.setColumns(10);
        
        scanExcludeRegexCb = new JCheckBox("正则");
        panel07.add(scanExcludeRegexCb);
        JPanel panel08 = new JPanel();
        settingPanel.add(panel08);
        
        excludeNullDirCb = new JCheckBox("剔除空目录");
        excludeNullDirCb.setSelected(true);
        panel08.add(excludeNullDirCb);
        
        operResultList = new JList<>();
        JPanel listPanel = new JPanel();
        JScrollPane jScrollPane = new JScrollPane(operResultList);
        listPanel.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(listPanel, BorderLayout.CENTER);
        listPanel.add(jScrollPane, BorderLayout.CENTER);
        
        JPanel operPanel = new JPanel();
        frame.getContentPane().add(operPanel, BorderLayout.EAST);
        operPanel.setLayout(new GridLayout(10, 1, 0, 0));
        
        JPanel panel09 = new JPanel();
        operPanel.add(panel09);
        
        JLabel label_3 = new JLabel("操作");
        panel09.add(label_3);
        JPanel panel10 = new JPanel();
        operPanel.add(panel10);
        JPanel panel11 = new JPanel();
        operPanel.add(panel11);
        
        deleteOperBtn = new JButton("删除");
        panel11.add(deleteOperBtn);
        
        resetOperBtn = new JButton("重置");
        panel11.add(resetOperBtn);
        JPanel panel12 = new JPanel();
        operPanel.add(panel12);
        JPanel panel13 = new JPanel();
        operPanel.add(panel13);
        JPanel panel14 = new JPanel();
        operPanel.add(panel14);
        
        undeleteCb = new JCheckBox("不执行删除操作");
        undeleteCb.setSelected(true);
        panel14.add(undeleteCb);
        JPanel panel15 = new JPanel();
        operPanel.add(panel15);
        
        executeBtn = new JButton("执行同步");
        panel15.add(executeBtn);
        
        chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setDialogTitle("请选择目录");
        chooser.setApproveButtonText("选择该目录");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        
        listModel = new DefaultListModel<>();
        comparer = new FileListComparer();
        scanner = new PathScanner();
        executer = new JaveExecute();
        
        operResultList.setModel(listModel);
        
        executeProcess = new JProgressBar();
        listPanel.add(executeProcess, BorderLayout.SOUTH);
        
        processTextTb = new JLabel("--");
        processTextTb.setHorizontalAlignment(SwingConstants.CENTER);
        listPanel.add(processTextTb, BorderLayout.NORTH);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StartUp window = new StartUp();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
