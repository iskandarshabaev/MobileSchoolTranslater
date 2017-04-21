package com.ishabaev.mobileschooltranslater.screen;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;

import static android.view.View.GONE;

public class Animations {

    private static final int DURATION = 500;

    public static ObjectAnimator makeShow(View v, int y) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "translationY", y);
        animation.setDuration(DURATION);
        animation.setInterpolator(new OvershootInterpolator(0.5f));
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return animation;
    }

    public static ObjectAnimator makeHide(View v, int y) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "translationY", y);
        animation.setDuration(DURATION);
        animation.setInterpolator(new OvershootInterpolator(0.5f));
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return animation;
    }
}
