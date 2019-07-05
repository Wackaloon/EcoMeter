package com.wackalooon.meter.domain.model

import java.io.Serializable

sealed class Type : Serializable {
    data class Water(val waterType: WaterType) : Type()
    object Gas : Type()
    object Electricity : Type()

    override fun toString(): String {
        return this.toString()
    }

    companion object {
        fun fromString(type: String): Type {
            val value = Type::class.sealedSubclasses.find {
                it.toString() == type
            }?.objectInstance
            requireNotNull(value) { "Cannot deserialize type $type" }
            return value
        }
    }
}
