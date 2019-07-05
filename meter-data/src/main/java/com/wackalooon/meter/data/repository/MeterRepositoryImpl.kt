package com.wackalooon.meter.data.repository

import com.wackalooon.meter.data.model.MeterDatabaseEntity
import com.wackalooon.meter.data.storage.MeterDao
import com.wackalooon.meter.domain.model.Meter
import com.wackalooon.meter.domain.model.Type
import com.wackalooon.meter.domain.model.WaterType
import com.wackalooon.meter.domain.repository.MeterRepository

class MeterRepositoryImpl(private val meterDao: MeterDao) : MeterRepository {
    override fun getListOfMeters(): List<Meter> {
        return listOf(
                Meter(0, "Kitchen", "Kitchen", Type.Water(WaterType.HOT)),
                Meter(0, "Bathroom","Bathroom", Type.Water(WaterType.COLD)),
                Meter(0, "Kitchen","Kitchen", Type.Gas),
                Meter(0, "Electric shield","Electric shield", Type.Electricity)
        )
        //return meterDao.getAll().map { it.toMeter() }
    }

    override fun addMeter(name: String, type: Type) {
        meterDao.insert(MeterDatabaseEntity(id = 0, name = name, type = type))
    }

    override fun updateMeter(meter: Meter) {
        meterDao.update(meter.toMeterDaoEntity())
    }

    override fun deleteMeter(meterId: Long) {
        meterDao.delete(meterId)
    }

    private fun MeterDatabaseEntity.toMeter(): Meter {
        return Meter(id, name, type.toString(), type)
    }

    private fun Meter.toMeterDaoEntity(): MeterDatabaseEntity {
        return MeterDatabaseEntity(id, name, type)
    }
}
