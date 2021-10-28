package com.yk.imageloader2.request;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class FragmentActivityRequester extends BaseRequester {
    private final FragmentActivity fa;

    private RequestFragment requestFragment;

    public FragmentActivityRequester(FragmentActivity fa) {
        this.fa = fa;
        addRequestFragment();
    }

    @Override
    public Context getContext() {
        return fa;
    }

    private void addRequestFragment() {
        if (fa == null) {
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
        fa.getSupportFragmentManager().beginTransaction()
                .add(requestFragment, RequestFragment.TAG)
                .commitAllowingStateLoss();
    }

    private void removeRequestFragment() {
        if (fa == null) {
            return;
        }
        if (requestFragment == null) {
            return;
        }
        requestFragment.setOnLifeCircleListener(null);
        fa.getSupportFragmentManager().beginTransaction()
                .remove(requestFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void setRequestState(RequestState requestState) {
        super.setRequestState(requestState);
        if (requestState == RequestState.DONE_REQUEST || requestState == RequestState.STOP_REQUEST) {
            removeRequestFragment();
        }
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
