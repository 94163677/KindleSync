package air.kanna.kindlesync.config;

public interface ConfigService<T> {
    T getConfig();
    boolean saveConfig(T config);
}
