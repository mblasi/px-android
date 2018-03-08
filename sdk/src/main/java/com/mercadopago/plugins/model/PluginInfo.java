package com.mercadopago.plugins.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class PluginInfo {


    public final String id;
    public final String name;
    public final String description;
    public final @DrawableRes
    int icon;

    public PluginInfo(@NonNull final String id,
                      @NonNull final String name,
                      @DrawableRes final int icon,
                      @NonNull final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public PluginInfo(@NonNull final String id,
                      @NonNull final String name,
                      @DrawableRes final int icon) {

        this.id = id;
        this.name = name;
        this.description = null;
        this.icon = icon;
    }
}