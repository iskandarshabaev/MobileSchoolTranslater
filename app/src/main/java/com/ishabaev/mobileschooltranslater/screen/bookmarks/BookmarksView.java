package com.ishabaev.mobileschooltranslater.screen.bookmarks;

import com.ishabaev.mobileschooltranslater.datasource.Translation;

import java.util.List;

public interface BookmarksView {

    void showBookmarks(List<Translation> translations);

    void addBookmarks(List<Translation> translations);

    void removeBookmark(Translation translation);

}
