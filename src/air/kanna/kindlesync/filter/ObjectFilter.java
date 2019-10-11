package air.kanna.kindlesync.filter;

public interface ObjectFilter<T> {
    boolean accept(T object);
}
