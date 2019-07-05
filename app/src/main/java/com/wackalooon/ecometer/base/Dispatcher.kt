package com.wackalooon.ecometer.base

import kotlinx.coroutines.flow.Flow

interface Dispatcher<E : Event, U : Update> {
    fun dispatchEvent(event: E): Flow<U>
}
