# kdfutils


[ ![Download](https://img.shields.io/badge/Maven%20Central-1.4.5-orange) ](https://mvnrepository.com/artifact/com.github.holgerbrandl/kdutils)  [![Build Status](https://github.com/holgerbrandl/kdutils/workflows/build/badge.svg)](https://github.com/holgerbrandl/kdutils/actions?query=workflow%3Abuild) [![Gitter](https://badges.gitter.im/holgerbrandl/kdutils.svg)](https://gitter.im/holgerbrandl/kdutils?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)



> Misc utilities for kotlin-dataframe

An opinionated set of utilities that we loved in [krangl](https://github.com/holgerbrandl/krangl) are yet/initially/by-design missing in [kotlin-dataframe](https://github.com/Kotlin/dataframe)


### Example datasets 

KDF lacks example data to produce reproducible examples

```kotlin
import org.jetbrains.kotlinx.dataframe.datasets

irisData.head()

sleepData.count()

flightsdata.columnNames()
```

### Bidirectional conversion between krangl and kotlin-dataframe

```kotlin
irisData.toKotlinDF().toKranglDF()
```
Note: `kdfutils` does not have a runtme dependency on krangl. It's up to the user to add it if bi-directional conversion is needed

### Extended unfold support

KDF supports an API to unfold  object columns. However, as it still lacks some convenience, here we support of properties from object column with more control over the unfolding process (optionally keep original column, cherry which attributes to unfold, add prefix)
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
personsDF.unfold<City>("address", properties= listOf(City::name), keep = true, addPrefix = true ) 
```

### Naming Conventions & Conversions

There are many ways to name columns. To ease the transition (between camel, snake, ..) and to create names complying with compiler conventions, this library provides some renaming utilities

Typically, this works best by first renaming columns to camel case


## Gradle

To get started, simply add it as a dependency:
```
dependencies {
    implementation "com.github.holgerbrandl:kdfutils:1.4.5"
}
```
Note that kdfutils does not depend on `krangl` any longer

Builds are hosted on [maven-central](https://search.maven.org/search?q=a:kdfutils) supported by the great folks at [sonatype](https://www.sonatype.com/).