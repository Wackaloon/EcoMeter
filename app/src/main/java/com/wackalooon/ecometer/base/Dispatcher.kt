package com.wackalooon.ecometer.base

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

interface Dispatcher<E : Event, U : Update> {
    @WorkerThread
    fun dispatchEvent(event: E): Flow<U>
}
