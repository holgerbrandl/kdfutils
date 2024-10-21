package kdfutils

import io.kotest.matchers.shouldBe
import org.jetbrains.kotlinx.dataframe.datasets.irisData
import org.jetbrains.kotlinx.dataframe.datasets.sleepData
import org.jetbrains.kotlinx.dataframe.datasets.sleepPatterns
import org.junit.jupiter.api.Test

class DataSetTests{
    @Test
    fun `it should load test datasets`(){
        irisData.apply{
            columnsCount() shouldBe  5
        }

        sleepData.apply {
            columnsCount() shouldBe 11
        }

        sleepPatterns.apply {
            size shouldBe 83
        }
    }
}