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

package dev.jamesyox.kastro.demo.clock

import dev.jamesyox.kastro.demo.luna.LunarHorizonState
import dev.jamesyox.kastro.demo.sol.SolarLightState
import dev.jamesyox.kastro.demo.sol.SolarPhaseState

sealed interface CelestialClockEvent {

    sealed interface LunarHorizonEvent : CelestialClockEvent {
        val lunarHorizonState: LunarHorizonState

        value class Hover(
            override val lunarHorizonState: LunarHorizonState
        ) : LunarHorizonEvent

        value class Select(
            override val lunarHorizonState: LunarHorizonState
        ) : LunarHorizonEvent
    }

    sealed interface SolarPhaseEvent : CelestialClockEvent {
        val solarPhaseState: SolarPhaseState

        value class Hover(
            override val solarPhaseState: SolarPhaseState
        ) : SolarPhaseEvent

        value class Select(
            override val solarPhaseState: SolarPhaseState
        ) : SolarPhaseEvent
    }

    sealed interface LightEvent : CelestialClockEvent {
        val solarLightState: SolarLightState

        value class Hover(
            override val solarLightState: SolarLightState
        ) : LightEvent

        value class Select(
            override val solarLightState: SolarLightState
        ) : LightEvent
    }

    data object OnMouseLeave : CelestialClockEvent
    data object OnSettingsClick : CelestialClockEvent
}
