package com.yk.imageloader2.manager;

import android.app.Activity;
import android.app.Application;

import androidx.fragment.app.FragmentActivity;

import com.yk.imageloader2.request.ActivityRequester;
import com.yk.imageloader2.request.ApplicationRequester;
import com.yk.imageloader2.request.FragmentActivityRequester;
import com.yk.imageloader2.request.IRequester;

public class RequestBuilder {

    public static IRequester get(Application application) {
        return new ApplicationRequester(application);
    }

    public static IRequester get(FragmentActivity fragmentActivity) {
        return new FragmentActivityRequester(fragmentActivity);
    }

    public static IRequester get(Activity activity) {
        return new ActivityRequester(activity);
    }

}
