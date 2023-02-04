@file:Suppress("PackageDirectoryMismatch")

package org.jetbrains.kotlinx.dataframe.api.util

import krangl.util.detectPropertiesByReflection
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*

//
//@JvmName("unfoldByProperty")
//fun org.jetbrains.kotlinx.dataframe.DataFrame<*>.unfold2(
//    columnName: String,
//    properties: List<KCallable<*>>,
//    keep: Boolean = true,
//    addPrefix: Boolean = false,
//) = toKranglDF().unfold(columnName, properties, keep, addPrefix)
//    .toKotlinDF()
//
//
//inline fun <reified T> org.jetbrains.kotlinx.dataframe.DataFrame<*>.unfold2(
//    columnName: String,
//    properties: List<String> = detectPropertiesByReflection<T>().map { it.name },
//    keep: Boolean = true,
//    addPrefix: Boolean = false,
//) = toKranglDF().unfold<T>(columnName, properties, keep, addPrefix)
//    .toKotlinDF()


// just kept for compatibility with krangl
inline fun <reified T> DataFrame<*>.unfoldByReflection(
    column: String,
    keep: Boolean = true,
    properties: List<String> = detectPropertiesByReflection<T>().map { it.name },
    addPrefix: Boolean = false,
): DataFrame<*> = this.unfold(column, properties, keep, addPrefix)


fun DataFrame<*>.unfold(
    column: String,
    properties: List<String>? = null,
    keep: Boolean = false,
    addPrefix: Boolean = false,
): DataFrame<*> {
    val unfold = select(column)
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
