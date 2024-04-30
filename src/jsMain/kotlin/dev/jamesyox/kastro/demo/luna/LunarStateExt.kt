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

package dev.jamesyox.kastro.demo.luna

import dev.jamesyox.kastro.demo.misc.MovementIndicators
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.luna.LunarPhase
import dev.jamesyox.kastro.luna.LunarState

val LunarPhase.prettyString get() = when (this) {
    LunarPhase.Intermediate.WaningCrescent -> "Waning Crescent"
    LunarPhase.Intermediate.WaningGibbous -> "Waning Gibbous"
    LunarPhase.Intermediate.WaxingCrescent -> "Waxing Crescent"
    LunarPhase.Intermediate.WaxingGibbous -> "Waxing Gibbous"
    LunarEvent.PhaseEvent.FirstQuarter -> "First Quarter"
    LunarEvent.PhaseEvent.FullMoon -> "Full Moon"
    LunarEvent.PhaseEvent.LastQuarter -> "Last Quarter"
    LunarEvent.PhaseEvent.NewMoon -> "New Moon"
}

val LunarState.illuminationIndicator get() = when (phase) {
    LunarPhase.Intermediate.WaningCrescent -> MovementIndicators.Down
    LunarPhase.Intermediate.WaningGibbous -> MovementIndicators.Down
    LunarPhase.Intermediate.WaxingCrescent -> MovementIndicators.Up
    LunarPhase.Intermediate.WaxingGibbous -> MovementIndicators.Up
}
