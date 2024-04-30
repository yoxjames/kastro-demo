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

package dev.jamesyox.kastro.demo.svgk

import dev.jamesyox.kastro.demo.clock.CelestialClockEvent
import dev.jamesyox.kastro.demo.luna.LunarHorizonState
import dev.jamesyox.kastro.demo.sol.SolarLightState
import dev.jamesyox.kastro.demo.sol.SolarPhaseState
import dev.jamesyox.kastro.demo.table.EventRowEvent
import dev.jamesyox.kastro.demo.table.LunarPhaseRowEvent
import dev.jamesyox.kastro.demo.table.SolarPhaseRowEvent
import dev.jamesyox.kastro.demo.view.EventsViewEvent
import dev.jamesyox.kastro.demo.view.StateViewEvent
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.sol.SolarEvent

sealed interface Selected {
    sealed interface SelectedState : Selected {
        value class Solar(
            val solarPhaseState: SolarPhaseState
        ) : SelectedState

        value class Lunar(
            val lunarPhaseState: LunarHorizonState
        ) : SelectedState

        value class Light(
            val solarLightState: SolarLightState
        ) : SelectedState
    }

    sealed interface SelectedEvent : Selected {
        value class Lunar(
            val lunarEvent: LunarEvent
        ) : SelectedEvent

        value class Solar(
            val solarEvent: SolarEvent
        ) : SelectedEvent
    }
}

fun celestialClockFocusStateMachine(state: Selected?, transition: CelestialClockEvent): Selected? = when (transition) {
    is CelestialClockEvent.LightEvent -> Selected.SelectedState.Light(transition.solarLightState)
    is CelestialClockEvent.LunarHorizonEvent -> Selected.SelectedState.Lunar(transition.lunarHorizonState)
    is CelestialClockEvent.SolarPhaseEvent -> Selected.SelectedState.Solar(transition.solarPhaseState)
    CelestialClockEvent.OnMouseLeave -> null
    CelestialClockEvent.OnSettingsClick -> state
}

fun celestialClockSelectStateMachine(
    state: Selected?,
    transition: CelestialClockEvent
): Selected? = when (transition) {
    is CelestialClockEvent.LightEvent.Select -> Selected.SelectedState.Light(transition.solarLightState)
    is CelestialClockEvent.LunarHorizonEvent.Select -> Selected.SelectedState.Lunar(transition.lunarHorizonState)
    is CelestialClockEvent.SolarPhaseEvent.Select -> Selected.SelectedState.Solar(transition.solarPhaseState)
    else -> state
}

fun eventsViewEventsFocusStateMachine(transition: EventsViewEvent): Selected.SelectedEvent? = when (transition) {
    is EventsViewEvent.EventRow -> when (transition.event) {
        is EventRowEvent.Lunar -> Selected.SelectedEvent.Lunar(transition.event.lunarEvent)
        is EventRowEvent.Solar -> Selected.SelectedEvent.Solar(transition.event.solarEvent)
    }
    EventsViewEvent.OnMouseLeave -> null
}

fun eventsViewEventsSelectStateMachine(
    state: Selected?,
    transition: EventsViewEvent
): Selected? = when (transition) {
    is EventsViewEvent.EventRow -> when (transition.event) {
        is EventRowEvent.Solar.Select -> Selected.SelectedEvent.Solar(transition.event.solarEvent)
        is EventRowEvent.Lunar.Select -> Selected.SelectedEvent.Lunar(transition.event.lunarEvent)
        is EventRowEvent.Lunar.Hover -> state
        is EventRowEvent.Solar.Hover -> state
    }
    EventsViewEvent.OnMouseLeave -> state
}

fun statesViewEventsFocusStateMachine(transition: StateViewEvent): Selected.SelectedState? = when (transition) {
    is StateViewEvent.Lunar -> Selected.SelectedState.Lunar(transition.event.lunarHorizonState)
    is StateViewEvent.Solar -> when (transition.event) {
        is SolarPhaseRowEvent.Light.Hover -> Selected.SelectedState.Light(transition.event.lightState)
        is SolarPhaseRowEvent.Light.Select -> Selected.SelectedState.Light(transition.event.lightState)
        is SolarPhaseRowEvent.Phase.Hover -> Selected.SelectedState.Solar(transition.event.phase)
        is SolarPhaseRowEvent.Phase.Select -> Selected.SelectedState.Solar(transition.event.phase)
    }
    StateViewEvent.OnMouseLeave -> null
}

fun statesViewEventsSelectStateMachine(
    state: Selected?,
    transition: StateViewEvent
): Selected? = when (transition) {
    is StateViewEvent.Lunar -> when (transition.event) {
        is LunarPhaseRowEvent.Hover -> state
        is LunarPhaseRowEvent.Select -> Selected.SelectedState.Lunar(transition.event.lunarHorizonState)
    }
    is StateViewEvent.Solar -> when (transition.event) {
        is SolarPhaseRowEvent.Phase.Hover -> state
        is SolarPhaseRowEvent.Phase.Select -> Selected.SelectedState.Solar(transition.event.phase)
        is SolarPhaseRowEvent.Light.Hover -> state
        is SolarPhaseRowEvent.Light.Select -> Selected.SelectedState.Light(transition.event.lightState)
    }
    StateViewEvent.OnMouseLeave -> state
}
