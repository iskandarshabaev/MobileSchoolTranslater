package com.ishabaev.mobileschooltranslater.screen.langs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ishabaev.mobileschooltranslater.R;
import com.ishabaev.mobileschooltranslater.datasource.CommonDataSource;
import com.ishabaev.mobileschooltranslater.datasource.DataSourceProvider;
import com.ishabaev.mobileschooltranslater.util.RxSchedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Observable;

public class LangsActivity extends AppCompatActivity {

    public static final String LANG_KEY = "lang_key";
    private RecyclerView mLangsRecyclerView;

    public static void startActivityForResult(Fragment fragment, int lang, int id) {
        Intent intent = new Intent(fragment.getContext(), LangsActivity.class);
        intent.putExtra(LANG_KEY, lang);
        fragment.startActivityForResult(intent, id);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langs);
        int lang = getIntent().getIntExtra(LANG_KEY, 0);
        findViews();
        initViews(lang);
    }

    private void findViews() {
        mLangsRecyclerView = (RecyclerView) findViewById(R.id.lang_list);
    }

    private void initViews(int langNumber) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        LangsAdapter langsAdapter = new LangsAdapter(new ArrayList<>());
        langsAdapter.setOnLangSelected(lang -> {
            if (langNumber == 1) {
                DataSourceProvider.provideCommon(this).saveLangA(lang.getKey());
            } else if (langNumber == 2) {
                DataSourceProvider.provideCommon(this).saveLangB(lang.getKey());
            }
            onBackPressed();
        });
        mLangsRecyclerView.setLayoutManager(layoutManager);
        mLangsRecyclerView.setItemAnimator(itemAnimator);
        mLangsRecyclerView.setAdapter(langsAdapter);
        CommonDataSource commonDataSource = DataSourceProvider.provideCommon(this);
        List<Map.Entry<String, String>> langs = commonDataSource.getLangs(Locale.getDefault().getLanguage());
        Observable.from(langs)
                .sorted((stringStringEntry, stringStringEntry2) ->
                        stringStringEntry.getValue().compareTo(stringStringEntry2.getValue()))
                .toList()
                .compose(RxSchedulers.async())
                .subscribe(langsAdapter::changeDataset, Throwable::printStackTrace);
    }

}
