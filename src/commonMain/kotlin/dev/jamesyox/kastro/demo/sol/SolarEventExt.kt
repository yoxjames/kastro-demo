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

package dev.jamesyox.kastro.demo.sol

import dev.jamesyox.kastro.sol.SolarEvent

val SolarEvent.prettyString get() = when (this) {
    is SolarEvent.Sunrise -> "Sunrise"
    is SolarEvent.Sunset -> "Sunset"
    is SolarEvent.SunriseEnd -> "Sunrise End"
    is SolarEvent.SunsetBegin -> "Sunset Begin"
    is SolarEvent.BlueHourDawn -> "Blue Hour Dawn"
    is SolarEvent.BlueHourDawnEnd -> "Blue Hour Dawn End"
    is SolarEvent.BlueHourDusk -> "Blue Hour Dusk"
    is SolarEvent.BlueHourDuskEnd -> "Blue Hour Dusk End"
    is SolarEvent.GoldenHourDawn -> "Golden Hour Dawn"
    is SolarEvent.GoldenHourDawnEnd -> "Golden Hour Dawn End"
    is SolarEvent.GoldenHourDusk -> "Golden Hour Dusk"
    is SolarEvent.GoldenHourDuskEnd -> "Golden Hour Dusk End"
    is SolarEvent.Nadir -> "Nadir"
    is SolarEvent.Noon -> "Noon"
    is SolarEvent.AstronomicalDawn -> "Astronomical Dawn"
    is SolarEvent.AstronomicalDusk -> "Astronomical Dusk"
    is SolarEvent.CivilDawn -> "Civil Dawn"
    is SolarEvent.CivilDusk -> "Civil Dusk"
    is SolarEvent.Day -> "Day"
    is SolarEvent.NauticalDawn -> "Nautical Dawn"
    is SolarEvent.NauticalDusk -> "Nautical Dusk"
    is SolarEvent.Night -> "Night"
}

val SolarEvent.shortDescription get() = when (this) {
    is SolarEvent.Sunrise ->
        "The top edge of the sun rises above the horizon. " +
            "This is colloquially known as \"Sunrise\""
    is SolarEvent.Sunset -> "The top edge of the sun sets below the horizon. This is colloquially known as \"Sunset\""
    is SolarEvent.SunriseEnd -> "The bottom edge of the sun rises above the horizon"
    is SolarEvent.SunsetBegin -> "The bottom edge of the sun sets below the horizon"
    is SolarEvent.BlueHourDawn -> "Blue hour has begun during dawn"
    is SolarEvent.BlueHourDawnEnd -> "Blue hour has ended during dawn"
    is SolarEvent.BlueHourDusk -> "Blue hour has started during dusk"
    is SolarEvent.BlueHourDuskEnd -> "Blue hour has ended during dusk"
    is SolarEvent.GoldenHourDawn -> "Golden hour has started during dawn"
    is SolarEvent.GoldenHourDawnEnd -> "Golden hour has ended during dawn"
    is SolarEvent.GoldenHourDusk -> "Golden hour has started during dusk"
    is SolarEvent.GoldenHourDuskEnd -> "Golden hour has ended during dusk"
    is SolarEvent.Nadir -> "The sun has reached the lowest point in the sky"
    is SolarEvent.Noon -> "The sun has reached the highest point in the sky"
    is SolarEvent.AstronomicalDawn -> "Astronomical Dawn has begun"
    is SolarEvent.AstronomicalDusk -> "Astronomical Dusk has begun"
    is SolarEvent.CivilDawn -> "Civil dawn has begun"
    is SolarEvent.CivilDusk -> "Civil dusk has begun"
    is SolarEvent.Day ->
        "The middle of the sun is above 0° over the horizon. " +
            "This does not account for atmospheric refraction"
    is SolarEvent.NauticalDawn -> "Nautical dawn has begun"
    is SolarEvent.NauticalDusk -> "Nautical dusk has begun"
    is SolarEvent.Night -> "Night has begun"
}
