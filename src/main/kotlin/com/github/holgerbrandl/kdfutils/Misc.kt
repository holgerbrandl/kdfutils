@file:Suppress("unused")

package com.github.holgerbrandl.kdfutils

import org.apache.commons.csv.CSVFormat
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.into
import org.jetbrains.kotlinx.dataframe.api.rename
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import java.io.File


//fun DataFrame.convert(val columnName, fal )

fun DataFrame<*>.writeTSV(file: File) = writeCSV(file, CSVFormat.TDF)


//https://stackoverflow.com/questions/60010298/how-can-i-convert-a-camel-case-string-to-snake-case-and-back-in-idiomatic-kotlin
internal fun String.camelToSnakeCase(): String {
    return "(?<=[a-zA-Z])[A-Z]".toRegex()
        .replace(this) {
            "_${it.value}"
        }
        .lowercase()
}

fun String.snakeToCamelCase(): String {
    return "_[a-zA-Z]".toRegex().replace(this) {
        it.value.replace("_","")
            .uppercase()
    }
}

/** Convert column names to snake case */
fun DataFrame<*>.renameToSnakeCase() = rename { all() }.into { it.name.camelToSnakeCase() }
fun DataFrame<*>.renameToCamelCase() = rename { all() }.into { it.name.snakeToCamelCase() }