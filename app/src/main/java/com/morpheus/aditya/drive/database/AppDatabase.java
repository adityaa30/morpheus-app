package com.morpheus.aditya.drive.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.morpheus.aditya.drive.DriveUtils;

@Database(entities = {Data.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DataDao dataDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DriveUtils.DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
