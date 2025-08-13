package top.mrxiaom.sweet.music.commands;
        
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.pluginbase.utils.Util;
import top.mrxiaom.sweet.music.SweetMusic;
import top.mrxiaom.sweet.music.assets.Asset;
import top.mrxiaom.sweet.music.assets.AssetsManager;
import top.mrxiaom.sweet.music.func.AbstractModule;
import top.mrxiaom.sweet.music.voicechat.SweetPlugin;

import java.util.*;

@AutoRegister
public class CommandMain extends AbstractModule implements CommandExecutor, TabCompleter, Listener {
    public CommandMain(SweetMusic plugin) {
        super(plugin);
        registerCommand("sweetmusic", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        boolean silent = false;
        for (String arg : args) {
            if (arg.equals("-s") || arg.equals("--silent")) {
                silent = true;
                break;
            }
        }
        if (args.length == 1 && "list".equalsIgnoreCase(args[0]) && sender.isOp()) {
            t(sender, "&e资源列表如下:");
            for (String key : AssetsManager.inst().keys()) {
                t(sender, "&f  " + key);
            }
            return true;
        }
        if (args.length >= 2 && "play".equalsIgnoreCase(args[0]) && sender.isOp()) {
            Asset asset = AssetsManager.inst().get(args[1]);
            if (asset == null) {
                return t(sender, "&c资源&e " + args[1] + " &c不存在");
            }
            Player target;
            if (args.length == 3 && !args[2].equals("-s") && !args[2].equals("--silent")) {
                target = Util.getOnlinePlayer(args[2]).orElse(null);
                if (target == null) {
                    return t(sender, "&e玩家不在线 (或不存在)");
                }
            } else {
                if (sender instanceof Player) {
                    target = (Player) sender;
                } else {
                    return t(sender, "&c只有玩家才能执行该命令");
                }
            }
            SweetPlugin.inst().sendAudio(target, asset, true);
            if (silent) {
                return true;
            } else {
                return t(sender, "&a已执行播放&e " + asset.display());
            }
        }
        if (args.length >= 1 && "stop".equalsIgnoreCase(args[0]) && sender.isOp()) {
            Player target;
            if (args.length == 2 && !args[1].equals("-s") && !args[1].equals("--silent")) {
                target = Util.getOnlinePlayer(args[1]).orElse(null);
                if (target == null) {
                    return t(sender, "&e玩家不在线 (或不存在)");
                }
            } else {
                if (sender instanceof Player) {
                    target = (Player) sender;
                } else {
                    return t(sender, "&c只有玩家才能执行该命令");
                }
            }
            SweetPlugin.inst().getPlayer(target).stopCurrentAudio(true);
            if (silent) {
                return true;
            } else {
                return t(sender, "&a已停止播放");
            }
        }
        if (args.length >= 2 && "loop".equalsIgnoreCase(args[0]) && sender.isOp()) {
            Asset asset = AssetsManager.inst().get(args[1]);
            if (asset == null) {
                return t(sender, "&c资源&e " + args[1] + " &c不存在");
            }
            Player target;
            if (args.length == 3 && !args[2].equals("-s") && !args[2].equals("--silent")) {
                target = Util.getOnlinePlayer(args[2]).orElse(null);
                if (target == null) {
                    return t(sender, "&e玩家不在线 (或不存在)");
                }
            } else {
                if (sender instanceof Player) {
                    target = (Player) sender;
                } else {
                    return t(sender, "&c只有玩家才能执行该命令");
                }
            }
            SweetPlugin.inst().sendLoopAudio(target, asset, true);
            if (silent) {
                return true;
            } else {
                return t(sender, "&a已执行循环播放&e " + asset.display());
            }
        }
        if (args.length >= 3 && "optional".equalsIgnoreCase(args[0]) && sender.isOp()) {
            if ("play".equalsIgnoreCase(args[1]) && sender.isOp()) {
                Asset asset = AssetsManager.inst().get(args[2]);
                if (asset == null) {
                    return t(sender, "&c资源&e " + args[2] + " &c不存在");
                }
                Player target;
                if (args.length == 4 && !args[3].equals("-s") && !args[3].equals("--silent")) {
                    target = Util.getOnlinePlayer(args[3]).orElse(null);
                    if (target == null) {
                        return t(sender, "&e玩家不在线 (或不存在)");
                    }
                } else {
                    if (sender instanceof Player) {
                        target = (Player) sender;
                    } else {
                        return t(sender, "&c只有玩家才能执行该命令");
                    }
                }
                SweetPlugin.inst().sendAudio(target, asset, false);
                if (silent) {
                    return true;
                } else {
                    return t(sender, "&a已执行播放&e " + asset.display());
                }
            }
            if ("loop".equalsIgnoreCase(args[1]) && sender.isOp()) {
                Asset asset = AssetsManager.inst().get(args[2]);
                if (asset == null) {
                    return t(sender, "&c资源&e " + args[2] + " &c不存在");
                }
                Player target;
                if (args.length == 4 && !args[3].equals("-s") && !args[3].equals("--silent")) {
                    target = Util.getOnlinePlayer(args[3]).orElse(null);
                    if (target == null) {
                        return t(sender, "&e玩家不在线 (或不存在)");
                    }
                } else {
                    if (sender instanceof Player) {
                        target = (Player) sender;
                    } else {
                        return t(sender, "&c只有玩家才能执行该命令");
                    }
                }
                SweetPlugin.inst().sendLoopAudio(target, asset, false);
                if (silent) {
                    return true;
                } else {
                    return t(sender, "&a已执行循环播放&e " + asset.display());
                }
            }
        }
        if (args.length == 1 && "reload".equalsIgnoreCase(args[0]) && sender.isOp()) {
            plugin.reloadConfig();
            return t(sender, "&a配置文件已重载");
        }
        return true;
    }

    private static final List<String> listArg0 = Lists.newArrayList();
    private static final List<String> listOpArg0 = Lists.newArrayList(
            "list", "play", "stop", "loop", "optional", "reload");
    private static final List<String> listOptionalArg1 = Lists.newArrayList(
            "play", "loop");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return startsWith(sender.isOp() ? listOpArg0 : listArg0, args[0]);
        }
        if (args.length == 2) {
            if (sender.isOp()) {
                if ("play".equalsIgnoreCase(args[0]) || "loop".equalsIgnoreCase(args[0])) {
                    return startsWith(AssetsManager.inst().keys(), args[1]);
                }
                if ("optional".equalsIgnoreCase(args[0])) {
                    return startsWith(listOptionalArg1, args[1]);
                }
            }
        }
        if (args.length == 3) {
            if (sender.isOp()) {
                if ("optional".equalsIgnoreCase(args[0])) {
                    if ("play".equalsIgnoreCase(args[1]) || "loop".equalsIgnoreCase(args[1])) {
                        return startsWith(AssetsManager.inst().keys(), args[2]);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    public List<String> startsWith(Collection<String> list, String s) {
        return startsWith(null, list, s);
    }
    public List<String> startsWith(String[] addition, Collection<String> list, String s) {
        String s1 = s.toLowerCase();
        List<String> stringList = new ArrayList<>(list);
        if (addition != null) stringList.addAll(0, Lists.newArrayList(addition));
        stringList.removeIf(it -> !it.toLowerCase().startsWith(s1));
        return stringList;
    }
}
