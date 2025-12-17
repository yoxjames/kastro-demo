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

import dev.jamesyox.kastro.common.HorizonState
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.luna.calculateLunarState
import kotlin.time.Instant

data class LunarHorizonState(
    val start: Instant,
    val end: Instant,
    val horizonState: HorizonState,
    val startEvent: LunarEvent.HorizonEvent?,
    val endEvent: LunarEvent.HorizonEvent?
)

private data class LunarHorizonElement(
    val start: Instant,
    val startEvent: LunarEvent.HorizonEvent?,
    val horizonState: HorizonState
)

fun List<LunarEvent>.lunarHorizonState(
    timeRange: ClosedRange<Instant>,
    location: Pair<Double, Double>
): List<LunarHorizonState> {
    return asSequence()
        .filterIsInstance<LunarEvent.HorizonEvent>()
        .scan(
            LunarHorizonElement(
                start = timeRange.start,
                startEvent = null,
                horizonState = timeRange.start.calculateLunarState(location).horizonState
            )
        ) { _, horizonEvent ->
            LunarHorizonElement(
                start = horizonEvent.time,
                startEvent = horizonEvent,
                horizonState = horizonEvent.horizonState
            )
        }
        .plus(
            LunarHorizonElement(
                start = timeRange.endInclusive,
                startEvent = null,
                horizonState = timeRange.endInclusive.calculateLunarState(location).horizonState
            )
        ).zipWithNext()
        .map {
            LunarHorizonState(
                start = it.first.start,
                end = it.second.start,
                horizonState = it.first.horizonState,
                startEvent = it.first.startEvent,
                endEvent = it.second.startEvent
            )
        }.toList()
}

private val LunarEvent.HorizonEvent.horizonState get() = when (this) {
    is LunarEvent.HorizonEvent.Moonrise -> HorizonState.Up
    is LunarEvent.HorizonEvent.Moonset -> HorizonState.Down
}
