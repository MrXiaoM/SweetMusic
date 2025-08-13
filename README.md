# SweetMusic

Minecraft 通过 [Simple Voice Chat](https://modrinth.com/plugin/simple-voice-chat/versions?l=bukkit) 语音聊天 Mod 的服务端插件，进行单音轨的音乐/音效播放。

## 简介

由于在 Minecraft 原版，使用 [`/playsound`](https://zh.minecraft.wiki/w/%E5%91%BD%E4%BB%A4/playsound) 或发包播放音效，无论如何都会在玩家移动时发生声场变化。

于是我在读取文件上参考 fabric 上的 [audio-player](https://github.com/henkelmax/audio-player) mod，写出了一个音频资源管理器，再与 voicechat 插件进行交互，为每个玩家分配一条固定音轨，为每个玩家播放不同的音效/音乐。

目前仅支持播放 `.wav` 音频。

> 这个插件当前正在早期开发阶段，是从作者的私人项目中分离出来的部分。  
> 计划在正式版实现以下功能
> + 可配置每个玩家拥有自定义数量的音轨，命令控制在哪个音轨播放音乐/音效，以及停止播放
> + 进入区域自动播放音乐，离开区域自动停止播放
> + 多个玩家共享同一条音轨

## 命令

根命令为 `/sweetmusic`，别名为 `/smusic`, `/music`。  
以 `<>` 包裹的为必选参数，以 `[]` 包裹的为可选参数。  
所有命令均需要管理员权限才可执行。

| 命令                               | 描述                                |
|----------------------------------|-----------------------------------|
| `/music list`                    | 查看可以使用的音频资源列表                     |
| `/music play <音频> [玩家]`          | 为某人或自己播放一次音频                      |
| `/music loop <音频> [玩家]`          | 为某人或自己循环播放音频                      |
| `/music stop [玩家]`               | 为某人或自己停止播放音频                      |
| `/music optional play <音频> [玩家]` | 为某人或自己播放一次音频，如果已经有音频在播放了，则不进行任何操作 |
| `/music optional loop <音频> [玩家]` | 为某人或自己循环播放音频，如果已经有音频在播放了，则不进行任何操作 |
| `/music reload`                  | 重载配置文件                            |

对于 `play`、`loop`、`stop` 命令，可以在结尾添加 `-s` 或 `--silent`，使得命令成功执行时不输出任何提示。

音频放入 `assets` 文件夹内，插件将会在 1 秒内扫描到文件变更，自动进行加载，无需手动执行命令重载。

## 变量

本插件在 PlaceholderAPI 注册了如下变量

```
%sweetmusic_is_connected% -- 玩家是否已通过 Simple Voice Chat 连接到服务器
%sweetmusic_is_playing% -- 玩家当前是否正在播放音乐
%sweetmusic_playing_asset% -- 玩家当前正在播放的音乐显示名
%sweetmusic_playing_asset_raw% -- 玩家当前正在播放的音乐原始文件名
```
