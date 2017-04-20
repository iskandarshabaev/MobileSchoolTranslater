package com.ishabaev.mobileschooltranslater;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.ishabaev.mobileschooltranslater.screen.Navigator;
import com.ishabaev.mobileschooltranslater.screen.translate.TranslateFragment;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String CURRENT_TAB_POSITION = "current_tab_position";
    private TabLayout mTabs;
    private int currentTabPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, TranslateFragment.newInstance(), TranslateFragment.TAG)
                    .commit();
        } else {
            int position = savedInstanceState.getInt(CURRENT_TAB_POSITION);
            TabLayout.Tab tab = mTabs.getTabAt(position);
            tab.select();
        }
        Navigator.setTabs(mTabs);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_TAB_POSITION, currentTabPosition);

    }

    private void findViews() {
        mTabs = (TabLayout) findViewById(R.id.tabs);
    }

    private void initViews() {
        mTabs.addTab(mTabs.newTab().setText("Перевод"));
        mTabs.addTab(mTabs.newTab().setText("Закладки"));
        mTabs.addTab(mTabs.newTab().setText("Настройки"));
        mTabs.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (currentTabPosition == position) {
            return;
        }
        switch (position) {
            case 0:
                Navigator.showTranslate(this, position);
                break;
            case 1:
                Navigator.showBookmarks(this, position);
                break;
        }
        currentTabPosition = position;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
