@file:Suppress("PackageDirectoryMismatch")

package org.jetbrains.kotlinx.dataframe.api.util

import com.github.holgerbrandl.kdfutils.internal.detectPropertiesByReflection
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.columns.toColumnSet
import kotlin.reflect.KCallable
import kotlin.reflect.KProperty

//
@JvmName("unfoldByProperty")
inline fun <reified T> DataFrame<*>.unfoldByProperty(
    columnName: String,
    properties: List<KCallable<*>>,
    keep: Boolean = true,
    addPrefix: Boolean = false,
) = unfoldPropertiesOf<T>(columnName, properties.map { it.name }, keep, addPrefix)

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

//import org.jetbrains.kotlinx.dataframe.DataFrame.unfold as unfoldCore

inline fun <reified T> DataFrame<*>.unfoldPropertiesByRefOf(
    column: String,
    properties: List<KProperty<*>>? = detectPropertiesByReflection<T>(),
    keep: Boolean = false,
    addPrefix: Boolean = false,
) = unfoldPropertiesOf<T>(column, properties?.map{it.name}, keep, addPrefix)


inline fun <reified T> DataFrame<*>.unfoldPropertiesOf(
    column: String,
    properties: List<String>? = detectPropertiesByReflection<T>().map { it.name },
    keep: Boolean = false,
    addPrefix: Boolean = false,
): DataFrame<*> {
    @Suppress("USELESS_CAST")
    val flatten =
        // conversion needed as workaround for https://github.com/Kotlin/dataframe/issues/928
        convert { column<T>() }.with { it as T }
        .select(column)
        .unfold { cols { it.name() == column } }
        .flatten()

    val selectedAttr = flatten
        .run {
            if (properties != null) {
                select { properties.toColumnSet() }
            } else this
        }

    val withPrefix = if (addPrefix) {
        selectedAttr.rename(*selectedAttr.columnNames().map { it to column + "_" + it }
            .toTypedArray())
    } else selectedAttr

    val df = if (!keep) remove(column) else this

    // fix ambiguous names
    val unfoldUnique = withPrefix.rename(
        *withPrefix.columnNames()
            .map {
                it to (if (df.columnNames()
                        .contains(it)
                ) column + "_" + it else it)
            }
            .toTypedArray()
    )

    return df.add(unfoldUnique)
}
