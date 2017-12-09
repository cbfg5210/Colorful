package com.ue.colorful.model

import android.os.Parcel
import android.os.Parcelable

class PaletteColorSection(val colorSectionName: String, val colorSectionValue: Int,
                          val darkColorSectionsValue: Int, val paletteColorList: List<PaletteColor>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readInt(),
            ArrayList<PaletteColor>().apply { source.readList(this, PaletteColor::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(colorSectionName)
        writeInt(colorSectionValue)
        writeInt(darkColorSectionsValue)
        writeList(paletteColorList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PaletteColorSection> = object : Parcelable.Creator<PaletteColorSection> {
            override fun createFromParcel(source: Parcel): PaletteColorSection = PaletteColorSection(source)
            override fun newArray(size: Int): Array<PaletteColorSection?> = arrayOfNulls(size)
        }
    }
}
