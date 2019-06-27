package com.wackalooon.meter.data.storage

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_VERSION = 1

@Database(
    entities = [
        MeterDatabaseEntity::class
    ], version = DATABASE_VERSION
)
abstract class MeterDatabase : RoomDatabase() {

    abstract fun meterDao(): MeterDao

    companion object {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        val DATABASE_NAME = "meter-database"

        @Volatile
        private var instance: MeterDatabase? = null

        fun get(context: Context): MeterDatabase {
            var db = instance
            if (db == null) {
                synchronized(MeterDatabase::class) {
                    db = instance
                    if (db == null) {
                        db = createDatabase(context)
                        instance = db
                    }
                }
            }
            return requireNotNull(db)
        }

        private fun createDatabase(context: Context): MeterDatabase {
            return Room.databaseBuilder(
                context,
                MeterDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
