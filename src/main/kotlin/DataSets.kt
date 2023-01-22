@file:Suppress("PackageDirectoryMismatch")

package org.jetbrains.kotlinx.dataframe.datasets

import com.github.holgerbrandl.kdfutils.toKotlinDF

val irisData by lazy{ krangl.irisData.toKotlinDF() }

val sleepData by lazy{ krangl.sleepData.toKotlinDF() }

val flightsData by lazy{ krangl.flightsData.toKotlinDF() }

val sleepPatterns by lazy {krangl.sleepPatterns }
