package com.ishabaev.mobileschooltranslater.screen.bookmarks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ishabaev.mobileschooltranslater.R;
import com.ishabaev.mobileschooltranslater.datasource.Translation;

import java.util.List;

import static com.ishabaev.mobileschooltranslater.util.ImageHelper.tintImage;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksViewHolder> implements View.OnClickListener {

    private List<Translation> mTranslations;
    private OnItemClickListener mOnItemClickListener;

    public BookmarksAdapter(@NonNull List<Translation> translations) {
        mTranslations = translations;
    }

    public void changeDataSet(List<Translation> translations) {
        mTranslations.clear();
        mTranslations.addAll(translations);
        notifyDataSetChanged();
    }

    public void addDataOnTop(@NonNull Translation translation) {
        mTranslations.add(0, translation);
        notifyItemInserted(0);
    }

    public void removeItem(@NonNull Translation translation) {
        int index = mTranslations.indexOf(translation);
        if (index >= 0) {
            mTranslations.remove(translation);
            notifyItemRangeRemoved(index, 1);
        }
    }

    public void addDataOnTop(@NonNull List<Translation> translations) {
        mTranslations.addAll(translations);
        notifyItemInserted(0);
    }

    public void addData(@NonNull List<Translation> translations) {
        mTranslations.addAll(translations);
        notifyItemInserted(mTranslations.size());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public BookmarksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_translation, parent, false);
        BookmarksViewHolder holder = new BookmarksViewHolder(view);
        holder.view.setOnClickListener(this);
        holder.bookmarkButton.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View view) {
        Translation translation = (Translation) view.getTag();
        if (mOnItemClickListener != null) {
            if (view.getId() == R.id.item) {
                mOnItemClickListener.onClick(translation);
            } else if (view.getId() == R.id.bookmark) {
                if (translation.getBookmark()) {
                    tintImage(((ImageView) view), R.color.colorDividers);
                } else {
                    tintImage(((ImageView) view), R.color.colorAccent);
                }
                mOnItemClickListener.addToBookmarks(translation);
            }
        }
    }

    @Override
    public void onBindViewHolder(BookmarksViewHolder holder, int position) {
        holder.bind(mTranslations.get(position));
        holder.view.setTag(mTranslations.get(position));
        holder.bookmarkButton.setTag(mTranslations.get(position));
    }

    @Override
    public int getItemCount() {
        return mTranslations.size();
    }

    public interface OnItemClickListener {

        void onClick(Translation translation);

        void addToBookmarks(Translation translation);
    }
}
