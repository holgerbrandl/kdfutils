package kdfutils

import com.github.holgerbrandl.kdfutils.printDataClassSchema
import com.github.holgerbrandl.kdfutils.renameToCamelCase
import kutils.captureOutput
import org.jetbrains.kotlinx.dataframe.datasets.flightsData
import org.junit.jupiter.api.Test

class MiscTests {

    @Test
    fun `it should print the structure of a dataframe as data class definition`() {
        flightsData.renameToCamelCase()

        val (_, out: String, _) = captureOutput {
            flightsData.printDataClassSchema("FlightsRecord")
        }

        println(out)

        data class FlightsRecord(
            val year: Int,
            val month: Int,
            val day: Int,
            val dep_time: Int?,
            val dep_delay: Int?,
            val arr_time: Int?,
            val arr_delay: Int?,
            val carrier: String,
            val tailnum: String,
            val flight: Int,
            val origin: String,
            val dest: String,
            val air_time: Int?,
            val distance: Int,
            val hour: Int?,
            val minute: Int?,
        )

    }
}