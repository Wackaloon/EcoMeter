package com.wackalooon.meter.data.storage

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.wackalooon.meter.data.model.MeterDatabaseEntity

@Dao
interface MeterDao {

    @WorkerThread
    @Query("SELECT * FROM meterdatabaseentity")
    fun getAll(): List<MeterDatabaseEntity>

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(meter: MeterDatabaseEntity)

    @WorkerThread
    @Query("DELETE FROM meterdatabaseentity WHERE id = :meterId")
    fun delete(meterId: Long)

    @WorkerThread
    @Update
    fun update(meter: MeterDatabaseEntity)
}
