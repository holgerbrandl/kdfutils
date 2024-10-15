package com.github.holgerbrandl.kdfutils

import org.jetbrains.kotlinx.dataframe.AnyRow
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*

/**
 * Spread a key-value pair across multiple columns.
 *
 * @param key The bare (unquoted) name of the column whose values will be used as column headings.
 * @param value The bare (unquoted) name of the column whose values will populate the cells.
 * @param fill If set, missing values will be replaced with this value.
 * @param convert If set, attempt to do a type conversion will be run on all new columns. This is useful if the value column
 *                was a mix of variables that was coerced to a string.
 */
fun DataFrame<*>.spread(
    key: String,
    value: String,
    fill: Any? = null,
    convert: Boolean = false
): DataFrame<*> {
    // Get unique values of the key column to determine new column names
    val newColNames = this[key].distinct().toList()

    // Ensure new column names don't already exist
    require(newColNames.all { it !in this.columnNames() }) { "spread columns already exist in data-frame" }

    // Group by all columns except key and value columns
    val groupedDf = this.groupBy { all().except(key, value) }


//    fun addMapAsColumns(dataFrame: DataFrame<*>, result: LinkedHashMap<Int, Double?>): DataFrame<*> {
//        // Create a map with the columns and their corresponding single-row values as lists
//        val columnsMap = result.mapValues { listOf(it.value) } // wrap each value in a list for a single row
//
//        // Use the DataFrame API to add these columns to the existing DataFrame
//        return dataFrame.add(columnsMap)
//    }

    // Map each group to a DataFrame with spread columns
    val spreadGroups = groupedDf.groups.map { group ->
        // Create a map of new column names to values for the group
        val keyValues = group[key].toList().zip(group[value].toList()).toMap()
        val spreadCols = newColNames.associateWith { colName -> keyValues[colName] ?: fill }

        // Add the spread columns to the DataFrame in one go using add { spreadCols }
        var select = group.distinct { all().except(key, value) }
        spreadCols.map{ (key, value) ->
            select = select.add(key.toString()){ value }.inferType()
        }
//        select.add { spreadCols }
        select
    }

    // Combine the spread groups into a single DataFrame
    var result = spreadGroups.concat().explode()

    // Type conversion if needed
//    if (convert) {
//        for (colName in newColNames) {
//            result = result.convert { colName }.with { it.convert() }
//        }
//    }



    // Extension function to check if all values in a column can be parsed as Double
    fun Iterable<Any?>.canBeParsedAsDouble(): Boolean {
        return all { value ->
            value == null || (value is String && value.toDoubleOrNull() != null)
        }
    }

    fun DataFrame<*>.convertStringColumnsToDouble(): DataFrame<*> {
        // Convert each String column to Double if all values can be parsed as Double
        return this.convert { cols { it.isType<String>() && it.values().canBeParsedAsDouble() } }
            .with { (it as String).toDouble() }
    }



    return result.convertStringColumnsToDouble()
}