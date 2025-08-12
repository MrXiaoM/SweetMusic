package top.mrxiaom.sweet.music.voicechat;

import de.maxhenkel.voicechat.api.ServerPlayer;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import org.bukkit.entity.Player;
import top.mrxiaom.sweet.music.assets.Asset;

import java.util.UUID;

public class VoicePlayer {
    private final VoicechatServerApi api;
    private final Player player;
    private final ServerPlayer serverPlayer;
    private VoicechatConnection connection;
    private StaticAudioChannel channel;
    private AudioPlayer audioPlayer;
    private Asset playingAsset;
    private String lastWorld;
    public VoicePlayer(VoicechatServerApi api, Player player) {
        this.api = api;
        this.player = player;
        this.serverPlayer = api.fromServerPlayer(player);
        this.connection = api.getConnectionOf(serverPlayer);
        this.channel = getChannel();
    }

    private void ensureConnected() {
        if (connection == null) connection = api.getConnectionOf(serverPlayer);
    }

    public StaticAudioChannel getChannel() {
        if (!isConnected()) return null;
        String currentWorld = player.getWorld().getName();
        if (channel == null || !currentWorld.equals(lastWorld)) {
            lastWorld = currentWorld;
            channel = api.createStaticAudioChannel(UUID.randomUUID(), serverPlayer.getServerLevel(), connection);
        }
        return channel;
    }

    public void stopCurrentAudio(boolean ignoreStoppedAction) {
        if (!isConnected()) return;
        if (audioPlayer != null) {
            if (ignoreStoppedAction) {
                audioPlayer.setOnStopped(() -> {});
            }
            audioPlayer.stopPlaying();
            audioPlayer = null;
        }
    }

    public AudioPlayer createAudioPlayer(OpusEncoder encoder, Asset asset) {
        this.playingAsset = asset;
        this.audioPlayer = api.createAudioPlayer(getChannel(), encoder, asset.data());
        return this.audioPlayer;
    }

    public Asset getPlayingAsset() {
        return isPlaying() ? playingAsset : null;
    }

    public boolean canCreatePlayerNow() {
        return audioPlayer == null || audioPlayer.isStopped();
    }

    public boolean isPlaying() {
        return audioPlayer != null && audioPlayer.isPlaying();
    }

    public boolean isConnected() {
        ensureConnected();
        return connection != null && connection.isConnected();
    }

    public void dispose() {

    }
}
