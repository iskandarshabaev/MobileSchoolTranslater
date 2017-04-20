package com.ishabaev.mobileschooltranslater.screen.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class RxEditText extends AppCompatEditText {

    private PublishSubject<String> editTextSubject = PublishSubject.create();
    private Subscription subscription;
    private boolean ignoreTextChanging;

    public RxEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RxEditText(Context context) {
        super(context);
    }

    public RxEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (editTextSubject != null && !ignoreTextChanging) {
            editTextSubject.onNext(text.toString());
        }
    }

    public void setOnRxTextChangeListener(RxEditTextChangeListener listener, int debounce) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = editTextSubject.asObservable()
                .debounce(debounce, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnNext(listener::onTextChanged)
                .subscribe();
    }

    public void setTextWithoutNotifyinng(String text) {
        ignoreTextChanging = true;
        setText(text);
        ignoreTextChanging = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public interface RxEditTextChangeListener {
        void onTextChanged(String text);
    }
}
