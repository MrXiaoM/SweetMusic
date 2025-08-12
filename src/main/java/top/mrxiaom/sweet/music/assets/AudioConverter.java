package top.mrxiaom.sweet.music.assets;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.mp3.Mp3Decoder;
import top.mrxiaom.sweet.music.SweetMusic;
import top.mrxiaom.sweet.music.voicechat.SweetPlugin;

import javax.annotation.Nullable;
import javax.sound.sampled.*;
import java.io.*;

/**
 * Modified from <a href="https://github.com/henkelmax/audio-player/blob/master/src/main/java/de/maxhenkel/audioplayer/AudioConverter.java">audio-player</a>
 */
public class AudioConverter {

    public static AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000F, 16, 1, 2, 48000F, false);

    @Nullable
    public static AudioType getAudioType(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(bis);
            String type = fileFormat.getType().toString();
            if (type.equalsIgnoreCase("wave")) {
                return AudioType.WAV;
            }
            if (type.equalsIgnoreCase("mp3")) {
                return AudioType.MP3;
            }
        } catch (UnsupportedAudioFileException e) {
            return null;
        }
        return null;
    }

    public static short[] convert(File file, AudioType audioType, float volume) throws IOException, UnsupportedAudioFileException {
        if (audioType == AudioType.WAV) {
            return convertWav(file, volume);
        } else if (audioType == AudioType.MP3) {
            return convertMp3(file, volume);
        }
        throw new UnsupportedAudioFileException("Unsupported audio type");
    }

    public static short[] convertWav(File file, float volume) throws IOException, UnsupportedAudioFileException {
        try (AudioInputStream source = AudioSystem.getAudioInputStream(file)) {
            return convert(source, volume);
        }
    }

    private static short[] convert(AudioInputStream source, float volume) throws IOException {
        AudioFormat sourceFormat = source.getFormat();
        AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
        try (AudioInputStream stream1 = AudioSystem.getAudioInputStream(convertFormat, source);
            AudioInputStream stream2 = AudioSystem.getAudioInputStream(FORMAT, stream1);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024 * 1024]; int len;
            while ((len = stream2.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return SweetPlugin.api().getAudioConverter().bytesToShorts(adjustVolume(out.toByteArray(), volume));
        }
    }

    private static byte[] adjustVolume(byte[] audioSamples, float volume) {
        for (int i = 0; i < audioSamples.length; i += 2) {
            short buf1 = audioSamples[i + 1];
            short buf2 = audioSamples[i];

            buf1 = (short) ((buf1 & 0xFF) << 8);
            buf2 = (short) (buf2 & 0xFF);

            short res = (short) (buf1 | buf2);
            res = (short) (res * volume);

            audioSamples[i] = (byte) res;
            audioSamples[i + 1] = (byte) (res >> 8);

        }
        return audioSamples;
    }

    public static short[] convertMp3(File file, float volume) throws IOException, UnsupportedAudioFileException {
        try (FileInputStream fis = new FileInputStream(file)) {
            VoicechatApi api = SweetPlugin.api();
            Mp3Decoder mp3Decoder = api.createMp3Decoder(fis);
            if (mp3Decoder == null) {
                throw new IOException("Error creating mp3 decoder");
            }
            byte[] data = api.getAudioConverter().shortsToBytes(mp3Decoder.decode());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            AudioFormat audioFormat = mp3Decoder.getAudioFormat();
            AudioInputStream source = new AudioInputStream(byteArrayInputStream, audioFormat, data.length / audioFormat.getFrameSize());
            return convert(source, volume);
        } catch (Exception e) {
            SweetMusic.getInstance().warn("Error converting mp3 file with native decoder");
            return convert(AudioSystem.getAudioInputStream(file), volume);
        }
    }

    public enum AudioType { MP3, WAV }
}
