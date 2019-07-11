package com.morpheus.aditya.drive.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.morpheus.aditya.drive.database.AppDatabase
import com.morpheus.aditya.drive.database.Data

class DataViewModel(application: Application): AndroidViewModel(application) {
    private val mDatabase: AppDatabase = AppDatabase.getDatabase(application)
//    private var mData: LiveData<Data>

    init {
//        mData = mDatabase.dataDao().getLastData()
    }

//    fun getData(): LiveData<Data> {
//        return mData
//    }
}