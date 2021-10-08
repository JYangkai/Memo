package com.yk.memo;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.yk.base.rxjava.Observable;
import com.yk.base.rxjava.Subscriber;
import com.yk.markdown.Markdown;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String src = "普通\n" +
            "\n" +
            "> 引用\n" +
            "\n" +
            "```java\n" +
            "代码块\n" +
            "```\n" +
            "\n" +
            "- 无序列表1\n" +
            "- 无序列表2\n" +
            "\n" +
            "1. 有序列表1\n" +
            "2. 有序列表2\n" +
            "\n" +
            "# 标题1\n" +
            "\n" +
            "## 标题2\n" +
            "\n" +
            "### 标题3\n" +
            "\n" +
            "#### 标题4\n" +
            "\n" +
            "##### 标题5\n" +
            "\n" +
            "###### 标题6\n" +
            "\n" +
            "---";

    private AppCompatTextView tvMd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMd = findViewById(R.id.tvMd);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvMd.post(new Runnable() {
            @Override
            public void run() {
                test();
            }
        });
    }

    private void test() {
        Observable.fromCallable(new Observable.OnCallable<SpannableStringBuilder>() {
            @Override
            public SpannableStringBuilder call() {
                Log.d(TAG, "call: ");
                return Markdown.getMd(src);
            }
        })
                .subscribeOnIo()
                .observeOnUi()
                .subscribe(new Subscriber<SpannableStringBuilder>() {
                    @Override
                    public void onNext(SpannableStringBuilder spannableStringBuilder) {
                        Log.d(TAG, "onNext: " + spannableStringBuilder);
                        tvMd.setText(spannableStringBuilder);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }
}