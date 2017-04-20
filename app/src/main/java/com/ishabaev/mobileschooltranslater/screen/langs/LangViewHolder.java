package com.ishabaev.mobileschooltranslater.screen.langs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ishabaev.mobileschooltranslater.R;

import java.util.Map;

public class LangViewHolder extends RecyclerView.ViewHolder {

    View rootView;
    TextView lang;

    public LangViewHolder(@NonNull View view) {
        super(view);
        rootView = view;
        lang = (TextView) view.findViewById(R.id.lang);
    }

    public void bind(@NonNull Map.Entry<String, String> lang) {
        rootView.setTag(lang);
        this.lang.setText(lang.getValue());
    }
}
