package com.wackalooon.value.data.storage

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_VERSION = 1

@Database(
    entities = [
        ValueDatabaseEntity::class
    ], version = DATABASE_VERSION
)
abstract class ValueDatabase : RoomDatabase() {

    abstract fun valueDao(): ValueDao

    companion object {

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        val DATABASE_NAME = "value-database"

        @Volatile
        private var instance: ValueDatabase? = null

        fun get(context: Context): ValueDatabase {
            var db = instance
            if (db == null) {
                synchronized(ValueDatabase::class) {
                    db = instance
                    if (db == null) {
                        db = createDatabase(context)
                        instance = db
                    }
                }
            }
            return requireNotNull(db)
        }

        private fun createDatabase(context: Context): ValueDatabase {
            return Room.databaseBuilder(
                context,
                ValueDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
