package com.yk.imageloader2.manager;

import com.yk.imageloader2.lru.ImageDiskLruCache;
import com.yk.imageloader2.request.BaseRequester;

import java.util.ArrayList;
import java.util.List;

public class RequestManager {
    private static volatile RequestManager instance;

    private final List<BaseRequester> requesterList = new ArrayList<>();

    private RequestManager() {
    }

    public static RequestManager getInstance() {
        if (instance == null) {
            synchronized (RequestManager.class) {
                if (instance == null) {
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    public void addRequester(BaseRequester requester) {
        if (requesterList.contains(requester)) {
            return;
        }
        requesterList.add(requester);
        RequestThreadManager.getInstance().executeIoTask(requester);
    }

    public void removeRequester(BaseRequester requester) {
        requesterList.remove(requester);
        if (requesterList.isEmpty()) {
            ImageDiskLruCache.getInstance().flush();
        }
    }
}
