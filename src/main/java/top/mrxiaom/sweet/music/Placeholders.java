package top.mrxiaom.sweet.music;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.utils.PlaceholdersExpansion;
import top.mrxiaom.sweet.music.assets.Asset;
import top.mrxiaom.sweet.music.voicechat.SweetPlugin;
import top.mrxiaom.sweet.music.voicechat.VoicePlayer;

public class Placeholders extends PlaceholdersExpansion<SweetMusic> {
    public Placeholders(SweetMusic plugin) {
        super(plugin);
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("is_connected")) {
            VoicePlayer p = SweetPlugin.inst().getPlayer(player);
            return bool(p.isConnected());
        }
        if (params.equalsIgnoreCase("is_playing")) {
            VoicePlayer p = SweetPlugin.inst().getPlayer(player);
            return bool(p.isPlaying());
        }
        if (params.equalsIgnoreCase("playing_asset")) {
            VoicePlayer p = SweetPlugin.inst().getPlayer(player);
            Asset asset = p.getPlayingAsset();
            if (asset != null) {
                return asset.display();
            } else {
                return "";
            }
        }
        if (params.equalsIgnoreCase("playing_asset_raw")) {
            VoicePlayer p = SweetPlugin.inst().getPlayer(player);
            Asset asset = p.getPlayingAsset();
            if (asset != null) {
                return asset.key();
            } else {
                return "";
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}
