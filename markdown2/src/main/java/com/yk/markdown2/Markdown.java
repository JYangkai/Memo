package com.yk.markdown2;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yk.markdown2.core.RequestBuilder;
import com.yk.markdown2.style.MdStyleManager;
import com.yk.markdown2.core.requester.IRequester;

public class Markdown {
    private static MdStyleManager.Style defaultStyle = MdStyleManager.Style.STANDARD;

    public static void init(MdStyleManager.Style style) {
        defaultStyle = style;
    }

    public static MdStyleManager.Style getDefaultStyle() {
        return defaultStyle;
    }

    public static IRequester with(Context context) {
        if (context instanceof FragmentActivity) {
            return with((FragmentActivity) context);
        } else if (context instanceof Activity) {
            return with((Activity) context);
        }
        return with((Application) context);
    }

    public static IRequester with(Application application) {
        return RequestBuilder.get(application);
    }

    public static IRequester with(FragmentActivity fragmentActivity) {
        return RequestBuilder.get(fragmentActivity);
    }

    public static IRequester with(Activity activity) {
        return RequestBuilder.get(activity);
    }

    public static IRequester with(Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static IRequester with(android.app.Fragment fragment) {
        return with(fragment.getActivity());
    }

    public static IRequester with(View view) {
        return with(view.getContext());
    }

}