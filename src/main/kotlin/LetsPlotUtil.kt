package org.jetbrains.kotlinx.dataframe.api

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.letsPlot.intern.GenericAesMapping

/** Plot a data-frame with let-plot. To use this mapping add `implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:3.0.1")` or via `%use lets-plot` when using jupyter. */
fun DataFrame<*>.letsPlot(mapping: GenericAesMapping.() -> Unit = {}) = org.jetbrains.letsPlot.letsPlot(toMap(), mapping)
