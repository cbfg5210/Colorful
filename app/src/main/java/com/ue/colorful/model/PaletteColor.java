package com.ue.colorful.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaletteColor implements Parcelable {

    private final int hex;
    private final String hexString;
    private final String baseName;
    private final String colorSectionName;

    public PaletteColor(String colorSectionName, final int hex, final String baseName) {
        this.colorSectionName = colorSectionName;
        this.hex = hex;
        this.hexString = intToStringHex(this.hex);
        this.baseName = baseName;
    }

    public static String intToStringHex(int hex) {
        return String.format("#%06x", 0xFFFFFF & hex);
    }

    public int getHex() {
        return hex;
    }

    public String getHexString() {
        return hexString;
    }

    public String getBaseName() {
        return baseName;
    }

    public String getColorSectionName() {
        return colorSectionName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(colorSectionName);
        out.writeInt(hex);
        out.writeString(baseName);
    }

    public static final Parcelable.Creator<PaletteColor> CREATOR
            = new Parcelable.Creator<PaletteColor>() {
        public PaletteColor createFromParcel(Parcel in) {
            final String colorSectionName = in.readString();
            final int hex = in.readInt();
            final String baseName = in.readString();
            return new PaletteColor(colorSectionName, hex, baseName);
        }

        public PaletteColor[] newArray(int size) {
            return new PaletteColor[size];
        }
    };
}
