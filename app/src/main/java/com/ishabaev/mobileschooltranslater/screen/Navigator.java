package com.ishabaev.mobileschooltranslater.screen;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ishabaev.mobileschooltranslater.R;
import com.ishabaev.mobileschooltranslater.screen.bookmarks.BookmarksFragment;
import com.ishabaev.mobileschooltranslater.screen.langs.LangsActivity;
import com.ishabaev.mobileschooltranslater.screen.translate.TranslateFragment;

import java.lang.ref.WeakReference;

public class Navigator {

    private static int currentTabPosition;
    private static WeakReference<TabLayout> sTabs;

    public static void setTabs(TabLayout tabs) {
        sTabs = new WeakReference<>(tabs);
    }

    public static void showTranslate(FragmentActivity activity, int position) {
        changeFragment(activity, TranslateFragment.newInstance(), position, TranslateFragment.TAG);
    }

    public static void showTranslate(FragmentActivity activity, int position, Long id) {
        TabLayout.Tab tab = sTabs.get().getTabAt(0);
        tab.select();
        changeFragment(activity, TranslateFragment.newInstance(id), position, TranslateFragment.TAG);
    }

    public static void showLangs(Fragment fragment, int lang, int id) {
        LangsActivity.startActivityForResult(fragment, lang, id);
    }

    public static void showBookmarks(FragmentActivity activity, int position) {
        changeFragment(activity, BookmarksFragment.newInstance(), position, BookmarksFragment.TAG);
    }

    public static void changeFragment(FragmentActivity activity, Fragment fragment, int position, String tag) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (position > currentTabPosition) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);
        }
        transaction.replace(R.id.fragment_container, fragment, tag).commit();
    }
}
