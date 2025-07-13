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

package dev.jamesyox.kastro.demo.input

import dev.jamesyox.kastro.demo.CelestialParameters
import dev.jamesyox.kastro.demo.misc.Location
import dev.jamesyox.kastro.demo.misc.locationUrl
import dev.jamesyox.kastro.demo.misc.url
import dev.jamesyox.kastro.demo.svgk.js.svgMagick
import dev.jamesyox.kastro.demo.values
import dev.jamesyox.statik.css
import dev.jamesyox.svgk.attr.types.obj.ViewBox
import dev.jamesyox.svgk.tags.path
import dev.jamesyox.svgk.tags.svg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.Position
import kotlinx.css.alignItems
import kotlinx.css.columnGap
import kotlinx.css.display
import kotlinx.css.em
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.left
import kotlinx.css.marginBottom
import kotlinx.css.paddingBottom
import kotlinx.css.paddingTop
import kotlinx.css.pct
import kotlinx.css.position
import kotlinx.css.properties.Transforms
import kotlinx.css.properties.translate
import kotlinx.css.px
import kotlinx.css.top
import kotlinx.css.transform
import kotlinx.css.width
import kotlinx.css.zIndex
import kotlinx.datetime.LocalDate
import kotlinx.html.InputType
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.js.div
import kotlinx.html.js.input
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.js.table
import kotlinx.html.js.td
import kotlinx.html.js.tr
import kotlinx.html.label
import org.w3c.dom.HTMLElement
import web.clipboard.writeText
import web.navigator.navigator
import kotlin.time.Duration.Companion.seconds

context(_: CoroutineScope)
fun TagConsumer<HTMLElement>.InputPanel(
    parameters: CelestialParameters,
    mapUpdates: Flow<Location>,
    paramsState: StateFlow<CelestialParameters>,
    onEvent: (InputPanelEvent) -> Unit
): HTMLElement {
    return div {
        table {
            css {
                paddingBottom = 12.px
            }
            LatitudeInput(
                initialValue = parameters.location.latitude,
                value = mapUpdates.map { it.latitude }
            ) {
                onEvent(InputPanelEvent.LatitudeUpdate(it))
            }
            LongitudeInput(
                initialValue = parameters.location.longitude,
                value = mapUpdates.map { it.longitude }
            ) {
                onEvent(InputPanelEvent.LongitudeUpdate(it))
            }
            HeightInput(parameters.height) {
                onEvent(InputPanelEvent.HeightChange(it))
            }
            DateInput(parameters.date) {
                onEvent(InputPanelEvent.DateChange(it))
            }
        }
        div {
            css {
                display = Display.flex
                width = 100.pct
                flexDirection = FlexDirection.row
                alignItems = Align.center
                justifyContent = JustifyContent.center
                columnGap = 12.px
                marginBottom = 12.px
            }
            CopyButton(
                paramState = paramsState,
                text = "Copy Location Link",
                withDate = false
            )
            CopyButton(
                paramState = paramsState,
                text = "Copy Full Link",
                withDate = true
            )
            button {
                css {
                    height = 50.px
                }
                img {
                    css {
                        paddingTop = 4.px
                        width = 24.px
                        height = 24.px
                    }
                    src = "locate.svg"
                }
                onClickFunction = { onEvent(InputPanelEvent.GeolocationRequest) }
            }
        }
        // Map
        div {
            id = "map"
            svgMagick {
                svg(
                    viewBox = ViewBox(-3, -7, 6, 14)
                ) {
                    path(fill = "blue", strokeWidth = 0.25, stroke = "white") {
                        M(0, 0)
                        L(-3, -7)
                        L(0, -5)
                        L(3, -7)
                        Z
                    }
                }.css {
                    position = Position.absolute
                    height = 40.px
                    width = 40.px
                    zIndex = 500
                    top = 50.pct
                    left = 50.pct
                    transform = Transforms().apply {
                        translate((-50).pct, (-50).pct)
                    }
                }
            }
        }
    }
}

context(coroutineScope: CoroutineScope)
private fun TagConsumer<HTMLElement>.CopyButton(
    paramState: StateFlow<CelestialParameters>,
    text: String,
    withDate: Boolean
) {
    button {
        css {
            height = 50.px
        }
        +text
        onClickFunction = {
            coroutineScope.launch {
                navigator.clipboard?.writeText(if (withDate) paramState.value.url else paramState.value.locationUrl)
                val target = it.target.asDynamic()
                target.innerText = "Copied"
                delay(1.seconds)
                target.innerText = text
            }
        }
    }
}

sealed interface InputPanelEvent {
    sealed interface ManualEvent : InputPanelEvent
    value class LatitudeUpdate(
        val latitude: Double
    ) : ManualEvent

    value class LongitudeUpdate(
        val longitude: Double
    ) : ManualEvent

    value class DateChange(
        val date: LocalDate
    ) : ManualEvent

    value class HeightChange(
        val height: Double
    ) : ManualEvent

    data object GeolocationRequest : InputPanelEvent
}

private fun TagConsumer<HTMLElement>.DateInput(
    value: LocalDate,
    onChange: (LocalDate) -> Unit
) {
    tr {
        td {
            label {
                htmlFor = "dateInput"
                +"Date"
            }
        }
        td {
            input {
                css {
                    width = 10.em
                }
                id = "dateInput"
                type = InputType.date
                this.value = value.toString()
                onInputFunction = {
                    runCatching { LocalDate.parse((it.target.asDynamic().value as String)) }
                        .getOrNull()
                        ?.also(onChange)
                    onChange(LocalDate.parse((it.target.asDynamic().value as String)))
                }
            }
        }
    }
}

context(coroutineScope: CoroutineScope)
private fun TagConsumer<HTMLElement>.LatitudeInput(
    initialValue: Double,
    value: Flow<Double>,
    onChange: (Double) -> Unit
) {
    tr {
        td {
            label { +"Latitude:" }
        }
        td {
            input {
                css {
                    width = 10.em
                }
                this.type = InputType.number
                this.value = initialValue.toString()
                onInputFunction = {
                    (it.target.asDynamic().value as String)
                        .toDoubleOrNull()
                        ?.coerceIn(-90.0..90.0)
                        ?.also(onChange)
                }
                max = "90"
                min = "-90"
            }.values(value.map { it.toString() })
        }
    }
}

context(coroutineScope: CoroutineScope)
private fun TagConsumer<HTMLElement>.LongitudeInput(
    initialValue: Double,
    value: Flow<Double>,
    onChange: (Double) -> Unit
) {
    tr {
        td {
            label { +"Longitude:" }
        }
        td {
            input {
                css {
                    width = 10.em
                }
                this.type = InputType.number
                this.value = initialValue.toString()
                onInputFunction = {
                    (it.target.asDynamic().value as String)
                        .toDoubleOrNull()
                        ?.coerceIn(-180.0..180.0)
                        ?.also(onChange)
                }
                max = "180"
                min = "-180"
            }.values(value.map { it.toString() })
        }
    }
}

context(coroutineScope: CoroutineScope)
private fun TagConsumer<HTMLElement>.HeightInput(
    initialValue: Double,
    onChange: (Double) -> Unit
) {
    tr {
        td {
            label { +"Height (meters):" }
        }
        td {
            input {
                css {
                    width = 10.em
                }
                this.type = InputType.number
                this.value = initialValue.toString()
                onInputFunction = {
                    (it.target.asDynamic().value as String)
                        .toDoubleOrNull()
                        ?.coerceIn(-180.0..180.0)
                        ?.also(onChange)
                }

                min = "0"
            }
        }
    }
}
