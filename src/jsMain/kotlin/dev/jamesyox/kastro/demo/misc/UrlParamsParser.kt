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

package dev.jamesyox.kastro.demo.misc

import dev.jamesyox.kastro.demo.CelestialParameters
import kotlinx.browser.window
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import web.url.URLSearchParams

private const val LATITUDE_KEY = "latitude"
private const val LONGITUDE_KEY = "longitude"
private const val DATE_KEY = "date"

class UrlParamsParser(clock: Clock, timeZone: TimeZone) {
    val startingParams get() = getUrlParams() ?: defaultDenverParams

    private val denver = Location(latitude = 39.749618, longitude = -104.988892)

    private val defaultDenverParams = CelestialParameters(
        location = denver,
        timeZone = timeZone,
        date = clock.now().toLocalDateTime(timeZone).date
    )

    private fun getUrlParams(): CelestialParameters? {
        val search = window.location.search
        return URLSearchParams(search).extractParams()
    }

    @Suppress("ReturnCount")
    private fun URLSearchParams.extractParams(): CelestialParameters? {
        val date = this[DATE_KEY]?.toLocalDate() ?: defaultDenverParams.date
        return CelestialParameters(
            location = Location(
                latitude = this[LATITUDE_KEY]?.toDouble() ?: return null,
                longitude = this[LONGITUDE_KEY]?.toDouble() ?: return null
            ),
            date = date,
            timeZone = TimeZone.currentSystemDefault()
        )
    }
}

val CelestialParameters.url: String get(): String {
    val params = listOf(
        LATITUDE_KEY to location.latitude.toString(),
        LONGITUDE_KEY to location.longitude.toString(),
        DATE_KEY to date.toString()
    )
    val paramsString = params.joinToString(separator = "&") {
        "${it.first}=${it.second}"
    }
    return with(window.location) { "$origin$pathname?$paramsString" }
}
val CelestialParameters.locationUrl: String get(): String {
    val params = listOf(
        LATITUDE_KEY to location.latitude.toString(),
        LONGITUDE_KEY to location.longitude.toString(),
    )
    val paramsString = params.joinToString(separator = "&") {
        "${it.first}=${it.second}"
    }
    return with(window.location) { "$origin$pathname?$paramsString" }
}
