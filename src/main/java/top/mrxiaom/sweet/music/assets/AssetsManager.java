package top.mrxiaom.sweet.music.assets;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.pluginbase.utils.Util;
import top.mrxiaom.sweet.music.SweetMusic;
import top.mrxiaom.sweet.music.func.AbstractModule;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AutoRegister
public class AssetsManager extends AbstractModule {
    private boolean hasApiLoaded = false;
    private final File directory;
    private FileAlterationMonitor monitor;
    private final Map<String, Asset> assets = new HashMap<>();
    public AssetsManager(SweetMusic plugin) throws Exception {
        super(plugin);
        this.directory = plugin.resolve("./assets");
        Util.mkdirs(this.directory);
        FileAlterationObserver observer = new FileAlterationObserver(this.directory);
        observer.addListener(new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                if (!hasApiLoaded) return;
                String key = Util.getRelationPath(directory, file, true);
                loadAsset(key, file);
            }
            @Override
            public void onFileChange(File file) {
                if (!hasApiLoaded) return;
                String key = Util.getRelationPath(directory, file, true);
                loadAsset(key, file);
            }
            @Override
            public void onFileDelete(File file) {
                if (!hasApiLoaded) return;
                String key = Util.getRelationPath(directory, file, true);
                assets.remove(key);
            }
        });
        this.monitor = new FileAlterationMonitor(1000L);
        this.monitor.addObserver(observer);
        this.monitor.start();
    }

    public void reloadAssets() {
        this.assets.clear();
        load(this.directory);
        this.hasApiLoaded = true;
    }

    private void load(File directory) {
        File[] files = directory.listFiles();
        if (files != null) for (File file : files) {
            if (file.isDirectory()) {
                load(file);
                continue;
            }
            String key = Util.getRelationPath(directory, file, true);
            loadAsset(key, file);
        }
    }

    private void loadAsset(String key, File file) {
        try {
            AudioConverter.AudioType type = AudioConverter.getAudioType(file);
            if (type != null) {
                info("加载文件 " + key + " " + type);
                short[] data = AudioConverter.convert(file, type, 1.0f);
                Asset asset = new Asset(key, file, data);
                assets.put(key, asset);
            }
        } catch (Throwable t) {
            warn("加载文件 " + key + " 时出现错误", t);
            assets.remove(key);
        }
    }

    public Asset get(String key) {
        return assets.get(key);
    }

    public Collection<String> keys() {
        return Collections.unmodifiableCollection(assets.keySet());
    }

    public Collection<Asset> assets() {
        return Collections.unmodifiableCollection(assets.values());
    }

    @Override
    public void onDisable() {
        if (monitor != null) {
            try {
                monitor.stop();
            } catch (Exception ignored) {
            }
            monitor = null;
        }
    }

    public static AssetsManager inst() {
        return instanceOf(AssetsManager.class);
    }
}
