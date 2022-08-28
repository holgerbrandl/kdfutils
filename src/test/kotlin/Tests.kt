import io.kotest.matchers.shouldBe
import krangl.irisData
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.junit.Test
import java.util.*

private val <T> DataFrame<T>.names: Any
    get() = columnNames()

class ConversionTest {

    @Test
    fun `it should bidirectionally convert data-frame`() {
        val kotlinDF = irisData.asKotlinDF()
        val kranglDF = kotlinDF.asKranglDF()

        kranglDF shouldBe irisData
    }

    @Test
    fun `it should convert lists of objects into data-frame`() {
        data class Person(val name: String, val age: Int, val weight: Double)

        val users = listOf(
            Person("Anne", 23, 55.4),
            Person("Tina", 40, 60.4)
        )

        val df = users.asDataFrame()

        df.names shouldBe listOf("age", "name", "weight")
        df["age"][0] shouldBe 23
    }
}

class UnfoldTests {

    @Test
    fun `it should unwrap properties of object columns`() {
        val users = dataFrameOf("department", "ids")(
            "tech", UUID.randomUUID(),
            "admin", UUID.randomUUID()
        )

        val unwrapped = users.unfold<UUID>("ids", properties = listOf("variant"), keep = true)

        unwrapped["variant"][1] shouldBe 2
    }

    @Test
    fun `it should unwrap properties by property reference`() {
        val users = dataFrameOf("department", "ids")(
            "tech", UUID.randomUUID(),
            "admin", UUID.randomUUID()
        )

        val unwrapped = users.unfold("ids", properties = listOf(UUID::variant), keep = true)

        unwrapped["variant"][1] shouldBe 2
    }
}