package com.ishabaev.mobileschooltranslater.screen.bookmarks;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishabaev.mobileschooltranslater.R;
import com.ishabaev.mobileschooltranslater.datasource.Translation;

public class BookmarksViewHolder extends RecyclerView.ViewHolder {

    View view;
    ImageView bookmarkButton;
    private TextView mTextTextView;
    private TextView mTranslationTextView;
    private TextView mUITextView;

    public BookmarksViewHolder(@NonNull View view) {
        super(view);
        this.view = view;
        findViews(view);
    }

    private void findViews(@NonNull View view) {
        bookmarkButton = (ImageView) view.findViewById(R.id.bookmark);
        mTextTextView = (TextView) view.findViewById(R.id.text);
        mTranslationTextView = (TextView) view.findViewById(R.id.translation);
        mUITextView = (TextView) view.findViewById(R.id.ui);
    }

    public void bind(Translation translation) {
        mTextTextView.setText(translation.getText().replace("\n", " "));
        mTranslationTextView.setText(translation.getTranslation().replace("\n", " "));
        mUITextView.setText(translation.getUi());
        if (translation.getBookmark()) {
            bookmarkButton.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorAccent));
        } else {
            bookmarkButton.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.colorDividers));
        }
    }

}
