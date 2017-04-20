package com.ishabaev.mobileschooltranslater.screen.langs;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.LinkedTreeMap;
import com.ishabaev.mobileschooltranslater.R;

import java.util.List;
import java.util.Map;

public class LangsAdapter extends RecyclerView.Adapter<LangViewHolder> implements View.OnClickListener {

    private OnLangSelected mOnLangSelected;
    private List<Map.Entry<String, String>> mLangs;

    public LangsAdapter(List<Map.Entry<String, String>> langs) {
        mLangs = langs;
    }

    public void changeDataset(List<Map.Entry<String, String>> langs) {
        mLangs.clear();
        mLangs.addAll(langs);
        notifyDataSetChanged();
    }

    @Override
    public LangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_lang, parent, false);
        return new LangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LangViewHolder holder, int position) {
        holder.bind(mLangs.get(position));
        holder.rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mOnLangSelected != null) {
            Map.Entry<String, String> lang = (Map.Entry<String, String>) view.getTag();
            mOnLangSelected.onSelected(lang);
        }
    }

    @Override
    public int getItemCount() {
        return mLangs.size();
    }

    public void setOnLangSelected(OnLangSelected onLangSelected) {
        mOnLangSelected = onLangSelected;
    }

    public interface OnLangSelected {
        void onSelected(LinkedTreeMap.Entry<String, String> lang);
    }
}
