package com.ishabaev.mobileschooltranslater.screen.translate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishabaev.mobileschooltranslater.R;
import com.ishabaev.mobileschooltranslater.datasource.CacheDataSource;
import com.ishabaev.mobileschooltranslater.datasource.CommonDataSource;
import com.ishabaev.mobileschooltranslater.datasource.DataSourceProvider;
import com.ishabaev.mobileschooltranslater.datasource.TranslateDataSource;
import com.ishabaev.mobileschooltranslater.datasource.Translation;
import com.ishabaev.mobileschooltranslater.screen.Animations;
import com.ishabaev.mobileschooltranslater.screen.Navigator;
import com.ishabaev.mobileschooltranslater.screen.bookmarks.BookmarksAdapter;
import com.ishabaev.mobileschooltranslater.screen.langs.LangsActivity;
import com.ishabaev.mobileschooltranslater.screen.view.RxEditText;
import com.ishabaev.mobileschooltranslater.util.RxSchedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TranslateFragment extends Fragment implements TranslateView, View.OnClickListener {

    public static String TAG = "translate_fragment";
    public static String TRANSLATION_ID = "translation_id";

    private RxEditText mText;
    private ImageView mSwapButton;
    private TranslatePresenter mPresenter;
    private ImageView mClearButton;
    private TextView mTranslationTextView;
    private TextView mLanguageA;
    private TextView mLanguageB;
    private TextView mSpeechTextView;
    private TextView mTranslatedSpeechTextView;
    private ImageView mFavoritesButton;
    private RecyclerView mHistoryRecyclerView;
    private BookmarksAdapter adapter;
    private NestedScrollView mNestedScrollView;
    private CardView mTranslationCard;
    private CardView mHistoryCard;

    public static Fragment newInstance() {
        return newInstance(null);
    }

    public static Fragment newInstance(@Nullable Long translationId) {
        TranslateFragment fragment = new TranslateFragment();
        if (translationId != null) {
            Bundle args = new Bundle();
            args.putLong(TRANSLATION_ID, translationId);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);
        initPresenter();
        findViews(rootView);
        initViews();
        mTranslationCard.setY(-mTranslationCard.getHeight() -
                getResources().getDimensionPixelSize(R.dimen.margin_medium));
        mTranslationCard.setVisibility(View.GONE);
        mPresenter.loadLangs(Locale.getDefault().getLanguage());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null) {
            long id = args.getLong(TRANSLATION_ID, -1);
            if (id > 0) {
                mPresenter.loadTranslation(id);
            }
        }
    }

    private void initPresenter() {
        TranslateDataSource translateDataSource = DataSourceProvider.provideTranslate(getContext());
        CommonDataSource commonDataSource = DataSourceProvider.provideCommon(getContext());
        CacheDataSource cacheDataSource = DataSourceProvider.provideCache(getContext());
        mPresenter = new TranslatePresenter(this,
                translateDataSource, commonDataSource, cacheDataSource);
        mPresenter.setSystemLang(Locale.getDefault().getLanguage());
    }

    private void findViews(View rootView) {
        mText = (RxEditText) rootView.findViewById(R.id.text);
        mSwapButton = (ImageView) rootView.findViewById(R.id.swap);
        mClearButton = (ImageView) rootView.findViewById(R.id.clear);
        mTranslationTextView = (TextView) rootView.findViewById(R.id.translation);
        mLanguageA = (TextView) rootView.findViewById(R.id.language_a);
        mLanguageB = (TextView) rootView.findViewById(R.id.language_b);
        mSpeechTextView = (TextView) rootView.findViewById(R.id.speech);
        mTranslatedSpeechTextView = (TextView) rootView.findViewById(R.id.speech_translated);
        mFavoritesButton = (ImageView) rootView.findViewById(R.id.bookmark);
        mHistoryRecyclerView = (RecyclerView) rootView.findViewById(R.id.history);
        mNestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nested_scroll);
        mTranslationCard = (CardView) rootView.findViewById(R.id.translation_card);
        mHistoryCard = (CardView) rootView.findViewById(R.id.history_card);
    }

    private void initViews() {
        mSwapButton.setOnClickListener(this);
        mClearButton.setOnClickListener(this);
        mText.setOnRxTextChangeListener(text -> mPresenter.translateText(text), 1000);
        mLanguageA.setOnClickListener(this);
        mLanguageB.setOnClickListener(this);
        mFavoritesButton.setOnClickListener(this);
        initHistory();
    }

    private void initHistory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        adapter = new BookmarksAdapter(new ArrayList<>());
        adapter.setOnItemClickListener(new BookmarksAdapter.OnItemClickListener() {
            @Override
            public void onClick(Translation translation) {
                Navigator.showTranslate(getActivity(), 0, translation.getId());
            }

            @Override
            public void addToBookmarks(Translation translation) {
                translation.setBookmark(!translation.getBookmark());
                DataSourceProvider.provideCache(getContext()).updateTranslation(translation)
                        .compose(RxSchedulers.async())
                        .subscribe();
            }
        });
        mHistoryRecyclerView.setLayoutManager(layoutManager);
        mHistoryRecyclerView.setItemAnimator(animator);
        mHistoryRecyclerView.setAdapter(adapter);
        mHistoryRecyclerView.setNestedScrollingEnabled(false);
        mHistoryRecyclerView.setHasFixedSize(false);
        mNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    mPresenter.loadHistory();
                }
            }
        });
        mPresenter.loadHistory();
    }

    @Override
    public void showHistory(List<Translation> translations) {
        adapter.addData(translations);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear:
                mText.setText("");
                mTranslationTextView.setText("");
                ObjectAnimator hide = Animations.makeHide(mTranslationCard, -mTranslationCard.getHeight() -
                        getResources().getDimensionPixelSize(R.dimen.margin_medium));
                ObjectAnimator show = Animations.makeShow(mHistoryCard, 0);
                show.addUpdateListener(valueAnimator -> mNestedScrollView.scrollTo(0, 0));
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(hide, show);
                animatorSet.start();
                break;
            case R.id.swap:
                changeLanguage();
                break;
            case R.id.language_a:
                showLanguageSelection(1);
                break;
            case R.id.language_b:
                showLanguageSelection(2);
                break;
            case R.id.bookmark:
                String text = mText.getText().toString();
                String translation = mTranslationTextView.getText().toString();
                mPresenter.addToFavorites(text, translation);
                break;
        }
    }

    public void showLangs(String langA, String langB) {
        mLanguageA.setText(langA);
        mLanguageB.setText(langB);
        mSpeechTextView.setText(langA);
        mTranslatedSpeechTextView.setText(langB);
    }

    @Override
    public void showText(@NonNull String text) {
        mText.setTextWithoutNotifyinng(text);
    }

    private void showLanguageSelection(int lang) {
        LangsActivity.startActivityForResult(this, lang, 5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.loadLangs(Locale.getDefault().getLanguage());
    }

    private void changeLanguage() {
        mPresenter.swapLanguages();
        mText.setText(mTranslationTextView.getText());
    }

    @Override
    public void showTranslation(@NonNull String text) {
        mTranslationTextView.setText(text);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        ObjectAnimator show = Animations.makeShow(mTranslationCard, 0);
        ObjectAnimator hide = Animations.makeHide(mHistoryCard, height);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(hide, show);
        animatorSet.start();
    }

    @Override
    public void addHistoryItemToTop(Translation translation) {
        adapter.addDataOnTop(translation);
    }
}
