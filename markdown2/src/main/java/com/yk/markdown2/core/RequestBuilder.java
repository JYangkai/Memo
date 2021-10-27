package com.yk.markdown2.core;

import android.app.Activity;
import android.app.Application;

import androidx.fragment.app.FragmentActivity;

import com.yk.markdown2.core.requester.FragmentActivityRequester;
import com.yk.markdown2.core.requester.IRequester;

public class RequestBuilder {

    public static IRequester get(Application application) {
        return null;
    }

    public static IRequester get(FragmentActivity fragmentActivity) {
        return new FragmentActivityRequester(fragmentActivity);
    }

    public static IRequester get(Activity activity) {
        return null;
    }

}
