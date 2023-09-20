package com.aait.getak.network.local_db
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aait.getak.models.countries_model.Country


@Database(entities = [Country::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase(){
    abstract fun appDao(): AppDao
    companion object {
        private val LOG_TAG = AppDB::class.java.simpleName
        private var sInstance: AppDB? = null

        fun getInstance(context: Context): AppDB {
            if (sInstance == null) {
                Log.e(LOG_TAG, "db created")
                sInstance = Room.databaseBuilder(context.applicationContext,
                    AppDB::class.java, "app_DB")
                    .build()
            }
            return sInstance as AppDB
        }
    }
}
