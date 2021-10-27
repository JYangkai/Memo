package com.yk.markdown2.core;

import android.app.Activity;
import android.app.Application;

import androidx.fragment.app.FragmentActivity;

import com.yk.markdown2.core.requester.ActivityRequester;
import com.yk.markdown2.core.requester.ApplicationRequester;
import com.yk.markdown2.core.requester.FragmentActivityRequester;
import com.yk.markdown2.core.requester.IRequester;

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
