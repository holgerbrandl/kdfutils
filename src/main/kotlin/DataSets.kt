package org.jetbrains.kotlinx.dataframe.datasets

import org.jetbrains.kotlinx.dataframe.api.asKotlinDF

val irisData by lazy{ krangl.irisData.asKotlinDF() }

val sleepData by lazy{ krangl.sleepData.asKotlinDF() }
