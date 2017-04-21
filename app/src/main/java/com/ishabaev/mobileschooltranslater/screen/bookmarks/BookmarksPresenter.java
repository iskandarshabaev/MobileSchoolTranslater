package com.ishabaev.mobileschooltranslater.screen.bookmarks;

import android.support.annotation.NonNull;

import com.ishabaev.mobileschooltranslater.datasource.CacheDataSource;
import com.ishabaev.mobileschooltranslater.datasource.Translation;
import com.ishabaev.mobileschooltranslater.screen.BasePresenter;
import com.ishabaev.mobileschooltranslater.util.RxSchedulers;

import rx.subscriptions.CompositeSubscription;

public class BookmarksPresenter extends BasePresenter {

    private static final int CHUNK = 15;

    private CompositeSubscription mCompositeSubscription;
    private BookmarksView mView;
    private CacheDataSource mCacheDataSource;
    private int mOffset = 0;

    public BookmarksPresenter(@NonNull BookmarksView view,
                              @NonNull CacheDataSource cacheDataSource) {
        mView = view;
        mCacheDataSource = cacheDataSource;
    }

    public void loadBookmarks() {
        mCacheDataSource.getBookmarks(CHUNK, mOffset)
                .doOnNext(r -> mOffset += CHUNK)
                .compose(RxSchedulers.async())
                .subscribe(mView::addBookmarks, this::showError);
    }

    public void addToFavourites(Translation translation) {
        translation.setBookmark(!translation.getBookmark());
        mCacheDataSource.updateTranslation(translation)
                .compose(RxSchedulers.async())
                .subscribe(mView::removeBookmark, this::showError);
    }

    public void searchBookmark(@NonNull String text) {
        mOffset = 0;
        mCacheDataSource.searchBookmark(text, CHUNK, mOffset)
                .compose(RxSchedulers.async())
                .subscribe(mView::showBookmarks, this::showError);
    }

    public void loadBookmarks(@NonNull String text) {
        mCacheDataSource.searchBookmark(text, CHUNK, mOffset)
                .compose(RxSchedulers.async())
                .subscribe(mView::addBookmarks, this::showError);
    }

    private void showError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void subscribe() {
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.unsubscribe();
    }
}
