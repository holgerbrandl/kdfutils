package org.jetbrains.kotlinx.dataframe.api

import krangl.asDataFrame
import krangl.unfold
import krangl.util.detectPropertiesByReflection
import org.apache.commons.csv.CSVFormat
import org.jetbrains.kotlinx.dataframe.AnyFrame
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import java.io.File
import kotlin.reflect.KCallable


inline fun <reified T> Iterable<T>.asDataFrame(): AnyFrame = asDataFrame().asKotlinDF()


@JvmName("unfoldByProperty")
fun org.jetbrains.kotlinx.dataframe.DataFrame<*>.unfold(
    columnName: String,
    properties: List<KCallable<*>>,
    keep: Boolean = true,
    addPrefix: Boolean = false,
) = asKranglDF().unfold(columnName, properties, keep, addPrefix).asKotlinDF()


inline fun <reified T> org.jetbrains.kotlinx.dataframe.DataFrame<*>.unfold(
    columnName: String,
    properties: List<String> = detectPropertiesByReflection<T>().map { it.name },
    keep: Boolean = true,
    addPrefix: Boolean = false,
) = asKranglDF().unfold<T>(columnName, properties, keep, addPrefix).asKotlinDF()


//fun DataFrame.convert(val columnName, fal )


fun org.jetbrains.kotlinx.dataframe.DataFrame<*>.writeTSV(file: File) = writeCSV(file, CSVFormat.TDF)