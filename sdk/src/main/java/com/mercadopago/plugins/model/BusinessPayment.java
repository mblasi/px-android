package com.mercadopago.plugins.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class BusinessPayment implements PluginPayment, Parcelable {

    private final Status status;
    private final int iconId;
    private final String title;
    private final String subtitle;

    protected BusinessPayment(Parcel in) {
        iconId = in.readInt();
        title = in.readString();
        subtitle = in.readString();
        status = Status.fromString(in.readString());
    }

    private BusinessPayment(Builder builder) {
        this.title = builder.title;
        this.status = builder.status;
        this.iconId = builder.iconId;
        this.subtitle = builder.subtitle;
    }

    public enum Status {
        APPROVED("approved"),
        REJECTED("rejected"),
        IN_PROGRESS("in_progress"),
        PENDING("pending");

        private final String name;

        Status(final String name) {
            this.name = name;
        }

        public static Status fromString(String text) {
            for (Status b : Status.values()) {
                if (b.name.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            throw new IllegalStateException("Invalid status");
        }
    }


    public static class Builder {

        @NonNull
        private final Status status;
        @DrawableRes
        private final int iconId;
        @NonNull
        private final String title;
        @NonNull
        private final String subtitle;

        public Builder(@NonNull Status status,
                       @DrawableRes int iconId,
                       @NonNull String title,
                       @NonNull String subtitle) {
            this.title = title;
            this.status = status;
            this.iconId = iconId;
            this.subtitle = subtitle;
        }
    }

    @Override
    public void process(final Processor processor) {
        processor.process(this);
    }


    public static final Creator<BusinessPayment> CREATOR = new Creator<BusinessPayment>() {
        @Override
        public BusinessPayment createFromParcel(Parcel in) {
            return new BusinessPayment(in);
        }

        @Override
        public BusinessPayment[] newArray(int size) {
            return new BusinessPayment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(iconId);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(status.name);
    }

}