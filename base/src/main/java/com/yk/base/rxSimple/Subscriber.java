package com.yk.base.rxSimple;

public interface Subscriber<T> {
    void onNext(T t);

    void onComplete();

    void onError(Exception e);
}
