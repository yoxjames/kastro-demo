/*
 * Copyright 2024 James Yox
 *
 * This file is part of kastro-demo.
 *
 * Kastro-demo is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Kastro-demo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with kastro-demo. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package dev.jamesyox.kastro.demo

import dev.jamesyox.kastro.demo.clock.CelestialClockState
import dev.jamesyox.kastro.demo.input.InputPanelEvent
import dev.jamesyox.kastro.demo.luna.prettyString
import dev.jamesyox.kastro.demo.misc.CelestialParametersUpdate
import dev.jamesyox.kastro.demo.misc.asPair
import dev.jamesyox.kastro.demo.sol.prettyString
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.kastro.demo.svgk.celestialClockFocusStateMachine
import dev.jamesyox.kastro.demo.svgk.celestialClockSelectStateMachine
import dev.jamesyox.kastro.demo.svgk.eventsViewEventsFocusStateMachine
import dev.jamesyox.kastro.demo.svgk.eventsViewEventsSelectStateMachine
import dev.jamesyox.kastro.demo.svgk.statesViewEventsFocusStateMachine
import dev.jamesyox.kastro.demo.svgk.statesViewEventsSelectStateMachine
import dev.jamesyox.kastro.demo.view.ListViewEvent
import dev.jamesyox.kastro.demo.view.TableType
import dev.jamesyox.kastro.demo.view.TopPanelEvent
import dev.jamesyox.kastro.demo.view.TopPanelState
import dev.jamesyox.kastro.luna.calculateLunarState
import dev.jamesyox.kastro.sol.calculateSolarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.datetime.toDeprecatedInstant
import kotlinx.datetime.toStdlibInstant
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class GlobalState(
    private val clock: Clock,
    startingParams: CelestialParameters,
    appScope: CoroutineScope
) {
    private val currentTimeState = flow {
        while (true) {
            emit(clock.now())
            delay(1.minutes)
        }
    }

    val kastroDemoEvents = MutableSharedFlow<KastroDemoEvent>()

    val geolocationRequests = kastroDemoEvents
        .filterIsInstance<KastroDemoEvent.TopPanel>()
        .map { it.event }
        .filterIsInstance<TopPanelEvent.InputPanel>()
        .map { it.event }
        .filterIsInstance<InputPanelEvent.GeolocationRequest>()

    private val manualParamUpdates = kastroDemoEvents
        .filterIsInstance<KastroDemoEvent.TopPanel>()
        .map { it.event }
        .filterIsInstance<TopPanelEvent.InputPanel>()
        .map { it.event }
        .filterIsInstance<InputPanelEvent.ManualEvent>()
        .debounce(250.milliseconds)

    private val geolocationUpdates = kastroDemoEvents
        .filterIsInstance<KastroDemoEvent.GeolocationEvent.GeolocationSuccess>()
        .map { it.location }

    private val manualLocationUpdates = manualParamUpdates
        .scan(startingParams.location) { location, update ->
            when (update) {
                is InputPanelEvent.DateChange -> location
                is InputPanelEvent.LatitudeUpdate -> location.copy(latitude = update.latitude)
                is InputPanelEvent.LongitudeUpdate -> location.copy(longitude = update.longitude)
                is InputPanelEvent.HeightChange -> location
            }
        }.distinctUntilChanged()

    val nonMapUpdates = merge(manualLocationUpdates, geolocationUpdates)

    private val mapMovements = kastroDemoEvents
        .filterIsInstance<KastroDemoEvent.MapMovement>()
        .map { it.location }

    val nonManualUpdates = merge(mapMovements, geolocationUpdates)

    val paramsState = merge(
        merge(manualLocationUpdates, mapMovements, geolocationUpdates)
            .map { CelestialParametersUpdate.LocationUpdate(it) },
        manualParamUpdates.filterIsInstance<InputPanelEvent.DateChange>()
            .map { CelestialParametersUpdate.DateUpdate(it.date) },
        manualParamUpdates.filterIsInstance<InputPanelEvent.HeightChange>()
            .map { CelestialParametersUpdate.HeightUpdate(it.height) }
    ).scan(startingParams) { params, update ->
        when (update) {
            is CelestialParametersUpdate.DateUpdate -> params.copy(date = update.date)
            is CelestialParametersUpdate.LocationUpdate -> params.copy(location = update.location)
            is CelestialParametersUpdate.HeightUpdate -> params.copy(height = update.height)
        }
    }.stateIn(
        scope = appScope,
        started = SharingStarted.Eagerly,
        initialValue = startingParams
    )

    private val listViewEvents = kastroDemoEvents
        .filterIsInstance<KastroDemoEvent.List>()
        .map { it.event }

    val tabState = listViewEvents
        .filterIsInstance<ListViewEvent.SelectTab>()
        .map { it.tab }
        .stateIn(appScope, SharingStarted.Eagerly, TableType.States)

    val selectedItem = kastroDemoEvents
        .scan(null) { state: Selected?, kastroDemoEvent: KastroDemoEvent ->
            when (kastroDemoEvent) {
                is KastroDemoEvent.TopPanel -> when (kastroDemoEvent.event) {
                    is TopPanelEvent.InputPanel -> state
                    is TopPanelEvent.ClockEvent -> celestialClockSelectStateMachine(state, kastroDemoEvent.event.event)
                    TopPanelEvent.SettingsClicked -> state
                }
                is KastroDemoEvent.List -> when (kastroDemoEvent.event) {
                    is ListViewEvent.EventsView -> eventsViewEventsSelectStateMachine(
                        state,
                        kastroDemoEvent.event.event
                    )
                    is ListViewEvent.StatesView -> statesViewEventsSelectStateMachine(
                        state,
                        kastroDemoEvent.event.event
                    )
                    is ListViewEvent.SelectTab -> state
                }
                is KastroDemoEvent.DetailView -> null
                is KastroDemoEvent.MapMovement -> state
                is KastroDemoEvent.GeolocationEvent -> state
            }
        }.stateIn(appScope, SharingStarted.Eagerly, null)

    val focusedItem = kastroDemoEvents
        .scan(null) { state: Selected?, kastroDemoEvent: KastroDemoEvent ->
            when (kastroDemoEvent) {
                is KastroDemoEvent.TopPanel -> when (kastroDemoEvent.event) {
                    is TopPanelEvent.InputPanel -> state
                    is TopPanelEvent.ClockEvent -> celestialClockFocusStateMachine(state, kastroDemoEvent.event.event)
                    TopPanelEvent.SettingsClicked -> state
                }
                is KastroDemoEvent.List -> when (kastroDemoEvent.event) {
                    is ListViewEvent.EventsView -> eventsViewEventsFocusStateMachine(kastroDemoEvent.event.event)
                    is ListViewEvent.StatesView -> statesViewEventsFocusStateMachine(kastroDemoEvent.event.event)
                    is ListViewEvent.SelectTab -> state
                }
                is KastroDemoEvent.DetailView -> null
                is KastroDemoEvent.MapMovement -> state
                is KastroDemoEvent.GeolocationEvent -> state
            }
        }.combine(selectedItem) { focused, selected -> selected ?: focused }
        .stateIn(appScope, SharingStarted.Eagerly, null)

    val topPanelState = kastroDemoEvents
        .filterIsInstance<KastroDemoEvent.TopPanel>()
        .map { it.event }
        .scan(TopPanelState.Clock) { acc, topPanelEvent ->
            when (topPanelEvent) {
                is TopPanelEvent.SettingsClicked -> when (acc) {
                    TopPanelState.Clock -> TopPanelState.Settings
                    TopPanelState.Settings -> TopPanelState.Clock
                }

                else -> acc
            }
        }

    val lunarState = currentTimeState
        .combine(paramsState) { time, params ->
            time.toDeprecatedInstant().calculateLunarState(params.location.asPair())
        }
        .stateIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            initialValue = clock.now()
                .toDeprecatedInstant()
                .calculateLunarState(startingParams.location.asPair())
        )

    val solarState = currentTimeState
        .combine(paramsState) { time, params ->
            time.toDeprecatedInstant().calculateSolarState(params.location.asPair())
        }
        .stateIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            initialValue = clock.now()
                .toDeprecatedInstant().calculateSolarState(startingParams.location.asPair())
        )

    private val nextSolarHorizonEvent = paramsState.transformLatest {
        val iter = nextSolarHorizonEvent(parameters = it, time = clock.now())
        while (true) {
            val event = iter.next()
            emit(event)
            delay(event.time.toStdlibInstant() - clock.now())
        }
    }

    private val nextLunarHorizonEvent = paramsState.transformLatest {
        val iter = nextLunarHorizonEvent(parameters = it, time = clock.now())
        while (true) {
            val event = iter.next()
            emit(event)
            delay(event.time.toStdlibInstant() - clock.now())
        }
    }

    private fun Flow<Pair<String, Instant>>.toCountdown() = transformLatest {
        while (true) {
            val trueDuration = (it.second - clock.now()).inWholeSeconds.seconds
            val duration = if (trueDuration <= 1.hours) {
                trueDuration.inWholeSeconds.seconds
            } else {
                trueDuration.inWholeMinutes.minutes
            }
            emit("${it.first} in $duration")
            if (duration <= 1.hours) delay(1.seconds) else delay(1.minutes)
        }
    }

    val solarCountDownContent = nextSolarHorizonEvent
        .map { Pair(it.prettyString, it.time.toStdlibInstant()) }
        .toCountdown()
    val lunarCountDownContent = nextLunarHorizonEvent
        .map { Pair(it.prettyString, it.time.toStdlibInstant()) }
        .toCountdown()

    val celestialClockState = combine(paramsState, currentTimeState) { params, time ->
        CelestialClockState(celestialParameters = params, time = time)
    }
}
