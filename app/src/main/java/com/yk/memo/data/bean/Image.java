package com.yk.memo.data.bean;

public class Image {
    private String path;
    private String name;

    public Image(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Image{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
