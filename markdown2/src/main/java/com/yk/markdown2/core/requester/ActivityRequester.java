package com.yk.markdown2.core.requester;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

public class ActivityRequester extends BaseRequester {
    private final Activity activity;

    private RequestFragment requestFragment;

    public ActivityRequester(Activity activity) {
        this.activity = activity;
        addRequestFragment();
    }

    private void addRequestFragment() {
        if (activity == null) {
            return;
        }
        if (requestFragment == null) {
            requestFragment = RequestFragment.newInstance();
        }
        requestFragment.setOnLifeCircleListener(new OnLifeCircleListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {
                setRequestState(RequestState.STOP_REQUEST);
            }
        });
        activity.getFragmentManager().beginTransaction()
                .add(requestFragment, RequestFragment.TAG)
                .commitAllowingStateLoss();
    }

    private void removeRequestFragment() {
        if (activity == null) {
            return;
        }
        if (requestFragment == null) {
            return;
        }
        requestFragment.setOnLifeCircleListener(null);
        activity.getFragmentManager().beginTransaction()
                .remove(requestFragment)
                .commitAllowingStateLoss();
        requestFragment = null;
    }

    @Override
    public void setRequestState(RequestState requestState) {
        super.setRequestState(requestState);
        if (requestState == RequestState.DONE_REQUEST || requestState == RequestState.STOP_REQUEST) {
            removeRequestFragment();
        }
    }

    @Override
    public Context getContext() {
        return activity;
    }

    public static class RequestFragment extends Fragment {
        public static final String TAG = "RequestFragment";

        public static RequestFragment newInstance() {
            return new RequestFragment();
        }

        private OnLifeCircleListener onLifeCircleListener;

        public void setOnLifeCircleListener(OnLifeCircleListener onLifeCircleListener) {
            this.onLifeCircleListener = onLifeCircleListener;
        }

        @Override
        public void onStart() {
            super.onStart();
            if (onLifeCircleListener != null) {
                onLifeCircleListener.onStart();
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            if (onLifeCircleListener != null) {
                onLifeCircleListener.onStop();
            }
        }
    }
}
