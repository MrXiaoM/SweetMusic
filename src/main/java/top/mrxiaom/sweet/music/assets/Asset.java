package top.mrxiaom.sweet.music.assets;

import java.io.File;

public class Asset {
    private final String key;
    private String display;
    private final File file;
    private final short[] data;

    public Asset(String key, File file, short[] data) {
        this.key = key;
        this.display = key;
        this.file = file;
        this.data = data;
    }

    public String key() {
        return key;
    }

    public String display() {
        return display;
    }

    public File file() {
        return file;
    }

    public short[] data() {
        return data;
    }

    public void display(String display) {
        this.display = display;
    }
}
