package air.kanna.kindlesync.scan.filter;

public class FileNameExcludeFilter extends FileNameIncludeFilter{

    public FileNameExcludeFilter(String include) {
        super(include);
        defaultAccept = true;
        isAcceptTrue = false;
    }
}
