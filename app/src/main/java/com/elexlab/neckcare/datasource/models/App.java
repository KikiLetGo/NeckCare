package com.elexlab.neckcare.datasource.models;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

public class App {
    @PrimaryKey(AssignType.BY_MYSELF)
    private String packageName;
    private String name;
    @Ignore
    private Drawable icon;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null || !(obj instanceof App)){
            return super.equals(obj);
        }
        App other = (App) obj;
        return packageName.equals(other.getPackageName());
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
