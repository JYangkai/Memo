package com.yk.markdown2.core.requester;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yk.markdown2.style.MdStyleManager;
import com.yk.markdown2.style.style.BaseMdStyle;

public class FragmentActivityRequester extends BaseRequester {
    private final FragmentActivity fa;

    private RequestFragment requestFragment;

    public FragmentActivityRequester(FragmentActivity fa) {
        this.fa = fa;
        addRequestFragment();
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
                .commit();
    }

    private void removeRequestFragment() {
        if (fa == null) {
            return;
        }
        if (requestFragment == null) {
            return;
        }
        fa.getSupportFragmentManager().beginTransaction()
                .remove(requestFragment)
                .commit();
    }

    @Override
    public void setRequestState(RequestState requestState) {
        super.setRequestState(requestState);
        if (requestState == RequestState.DONE_REQUEST || requestState == RequestState.STOP_REQUEST) {
            removeRequestFragment();
        }
    }

    @Override
    public BaseMdStyle getMdStyle() {
        return MdStyleManager.getStyle(fa, getStyle());
    }

    @Override
    public Context getContext() {
        return fa;
    }

    private static class RequestFragment extends Fragment {
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
