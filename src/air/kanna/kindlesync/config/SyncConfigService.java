package air.kanna.kindlesync.config;

public interface SyncConfigService {
    SyncConfig getSyncConfig();
    boolean saveSyncConfig(SyncConfig config);
}
