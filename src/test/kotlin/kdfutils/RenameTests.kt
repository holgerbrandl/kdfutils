package kdfutils

import com.github.holgerbrandl.kdfutils.renameToCamelCase
import com.github.holgerbrandl.kdfutils.renameToKebapCase
import com.github.holgerbrandl.kdfutils.renameToSnakeCase
import com.github.holgerbrandl.kdfutils.renameToSpaces
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.junit.jupiter.api.Test

class RenameTests {

    @Test
    fun `it should allow to create a working schema for iris data`(){
        val df = dataFrameOf("foo-bar", "name 2", "prop(percent)", "dot.case", "camelCase", "snake_case")(
            "1", "2", "3", "4", "5", "6"
        )

        df.renameToSnakeCase().columnNames() shouldBe listOf("foo_bar", "name_2", "prop_percent", "dot_case", "camel_case", "snake_case")

        df.renameToCamelCase().columnNames()  shouldBe listOf("fooBar", "name2", "propPercent", "dotCase", "camelCase", "snakeCase")

        df.renameToKebapCase().columnNames()  shouldBe listOf("foo-bar", "name-2", "prop-percent", "dot-case", "camel-case", "snake-case")

        df.renameToSpaces().columnNames()  shouldBe listOf("Foo Bar", "Name 2", "Prop Percent", "Dot Case", "Camel Case", "Snake Case")
    }
}