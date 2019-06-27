package com.wackalooon.value.data.storage

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ValueDao {

    @WorkerThread
    @Query("SELECT * FROM valuedatabaseentity WHERE meterId = :meterId")
    fun getAll(meterId: Long): List<ValueDatabaseEntity>

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: ValueDatabaseEntity)

    @WorkerThread
    @Query("DELETE FROM valuedatabaseentity WHERE id = :valueId")
    fun delete(valueId: Long)

    @WorkerThread
    @Query("UPDATE valuedatabaseentity SET value = :newValue WHERE id = :valueId")
    fun update(valueId: Long, newValue: Double)
}
