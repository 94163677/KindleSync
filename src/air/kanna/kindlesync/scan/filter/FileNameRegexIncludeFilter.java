package air.kanna.kindlesync.scan.filter;

import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FileNameRegexIncludeFilter extends FileNameIncludeFilter {

    public FileNameRegexIncludeFilter(String include) {
        super(include);
    }
    
    @Override
    boolean acceptKey(String key, File file) {
        Pattern p = Pattern.compile(key);
        Matcher m = p.matcher(file.getName());
        return m.find();
    }
}
