package com.yk.markdown2.core;

import com.yk.markdown2.core.requester.BaseRequester;

import java.util.ArrayList;
import java.util.List;

public class RequestManager {
    private final List<BaseRequester> requesterList = new ArrayList<>();

    private static volatile RequestManager instance;

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
    }
}
