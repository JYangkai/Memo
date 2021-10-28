package com.yk.markdown2.core;

import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;

import com.yk.markdown2.core.requester.BaseRequester;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestThreadManager {
    private static volatile RequestThreadManager instance;

    private final ExecutorService ioService;

    private final Handler uiHandler;

    private RequestThreadManager() {
        ioService = Executors.newFixedThreadPool(5);
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public static RequestThreadManager getInstance() {
        if (instance == null) {
            synchronized (RequestThreadManager.class) {
                if (instance == null) {
                    instance = new RequestThreadManager();
                }
            }
        }
        return instance;
    }

    public void executeIoTask(BaseRequester requester) {
        ioService.execute(new RequestIoRunnable(requester));
    }

    public void executeUiTask(BaseRequester requester) {
        uiHandler.post(new RequestUiRunnable(requester));
    }

    public static class RequestIoRunnable implements Runnable {
        private final BaseRequester requester;

        private final MdParser parser;
        private final MdRender render;

        public RequestIoRunnable(BaseRequester requester) {
            this.requester = requester;
            parser = new MdParser();
            render = new MdRender();
        }

        @Override
        public void run() {
            requester.setRequestState(BaseRequester.RequestState.START_REQUEST);

            SpannableStringBuilder spanStrBuilder = render.render(
                    requester.getContext(),
                    parser.parser(requester.getSrc()),
                    requester.getMdStyle()
            );

            requester.setSpanStrBuilder(spanStrBuilder);

            RequestThreadManager.getInstance().executeUiTask(requester);
        }
    }

    public static class RequestUiRunnable implements Runnable {
        private final BaseRequester requester;

        public RequestUiRunnable(BaseRequester requester) {
            this.requester = requester;
        }

        @Override
        public void run() {
            BaseRequester.RequestState requestState = requester.getRequestState();
            if (requestState == BaseRequester.RequestState.STOP_REQUEST) {
                RequestManager.getInstance().removeRequester(requester);
                return;
            }

            requester.getTv().setText(requester.getSpanStrBuilder());

            requester.setRequestState(BaseRequester.RequestState.DONE_REQUEST);
            RequestManager.getInstance().removeRequester(requester);
        }
    }
}
