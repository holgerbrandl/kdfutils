package com.github.holgerbrandl.kdfutils

import krangl.unfold
import krangl.util.detectPropertiesByReflection
import org.apache.commons.csv.CSVFormat
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import java.io.File
import kotlin.reflect.KCallable


@JvmName("unfoldByProperty")
fun org.jetbrains.kotlinx.dataframe.DataFrame<*>.unfold(
    columnName: String,
    properties: List<KCallable<*>>,
    keep: Boolean = true,
    addPrefix: Boolean = false,
) = toKranglDF().unfold(columnName, properties, keep, addPrefix).toKotlinDF()


inline fun <reified T> org.jetbrains.kotlinx.dataframe.DataFrame<*>.unfold(
    columnName: String,
    properties: List<String> = detectPropertiesByReflection<T>().map { it.name },
    keep: Boolean = true,
    addPrefix: Boolean = false,
) = toKranglDF().unfold<T>(columnName, properties, keep, addPrefix).toKotlinDF()

