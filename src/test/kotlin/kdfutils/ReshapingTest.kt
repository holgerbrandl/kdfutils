package kdfutils

import com.github.holgerbrandl.kdfutils.spread
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.api.inferType
import org.jetbrains.kotlinx.dataframe.api.isType
import org.junit.jupiter.api.Test

class SpreadTest {

    @Test
    fun `it should reshape from long to wide`() {
        val longDf = dataFrameOf("person", "year", "weight", "sex")(
            "max", 2014, 33.1, "M",
            "max", 2015, 32.3, "M",
            "max", 2016, null, "M",
            "anna", 2013, 33.5, "F",
            "anna", 2014, 37.3, "F",
            "anna", 2015, 39.2, "F",
            "anna", 2016, 39.9, "F"
        )

        longDf.spread("year", "weight").apply {
            rowsCount() shouldBe 2
            columnsCount() shouldBe 6  // name, sex, 4 year columns

            // ensure that types were coerced correctly
            (this["2013"].isType<Double?>()) shouldBe true
            (this["2016"].isType<Double?>()) shouldBe true
        }
    }


    @Test
    fun `it should type convert stringified values from long to wide`() {
        val longDf = dataFrameOf("person", "property", "value", "sex")(
            "max", "salary", "33.1", "M",
            "max", "city", "London", "M",
            "anna", "salary", "33.5", "F",
            "anna", "city", "Berlin", "F"
        )

        longDf.spread("property", "value", convert = true).inferType("salary").apply {
            rowsCount() shouldBe 2
            columnsCount() shouldBe 4  // name, sex, 4 year columns

            // ensure that types were coerced correctly
            (this["city"].isType<String>()) shouldBe true
            (this["salary"].isType<Double>()) shouldBe true
        }
    }
}