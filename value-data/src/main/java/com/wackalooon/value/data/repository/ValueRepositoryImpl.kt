package com.wackalooon.value.data.repository

import com.wackalooon.value.data.storage.ValueDao
import com.wackalooon.value.data.storage.ValueDatabaseEntity
import com.wackalooon.value.domain.model.Value
import com.wackalooon.value.domain.repository.ValueRepository


class ValueRepositoryImpl(private val valueDao: ValueDao) : ValueRepository {
    override fun getValuesForMeterId(meterId: Long): List<Value> {
        return valueDao.getAll(meterId).map { it.toValue() }
    }

    override fun addValueForMeterId(value: Value, meterId: Long) {
        valueDao.insert(value.toValueDaoEntity(meterId))
    }

    override fun updateValue(valueId: Long, newValue: Double) {
        valueDao.update(valueId, newValue)
    }

    override fun deleteValue(valueId: Long) {
        valueDao.delete(valueId)
    }

    private fun ValueDatabaseEntity.toValue(): Value {
        return Value(id, creationTime, value)
    }

    private fun Value.toValueDaoEntity(meterId: Long): ValueDatabaseEntity {
        return ValueDatabaseEntity(id, creationTime, meterId, value)
    }
}
