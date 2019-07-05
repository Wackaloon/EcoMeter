package com.wackalooon.ecometer.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<E : Event, U : Update, S : State>(
    dispatcher: Dispatcher<E, U>,
    initialState: S
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    private val events = Channel<E>(Channel.CONFLATED)
    private val updateFlows = Channel<Flow<U>>(Channel.UNLIMITED)
    private val states = ConflatedBroadcastChannel(initialState)

    val stateChannel: ReceiveChannel<S> get() = states.openSubscription()
    val currentState get() = states.value
    fun offerEvent(event: E) {
        events.offer(event)
    }

    init {
        launch {
            events.consumeEach { event ->
                updateFlows.send(dispatcher.dispatchEvent(event))
            }
        }

        launch {
            updateFlows.consumeEach { updates ->
                updates.collect { update ->
                    states.send(updateState(update))
                }
            }
        }
    }

    protected abstract fun updateState(update: U): S

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }
}
