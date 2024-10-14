package com.github.holgerbrandl.kdfutils

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.readExcel
import java.io.*


sealed class ColumnTypeSpec {
    abstract fun typeOf(index: Int, columnName: String): ColType?
}

class GuessSpec : NamedColumnSpec(mapOf())

open class NamedColumnSpec(val colTypes: Map<String, ColType>) : ColumnTypeSpec() {
    val DEFAULT_COLUMN_SPEC_NAME = ".default"

    val DEFAULT_TYPE = colTypes[DEFAULT_COLUMN_SPEC_NAME]

    override fun typeOf(index: Int, columnName: String): ColType? {

        return colTypes[columnName] ?: DEFAULT_TYPE
    }

    constructor(vararg spec: Pair<String, ColType>) : this(spec.toMap())
}

/**
 * CompactColumnSpec uses a compact string representation where each character represents one column:
 *
 * c = character
 *
 * i = integer
 *
 * n = number
 *
 * d = double
 *
 * l = logical
 *
 * f = factor
 *
 * D = date
 *
 * T = date time
 *
 * t = time
 *
 * ? = guess
 *
 * _ or - = skip
 */
class CompactColumnSpec(val columnSpec: String) : ColumnTypeSpec() {

    val colTypes = columnSpec.map {
        when (it) {
            's' -> ColType.String
            'i' -> ColType.Int
            'l' -> ColType.Long
            'd' -> ColType.Double
            'b' -> ColType.Boolean
            // TODO natively support reading date columns
            'D' -> ColType.LocalDate
            'T' -> ColType.LocalDateTime
            't' -> ColType.LocalTime
            '?' -> null
//            '_' -> - = skip
            else -> throw IllegalArgumentException("invalid type '$it' in compact column spec '$columnSpec'")
        }
    }

    override fun typeOf(index: Int, columnName: String): ColType? {
        return colTypes[index]
    }
}

//private fun testExcelInCP() {
//    try {
//        Class.forName("org/apache/poi/ss/util/CellRangeAddress")
//    } catch (e: ClassNotFoundException) {
//        throw(RuntimeException("poi dependencies are missing in path. To enable excel support, please add 'org.apache.poi:poi-ooxml:4.1.1' to your project dependencies"))
//    }
//}

/**
 * Returns a DataFrame with the contents from an Excel file sheet.  By default, krangl treats blank cells as missing data.
 */
@JvmOverloads
fun DataFrame.Companion.readExcel(
    path: File,
    sheet: String? = null,
//    cellRange: CellRangeAddress? = null,
    colTypes: ColumnTypeSpec = GuessSpec(),
//    trim: Boolean = false,
//    guessMax: Int = GUESS_MAX,
//    na: String = MISSING_VALUE,
//    stopAtBlankLine: Boolean = true,
//    includeBlankLines: Boolean = false,
): DataFrame<*>  = readExcel(FileInputStream(path),sheet, colTypes)

@JvmOverloads
fun DataFrame.Companion.readExcel(
    path: InputStream,
    sheet: String? = null,
//    cellRange: CellRangeAddress? = null,
    colTypes: ColumnTypeSpec = GuessSpec(),
//    trim: Boolean = false,
//    guessMax: Int = GUESS_MAX,
//    na: String = MISSING_VALUE,
//    stopAtBlankLine: Boolean = true,
//    includeBlankLines: Boolean = false,
): DataFrame<*> {
    val df = DataFrame.readExcel(path, sheet)

    // Apply format conversions and create a new DataFrame with the converted columns
    return df.columns().fold(df) { acc, column ->
        val colType = colTypes.typeOf(acc.columnNames().indexOf(column.name()), column.name())
        acc.convert(column.name()).run {
            when (colType) {
                ColType.String -> toStr()
                ColType.Int -> toInt()
                ColType.Long -> toLong()
                ColType.Double -> toDouble()
                ColType.Boolean -> toBoolean()
                ColType.BigDecimal -> toBigDecimal()
                ColType.LocalDate -> toLocalDate()
                ColType.LocalTime -> toLocalTime()
                ColType.LocalDateTime -> toLocalDateTime()
                null -> to { it }
            }
        }
    }
}


fun main() {
    DataFrame.readExcel("src/test/resources/krangl/data/ExcelReadExample.xlsx")
}
