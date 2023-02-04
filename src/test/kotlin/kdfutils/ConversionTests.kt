package kdfutils

import com.github.holgerbrandl.kdfutils.toKotlinDF
import com.github.holgerbrandl.kdfutils.toKranglDF
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.datasets.irisData
import org.junit.Test

private val <T> DataFrame<T>.names: Any
    get() = columnNames()

class ConversionTests {

    @Test
    fun `it should bidirectionally convert data-frame`() {
        val kotlinDF = irisData.toKotlinDF()
        val kranglDF = kotlinDF.toKranglDF()

        kranglDF shouldBe irisData
    }

    @Test
    fun `it should convert lists of objects into data-frame`() {
        data class Person(val name: String, val age: Int, val weight: Double)

        val users = listOf(
            Person("Anne", 23, 55.4),
            Person("Tina", 40, 60.4)
        )

        val df = users.toDataFrame()

        df.names shouldBe listOf("name", "age", "weight")
        df["age"][0] shouldBe 23
    }
}

