package com.github.holgerbrandl.kdfutils

import org.apache.commons.csv.CSVFormat
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import java.io.File


//fun DataFrame.convert(val columnName, fal )

fun org.jetbrains.kotlinx.dataframe.DataFrame<*>.writeTSV(file: File) = writeCSV(file, CSVFormat.TDF)

