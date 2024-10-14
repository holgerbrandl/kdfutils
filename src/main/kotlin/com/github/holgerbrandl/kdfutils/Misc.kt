@file:Suppress("unused")

package com.github.holgerbrandl.kdfutils

import org.apache.commons.csv.CSVFormat
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.generateDataClasses
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import java.io.File


/** Wrapper around writeCSV to write a tab-separated file instead.*/
fun DataFrame<*>.writeTSV(file: File) = writeCSV(file, CSVFormat.TDF)

/** Workaround until https://github.com/Kotlin/dataframe/issues/344 has been fixed.*/
fun DataFrame<*>.printDataClassSchema(className: String) = generateDataClasses(className).print()
