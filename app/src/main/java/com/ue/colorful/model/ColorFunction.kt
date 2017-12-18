package com.ue.colorful.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by hawk on 2017/12/14.
 */
class ColorFunction(val funName: String, val funFlag: Int) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(funName)
        writeInt(funFlag)
    }

    companion object {
        @JvmField//这个注解不能删
        val CREATOR: Parcelable.Creator<ColorFunction> = object : Parcelable.Creator<ColorFunction> {
            override fun createFromParcel(source: Parcel): ColorFunction = ColorFunction(source)
            override fun newArray(size: Int): Array<ColorFunction?> = arrayOfNulls(size)
        }
    }
}