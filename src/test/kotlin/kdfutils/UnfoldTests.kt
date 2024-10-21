package kdfutils

import io.kotest.matchers.shouldBe
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.util.unfoldPropertiesOf
import org.jetbrains.kotlinx.dataframe.api.util.unfoldPropertiesByRefOf
import org.junit.jupiter.api.Test

class UnfoldTests {
    data class City(val name: String, val code: Int)
    data class Person(val name: String, val address: City)

    val persons: List<Person> = listOf(
        Person("Max", City("Dresden", 12309)),
        Person("Anna", City("Berlin", 10115))
    )

    val personsDF = persons.toDataFrame()

    @Test
    fun `it should unwrap columns`() {
        val dfUnfold = personsDF.unfoldPropertiesOf<City>(
            "address",
//            keep = true,
            properties = listOf("name"),
            addPrefix = true
        )

        dfUnfold.print()

        dfUnfold.apply {
            columnNames() shouldBe listOf("name", "address_name")
        }
    }

    @Test
    fun `it should unwarp columns without prefix`() {
        val dfUnfold = personsDF.unfoldPropertiesOf<City>(
            "address",
            keep = true,
            addPrefix = true
//            properties = listOf("name", "bundesLand"),
        )

        dfUnfold.print()

        dfUnfold.apply {
            columnNames() shouldBe listOf("name", "address", "address_code", "address_name")
        }
    }


//    @Test
//    fun `it should unwrap properties of object columns`() {
//        val users = dataFrameOf("department", "ids")(
//            "tech", UUID.randomUUID(),
//            "admin", UUID.randomUUID()
//        )
//
//        // todo still blocked via https://github.com/Kotlin/dataframe/issues/159
//        val unwrapped = users.unfoldByReflection<UUID>("ids", properties = listOf("variant"), keep = true)
//
//        unwrapped["variant"][1] shouldBe 2
//    }

    // Disabled because unfolding with property references is longer supported
//    @Test
//    fun `it should unwrap properties by property reference`() {
//        val users: DataFrame<*> = dataFrameOf("department", "ids")(
//            "tech", UUID.randomUUID(),
//            "admin", UUID.randomUUID()
//        )
//
//        val unwrapped = users.unfold(
//            "ids",
////            properties = listOf("variant"),
//            keep = true
//        )
//
//        // workaround
////        users.add("variant"){ "ids"<UUID>().variant() }
//
//        unwrapped["variant"][1] shouldBe 2
//    }
//



    @Test
    fun `it should only detect properties and not functions via reflection`() {
        data class Car(val type: String, val model: String){
            fun isAutomatic() = true
        }

        val cars: DataFrame<*> = dataFrameOf("owner", "car")(
            "Max", Car("audi", "a8"),
            "Tom", Car("toyota", "corolla")
        )
//        val unwrappedCore = cars.unfoldPropertiesOf<Car>("car")

//        cars.unfold<Car>()


        val unwrapped = cars.unfoldPropertiesOf<Car>(
            "car",
            properties = listOf("model"),
            keep = true,
            addPrefix = true
        )

        unwrapped.columnNames() shouldBe listOf("owner", "car", "car_model")
    }
}