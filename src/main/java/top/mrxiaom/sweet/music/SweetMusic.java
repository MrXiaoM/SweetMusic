package top.mrxiaom.sweet.music;
        
import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.pluginbase.utils.PAPI;
import top.mrxiaom.pluginbase.utils.scheduler.FoliaLibScheduler;
import top.mrxiaom.sweet.music.voicechat.SweetPlugin;

public class SweetMusic extends BukkitPlugin {
    public static SweetMusic getInstance() {
        return (SweetMusic) BukkitPlugin.getInstance();
    }

    public SweetMusic() {
        super(options()
                .bungee(false)
                .adventure(false)
                .database(false)
                .reconnectDatabaseWhenReloadConfig(false)
                .scanIgnore("top.mrxiaom.sweet.music.libs")
        );
        this.scheduler = new FoliaLibScheduler(this);
        if (System.getProperty("top.mrxiaom.sweet.music.loaded") != null) {
            throw new UnsupportedOperationException("由于需要依赖 voicechat 插件的生命周期，本插件不支持热重载");
        }
    }

    @Override
    protected void beforeEnable() {
        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            service.registerPlugin(new SweetPlugin(this));
            System.setProperty("top.mrxiaom.sweet.music.loaded", "true");
        }
    }

    @Override
    protected void afterEnable() {
        if (PAPI.isEnabled()) {
            new Placeholders(this).register();
        }
        getLogger().info("SweetMusic 加载完毕");
    }
}
