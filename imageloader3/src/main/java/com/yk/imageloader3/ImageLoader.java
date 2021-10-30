package com.yk.imageloader3;

/**
 * 图片异步加载器（验证RecyclerView滑动时，图片错位问题）
 */
public class ImageLoader {

    public static Requester load(String path) {
        return Requester.get(path);
    }

}
