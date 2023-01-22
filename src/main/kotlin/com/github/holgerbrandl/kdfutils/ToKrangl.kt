package com.github.holgerbrandl.kdfutils

import krangl.AnyCol
import org.jetbrains.kotlinx.dataframe.values
import kotlin.reflect.typeOf


fun <E> org.jetbrains.kotlinx.dataframe.DataFrame<E>.toKranglDF(): krangl.DataFrame {
    val kdfCols = columns().map {
        when {
            it.type() == typeOf<Double>() -> krangl.DoubleCol(it.name(), it.values() as List<Double?>)
            it.type() == typeOf<Int>() -> krangl.IntCol(it.name(), it.values() as List<Int?>)
            it.type() == typeOf<String>() -> krangl.StringCol(it.name(), it.values() as List<String?>)
            it.type() == typeOf<Boolean>() -> krangl.BooleanCol(it.name(), it.values() as List<Boolean?>)
            it.type() == typeOf<Long>() -> krangl.LongCol(it.name(), it.values() as List<Long?>)
            else -> AnyCol(it.name(), it.values as List)
//            else -> {
//                TODO("unsupported column type ${it}")
//            }
        }
    }
    val df = krangl.dataFrameOf(*kdfCols.toTypedArray())
    return df
}


fun main() {
}