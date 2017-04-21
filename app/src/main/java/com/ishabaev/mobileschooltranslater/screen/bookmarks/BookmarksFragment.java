package com.ishabaev.mobileschooltranslater.screen.bookmarks;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishabaev.mobileschooltranslater.R;
import com.ishabaev.mobileschooltranslater.datasource.CacheDataSource;
import com.ishabaev.mobileschooltranslater.datasource.DataSourceProvider;
import com.ishabaev.mobileschooltranslater.datasource.Translation;
import com.ishabaev.mobileschooltranslater.screen.Navigator;
import com.ishabaev.mobileschooltranslater.screen.view.RxEditText;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment implements BookmarksView {

    public static String TAG = "bookmarks_fragment";
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private RxEditText mSearchEditText;
    private RecyclerView mBookmarksRecyclerView;
    private BookmarksAdapter mAdapter;
    private BookmarksPresenter mPresenter;
    private boolean loading = true;

    public static Fragment newInstance() {
        return new BookmarksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        initPresenter();
        findViews(rootView);
        initViews();
        mPresenter.loadBookmarks();
        return rootView;
    }

    private void findViews(@NonNull View rootView) {
        mSearchEditText = (RxEditText) rootView.findViewById(R.id.search);
        mBookmarksRecyclerView = (RecyclerView) rootView.findViewById(R.id.bookmarks);
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        mAdapter = new BookmarksAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener(new BookmarksAdapter.OnItemClickListener() {
            @Override
            public void onClick(Translation translation) {
                Navigator.showTranslate(getActivity(), 0, translation.getId());
            }

            @Override
            public void addToBookmarks(Translation translation) {
                mPresenter.addToFavourites(translation);
            }
        });
        mBookmarksRecyclerView.setLayoutManager(layoutManager);
        mBookmarksRecyclerView.setItemAnimator(animator);
        mBookmarksRecyclerView.setAdapter(mAdapter);

        mSearchEditText.setOnRxTextChangeListener(text -> mPresenter.searchBookmark(text), 500);

        mBookmarksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false;
                            mPresenter.loadBookmarks(mSearchEditText.getText().toString());
                        }
                    }
                }
            }
        });
    }

    private void initPresenter() {
        CacheDataSource cacheDataSource = DataSourceProvider.provideCache(getContext());
        mPresenter = new BookmarksPresenter(this, cacheDataSource);
    }

    @Override
    public void addBookmarks(List<Translation> translations) {
        mAdapter.addData(translations);
    }

    @Override
    public void showBookmarks(List<Translation> translations) {
        mAdapter.changeDataSet(translations);
    }

    @Override
    public void removeBookmark(Translation translation) {
        mAdapter.removeItem(translation);
    }
}
