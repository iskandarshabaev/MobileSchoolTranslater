package com.ishabaev.mobileschooltranslater.datasource;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

@Entity(indexes = {
        @Index(value = "text, translation, ui", unique = true)
})
public class Translation {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String text;

    @NotNull
    private String translation;

    @NotNull
    private String ui;

    @NotNull
    private Boolean bookmark;

    @NonNull
    private Date date;

    @Generated(hash = 870351887)
    public Translation(Long id, @NotNull String text, @NotNull String translation,
            @NotNull String ui, @NotNull Boolean bookmark, @NotNull Date date) {
        this.id = id;
        this.text = text;
        this.translation = translation;
        this.ui = ui;
        this.bookmark = bookmark;
        this.date = date;
    }

    @Generated(hash = 321689573)
    public Translation() {
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslation() {
        return this.translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getUi() {
        return this.ui;
    }

    public void setUi(String ui) {
        this.ui = ui;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getBookmark() {
        return this.bookmark;
    }

    public void setBookmark(Boolean bookmark) {
        this.bookmark = bookmark;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
