@file:Suppress("PackageDirectoryMismatch")

package org.jetbrains.kotlinx.dataframe.datasets

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.map
import org.jetbrains.kotlinx.dataframe.io.CSVType
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.dataframe.io.readDelim
import org.jetbrains.kotlinx.dataframe.io.readTSV
import java.io.File
import java.net.URL


/**
An example data frame with 83 rows and 11 variables

This is an updated and expanded version of the mammals sleep dataset. Updated sleep times and weights were taken from V. M. Savage and G. B. West. A quantitative, theoretical framework for understanding mammalian sleep. Proceedings of the National Academy of Sciences, 104 (3):1051-1056, 2007.

Additional variables order, conservation status and vore were added from wikipedia.
- name. common name
- genus.
- vore. carnivore, omnivore or herbivore?
- order.
- conservation. the conservation status of the animal
- sleep_total. total amount of sleep, in hours
- sleep_rem. rem sleep, in hours
- sleep_cycle. length of sleep cycle, in hours
- awake. amount of time spent awake, in hours
- brainwt. brain weight in kilograms
- bodywt. body weight in kilograms
 */
val sleepData by lazy {
    val reader = DataFrame::class.java.getResourceAsStream("/data/msleep.csv")
    DataFrame.readDelim(reader!!, csvType = CSVType.DEFAULT)
}


/* Data class required to parse sleep Data records. */
data class SleepPattern(
    val name: String,
    val genus: String,
    val vore: String?,
    val order: String,
    val conservation: String?,
    val sleep_total: Double,
    val sleep_rem: Double?,
    val sleep_cycle: Double?,
    val awake: Double,
    val brainwt: Double?,
    val bodywt: Double
)

val sleepPatterns by lazy {
    sleepData.map { row ->
        SleepPattern(
            row["name"] as String,
            row["genus"] as String,
            row["vore"] as String?,
            row["order"] as String,
            row["conservation"] as String?,
            row["sleep_total"] as Double,
            row["sleep_rem"] as Double?,
            row["sleep_cycle"] as Double?,
            row["awake"] as Double,
            row["brainwt"] as Double?,
            row["bodywt"] as Double
        )
    }
}


/**
 * This famous (Fisher's or Anderson's) iris data set gives the measurements in centimeters
 * of the variables sepal length and width and petal length and width, respectively, for 50
 * flowers from each of 3 species of iris. The species are Iris setosa, versicolor, and virginica.
 *
 * ## Format
 *
 * iris is a data frame with 150 cases (rows) and 5 variables (columns) named `Sepal.Length`, `Sepal.Width`, `Petal.Length`, `Petal.Width`, and `Species`.
 *
 * ## Source
 * Fisher, R. A. (1936) The use of multiple measurements in taxonomic problems. Annals of Eugenics, 7, Part II, 179–188.
 *
 * The data were collected by Anderson, Edgar (1935). The irises of the Gaspe Peninsula, Bulletin of the American Iris Society, 59, 2–5.
 *
 * ## References
 * Becker, R. A., Chambers, J. M. and Wilks, A. R. (1988) The New S Language. Wadsworth & Brooks/Cole. (has iris3 as iris.)
 *
 *
 */
val irisData by lazy {
    DataFrame.readTSV(
        SleepPattern::class.java.getResourceAsStream("/data/iris.txt")!!,
    )
}


/**
On-time data for all 336776 flights that departed NYC (i.e. JFK, LGA or EWR) in 2013.

Adopted from r, see `nycflights13::flights`
 */


internal val cacheDataDir by lazy {
    File(System.getProperty("user.home"), ".krangl_example_data").apply { if (!isDirectory()) mkdir() }
}

internal val flightsCacheFile = File(cacheDataDir, ".flights_data.tsv.gz")

/**
 * On-time data for all flights that departed NYC (i.e. JFK, LGA or EWR) in 2013.
 *
 * * `year`, `month`,day: Date of departure
 * * `dep_time`, `arr_time`: Actual departure and arrival times, local tz.
 * * `sched_dep_time`, `sched_arr_time`: Scheduled departure and arrival times, local tz.
 * * `dep_delay`, `arr_delay`: Departure and arrival delays, in minutes. Negative times represent early departures/arrivals.
 * * `hour`, `minute`: Time of scheduled departure broken into hour and minutes.
 * * `carrier`: Two letter carrier abbreviation. See airlines to get name
 * * `tailnum`: Plane tail number
 * * `flight`: Flight number
 * * `origin`,dest: Origin and destination. See airports for additional metadata.
 * * `air_time`: Amount of time spent in the air, in minutes
 * * `distance`: Distance between airports, in miles
 * * `time_hour`: Scheduled date and hour of the flight as a POSIXct date. Along with origin, can be used to join flights data to weather data.
 *
 *
 * ### Source
 *
 * * RITA, Bureau of transportation statistics, http://www.transtats.bts.gov/DL_SelectFields.asp?Table_ID=236
 * * https://github.com/hadley/nycflights13
 */
val flightsData by lazy {

    if (!flightsCacheFile.isFile) {
        println("[krangl] Downloading flights data into local cache...")
        val flightsURL =
            URL("https://github.com/holgerbrandl/krangl/blob/v0.4/src/test/resources/krangl/data/nycflights.tsv.gz?raw=true")
        println("Done!")


        //    for progress monitoring use
        //    https@ //stackoverflow.com/questions/12800588/how-to-calculate-a-file-size-from-url-in-java

        flightsCacheFile.writeBytes(flightsURL.readBytes())
    }


    DataFrame.readTSV(flightsCacheFile)

    // consider to use progress bar here
}