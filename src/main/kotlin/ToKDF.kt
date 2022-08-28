package org.jetbrains.kotlinx.dataframe.api

import krangl.*
import org.jetbrains.kotlinx.dataframe.AnyFrame


fun DataFrame.asKotlinDF(): AnyFrame {
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
    val df = dataFrameOf(kdfCols)
    return df
}
