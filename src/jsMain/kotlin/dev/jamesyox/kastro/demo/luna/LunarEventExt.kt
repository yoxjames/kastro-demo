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

import dev.jamesyox.kastro.luna.LunarEvent

val LunarEvent.prettyString get() = when (this) {
    is LunarEvent.HorizonEvent.Moonrise -> "Moonrise"
    is LunarEvent.HorizonEvent.Moonset -> "Moonset"
    is LunarEvent.PhaseEvent.FirstQuarter -> "First Quarter"
    is LunarEvent.PhaseEvent.FullMoon -> "Full Moon"
    is LunarEvent.PhaseEvent.LastQuarter -> "Last Quarter"
    is LunarEvent.PhaseEvent.NewMoon -> "New Moon"
}

val LunarEvent.shortDescription get() = when (this) {
    is LunarEvent.HorizonEvent.Moonrise -> "The center of the moon rises above the horizon"
    is LunarEvent.HorizonEvent.Moonset -> "The center of the moon sets below the horizon"
    is LunarEvent.PhaseEvent.FirstQuarter -> "Half the moon is illuminated during the waxing phase"
    is LunarEvent.PhaseEvent.FullMoon -> "The moon is fully illuminated"
    is LunarEvent.PhaseEvent.LastQuarter -> "Half the moon is illuminated during the waning phase"
    is LunarEvent.PhaseEvent.NewMoon -> "The sun and moon aligned and the moon is not illuminated"
}
