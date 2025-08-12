package top.mrxiaom.sweet.music.func;
        
import top.mrxiaom.sweet.music.SweetMusic;

@SuppressWarnings({"unused"})
public abstract class AbstractPluginHolder extends top.mrxiaom.pluginbase.func.AbstractPluginHolder<SweetMusic> {
    public AbstractPluginHolder(SweetMusic plugin) {
        super(plugin);
    }

    public AbstractPluginHolder(SweetMusic plugin, boolean register) {
        super(plugin, register);
    }
}
