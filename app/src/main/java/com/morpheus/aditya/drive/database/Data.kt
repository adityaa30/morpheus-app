package com.morpheus.aditya.drive.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "table_data")
@Parcelize
data class Data(
    @ColumnInfo(name = "EAR")
    val ear: Double,

    @ColumnInfo(name = "alarm")
    val alarm: Boolean,

    @ColumnInfo(name = "time")
    val time: Long
) : Parcelable {

    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

}