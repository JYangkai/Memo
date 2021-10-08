package com.yk.base.rxjava;

public interface Subscriber<T> {
    void onNext(T t);

    void onComplete();

    void onError(Exception e);
}
