package com.jmartinal.madridbusgps.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by Jorge on 15/09/2015.
 * This class represents an option in the Navigation Drawer menu
 */
public class NavigationDrawerListSection {
    private String name;
    private Drawable icon;

    public NavigationDrawerListSection() {
    }

    public NavigationDrawerListSection(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}

