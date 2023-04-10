@file:Suppress("PackageDirectoryMismatch")

package org.jetbrains.kotlinx.dataframe.api.util

import com.github.holgerbrandl.kdfutils.toKotlinDF
import com.github.holgerbrandl.kdfutils.toKranglDF
import krangl.unfold
import krangl.util.detectPropertiesByReflection
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import kotlin.reflect.KCallable

//
@JvmName("unfoldByProperty")
inline fun  <reified T> DataFrame<*>.unfoldByProperty(
    columnName: String,
    properties: List<KCallable<*>>,
    keep: Boolean = true,
    addPrefix: Boolean = false,
) = toKranglDF().unfold(columnName, properties, keep, addPrefix)
    .toKotlinDF()
//
//
//inline fun <reified T> org.jetbrains.kotlinx.dataframe.DataFrame<*>.unfold2(
//    columnName: String,
//    properties: List<String> = detectPropertiesByReflection<T>().map { it.name },
//    keep: Boolean = true,
//    addPrefix: Boolean = false,
//) = toKranglDF().unfold<T>(columnName, properties, keep, addPrefix)
//    .toKotlinDF()

//
////// just kept for compatibility with krangl
//inline fun <reified T> DataFrame<*>.unfoldByReflection(
//    column: String,
//    keep: Boolean = true,
//    properties: List<String> = detectPropertiesByReflection<T>().map { it.name },
//    addPrefix: Boolean = false,
//): DataFrame<*> = this.unfold<T>(column, properties, keep, addPrefix)


inline fun <reified T> DataFrame<*>.unfold(
    column: String,
    properties: List<String>? = detectPropertiesByReflection<T>().map { it.name },
    keep: Boolean = false,
    addPrefix: Boolean = false,
): DataFrame<*> {
    val unfold = inferType().select(column)
        .unfold { cols { it.name() == column } }
        .flatten()
        .run {
            if(properties != null) {
                select(properties)
            } else this
        }

    val withPrefix = if(addPrefix) {
        unfold.rename(*unfold.columnNames().map { it to column + "_" + it }
            .toTypedArray())
    } else unfold

    val df = if(!keep) remove(column) else this

    // fix ambiguous names
    val unfoldUnique = withPrefix.rename(
        *withPrefix.columnNames()
            .map {
                it to (if(df.columnNames()
                        .contains(it)
                ) column + "_" + it else it)
            }
            .toTypedArray()
    )

    return df.add(unfoldUnique)
}
