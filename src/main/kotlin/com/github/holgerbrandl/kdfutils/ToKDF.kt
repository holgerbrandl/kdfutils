package com.github.holgerbrandl.kdfutils

import krangl.*
import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.api.columnOf
import org.jetbrains.kotlinx.dataframe.api.named


fun DataFrame.toKotlinDF(): AnyFrame {
    val kdfCols = cols.map {
        when(it) {
            is DoubleCol -> columnOf(*it.values)
            is IntCol -> columnOf(*it.values)
            is StringCol -> columnOf(*it.values)
            is BooleanCol -> columnOf(*it.values)
            is LongCol -> columnOf(*it.values)
            is AnyCol -> columnOf(*it.values)
            else -> {
                TODO()
            }
        }.named(it.name)
    }
    val df = org.jetbrains.kotlinx.dataframe.api.dataFrameOf(kdfCols)
    return df
}
