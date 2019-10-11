package air.kanna.kindlesync.scan.filter;

public class FileNameRegexExcludeFilter extends FileNameRegexIncludeFilter {
    
    public FileNameRegexExcludeFilter(String include) {
        super(include);
        defaultAccept = true;
        isAcceptTrue = false;
    }
}
