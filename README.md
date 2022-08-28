# kdfutils


[ ![Download](https://img.shields.io/badge/Maven%20Central-1.0-orange) ](https://mvnrepository.com/artifact/com.github.holgerbrandl/kdutils)  [![Build Status](https://github.com/holgerbrandl/kdutils/workflows/build/badge.svg)](https://github.com/holgerbrandl/kdutils/actions?query=workflow%3Abuild) [![Gitter](https://badges.gitter.im/holgerbrandl/kdutils.svg)](https://gitter.im/holgerbrandl/kdutils?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)



> Misc utilities for kotlin-dataframe

An opinionated set of utilities that we loved in [krangl](https://github.com/holgerbrandl/krangl) are yet/initially/bydesign missing in [kotlin-dataframe](https://github.com/Kotlin/dataframe)


1. Convert any iterables to dataframe via reflection
```kotlin
data class Person(val name:String, val address:String)
val persons : List<Person> = listOf(Person("Max", "Hamburg"), Person("Julia", "Berlin"))

val personsDF: DataFrame = persons.asDataFrame() 
```

2. Unfold properties from object column
```kotlin
data class City(val name:String, val code:Int)
data class Person(val name:String, val address:City)

val persons : List<Person> = listOf(
    Person("Max", City("Dresden", 12309)),
    Person("Anna", City("Berlin", 10115))
)

val personsDF: DataFrame = persons.asDataFrame()
personsDF.unfold<City>("address") 

// or selectively via property reference
personsDF.unfold<City>("address", properties= listOf(City::name)) 
```

## Under the Hood

`kdfutils` is using bidirectional conversion from/to `krangl` to enable certain features for `kdf` (kotlin-dataframe). The idea is to replace them with direct implementations over time and potentially propose backports (via PR) them into the kdf repo.


## Gradle

To get started simply add it as a dependency:
```
dependencies {
    implementation "com.github.holgerbrandl:kdfutils:1.0"
}
```

Builds are hosted on [maven-central](https://search.maven.org/search?q=a:kalasim) supported by the great folks at [sonatype](https://www.sonatype.com/).
