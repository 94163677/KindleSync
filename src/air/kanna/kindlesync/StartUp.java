package air.kanna.kindlesync;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;

import air.kanna.kindlesync.compare.FileListComparer;
import air.kanna.kindlesync.compare.FileOperationItem;
import air.kanna.kindlesync.config.SyncConfig;
import air.kanna.kindlesync.config.SyncConfigService;
import air.kanna.kindlesync.config.impl.SyncConfigServicePropertiesImpl;
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
import javax.swing.JProgressBar;

public class StartUp {
    private static final Logger logger = Logger.getLogger(StartUp.class);
    
    private static final String TITLE = "文件夹同步工具";
    private static final String CONFIG_FILE = "config.cfg";
    
    private static final int MODE_START = 1;
    private static final int MODE_SCANED = 2;
    private static final int MODE_PROCESS = 3;
    
    private JFrame frame;
    private JList<FileOperationItem> operResultList;
    private JTextField basePathTf;
    private JTextField destPathTf;
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
    private DefaultListModel<FileOperationItem> listModel;
    private SyncConfig config;
    private SyncConfigService configService;
    private PathScanner scanner;
    private FileListComparer comparer;
    private OperationExecute executer;
    
    private File pathA;
    private File pathB;
    private List<File> fileListA;
    private List<File> fileListB;
    private List<FileOperationItem> result;
    private JProgressBar executeProcess;
    
    /**
     * Create the application.
     */
    public StartUp() {
        initialize();
        initData();
        initControl();
    }
    
    private void setFromConfig() {
        if(!Nullable.isNull(config.getBasePath())) {
            basePathTf.setText(config.getBasePath());
        }
        if(!Nullable.isNull(config.getDescPath())) {
            destPathTf.setText(config.getDescPath());
        }
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
        config.setBasePath(basePathTf.getText());
        config.setDescPath(destPathTf.getText());
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
        
        config = configService.getSyncConfig();
        if(config == null) {
            config = new SyncConfig();
        }
        
        setFromConfig();
    }
    
    private void initControl() {
        setMode(MODE_START);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveToConfig();
                if(!configService.saveSyncConfig(config)) {
                    logger.error("Save Config to file error");
                }
                System.exit(0);
            }
        });
        
        baseSelectPathBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                File old = null;
                if(!Nullable.isNull(basePathTf.getText())) {
                    old = new File(basePathTf.getText());
                }else {
                    old = new File(".");
                }
                
                chooser.setSelectedFile(old);
                if(JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(frame)){
                    File selected = chooser.getSelectedFile();
                    basePathTf.setText(selected.getAbsolutePath());
                    reset();
                }
            }
        });
        
        destSelectPathBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                File old = null;
                if(!Nullable.isNull(destPathTf.getText())) {
                    old = new File(destPathTf.getText());
                }else {
                    old = new File(".");
                }
                
                chooser.setSelectedFile(old);
                if(JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(frame)){
                    File selected = chooser.getSelectedFile();
                    destPathTf.setText(selected.getAbsolutePath());
                    reset();
                }
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
        
        basePathTf.getDocument().addDocumentListener(docListener);
        destPathTf.getDocument().addDocumentListener(docListener);
        
        baseToDestBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(Nullable.isNull(basePathTf.getText())) {
                    JOptionPane.showMessageDialog(frame, "请选择源目录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(Nullable.isNull(destPathTf.getText())) {
                    JOptionPane.showMessageDialog(frame, "请选择目标目录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                pathA = new File(basePathTf.getText());
                pathB = new File(destPathTf.getText());
                
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
                if(Nullable.isNull(basePathTf.getText())) {
                    JOptionPane.showMessageDialog(frame, "请选择源目录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(Nullable.isNull(destPathTf.getText())) {
                    JOptionPane.showMessageDialog(frame, "请选择目标目录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                pathA = new File(destPathTf.getText());
                pathB = new File(basePathTf.getText());
                
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
                List<FileOperationItem> list = operResultList.getSelectedValuesList();
                if(list == null || list.size() <= 0) {
                    return;
                }
                for(FileOperationItem item : list) {
                    listModel.removeElement(item);
                }
            }
        });
        
        resetOperBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                listModel.clear();
                for(FileOperationItem item : result) {
                    listModel.addElement(item);
                }
            }
        });
        
        executeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
            }
        });
    }
    
    private void startScan() {
        resetScanFilter();
        
        fileListA = scanner.scan(pathA);
        fileListB = scanner.scan(pathB);
        
        result = comparer.getCompareResult(pathA, pathB, fileListA, fileListB);
        
        listModel.clear();
        for(FileOperationItem item : result) {
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
        
        basePathTf = new JTextField();
        panel01.add(basePathTf);
        basePathTf.setColumns(17);
        
        baseSelectPathBtn = new JButton("...");
        panel01.add(baseSelectPathBtn);
        
        JPanel panel02 = new JPanel();
        JLabel label02 = new JLabel("目标目录：");
        filePanel.add(panel02, BorderLayout.EAST);
        panel02.add(label02);
        
        destPathTf = new JTextField();
        panel02.add(destPathTf);
        destPathTf.setColumns(17);
        
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
        listPanel.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(listPanel, BorderLayout.CENTER);
        listPanel.add(operResultList, BorderLayout.CENTER);
        
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
        executer = new OperationExecute();
        
        operResultList.setModel(listModel);
        
        executeProcess = new JProgressBar();
        listPanel.add(executeProcess, BorderLayout.SOUTH);
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
