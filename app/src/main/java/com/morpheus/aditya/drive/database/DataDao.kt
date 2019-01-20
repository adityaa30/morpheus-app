package com.morpheus.aditya.drive.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface DataDao{
    @Insert
    fun addData(data: Data) : Single<Long>

    @Query("SELECT * FROM table_data ORDER BY time DESC LIMIT 1")
    fun getLastData(): Observable<Data>
}