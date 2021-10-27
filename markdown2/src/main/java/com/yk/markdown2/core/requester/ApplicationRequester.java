package com.yk.markdown2.core.requester;

import android.app.Application;
import android.content.Context;

public class ApplicationRequester extends BaseRequester {
    private final Application application;

    public ApplicationRequester(Application application) {
        this.application = application;
    }

    @Override
    public Context getContext() {
        return application;
    }
}
