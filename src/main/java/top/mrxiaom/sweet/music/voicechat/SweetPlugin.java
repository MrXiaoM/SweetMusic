package top.mrxiaom.sweet.music.voicechat;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import top.mrxiaom.sweet.music.SweetMusic;
import top.mrxiaom.sweet.music.assets.Asset;
import top.mrxiaom.sweet.music.assets.AssetsManager;
import top.mrxiaom.sweet.music.func.AbstractPluginHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SweetPlugin extends AbstractPluginHolder implements VoicechatPlugin, Listener {
    private VoicechatApi api;
    private VoicechatServerApi serverApi;
    private final Map<UUID, VoicePlayer> players = new HashMap<>();
    public SweetPlugin(SweetMusic plugin) {
        super(plugin, true);
        registerEvents();
    }

    @Override
    public String getPluginId() {
        return "SweetMusic";
    }

    public VoicechatApi getApi() {
        return api;
    }

    public VoicechatServerApi getServerApi() {
        return serverApi;
    }

    @NotNull
    public VoicePlayer getPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        VoicePlayer loaded = players.get(uuid);
        if (loaded != null) {
            return loaded;
        }
        VoicePlayer voicePlayer = new VoicePlayer(serverApi, player);
        players.put(uuid, voicePlayer);
        return voicePlayer;
    }

    public void sendAudio(Player player, Asset asset) {
        sendAudio(player, asset, false);
    }

    public void sendAudio(Player player, Asset asset, boolean stopCurrent) {
        VoicePlayer voicePlayer = getPlayer(player);
        if (voicePlayer.isConnected()) {
            if (stopCurrent) {
                voicePlayer.stopCurrentAudio(true);
            }
            if (voicePlayer.canCreatePlayerNow()) {
                AudioPlayer p = voicePlayer.createAudioPlayer(api.createEncoder(), asset);
                p.startPlaying();
            }
        }
    }

    public void sendLoopAudio(Player player, Asset asset) {
        sendLoopAudio(player, asset, false);
    }

    public void sendLoopAudio(Player player, Asset asset, boolean stopCurrent) {
        VoicePlayer voicePlayer = getPlayer(player);
        if (voicePlayer.isConnected()) {
            if (stopCurrent) {
                voicePlayer.stopCurrentAudio(true);
            }
            if (voicePlayer.canCreatePlayerNow()) {
                AudioPlayer p = voicePlayer.createAudioPlayer(api.createEncoder(), asset);
                p.setOnStopped(() -> sendLoopAudio(player, asset, stopCurrent));
                p.startPlaying();
            }
        }
    }

    @Override
    public void initialize(VoicechatApi api) {
        this.api = api;
        AssetsManager.inst().reloadAssets();
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }

    private void onServerStarted(VoicechatServerStartedEvent event) {
        serverApi = event.getVoicechat();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        VoicePlayer remove = players.remove(e.getPlayer().getUniqueId());
        if (remove != null) {
            remove.dispose();
        }
    }

    public static SweetPlugin inst() {
        return instanceOf(SweetPlugin.class);
    }

    public static VoicechatApi api() {
        return inst().getApi();
    }

    public static VoicechatServerApi serverApi() {
        return inst().getServerApi();
    }
}
