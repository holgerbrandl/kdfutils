# kdfutils - Release History

## v1.4

Finally, we have removed dependency from [krangl](https://github.com/holgerbrandl/krangl). Now kdfutils simply enhances the  API of https://github.com/Kotlin/dataframe

Added

* New Functionality: Introduced a spread function for reshaping dataframes, enabling the transformation from long to wide formats.
* Excel Module: Created additional scripts and configurations, including test resources and a new README for the kdfutils-excel module.

Changed

* Module Naming: Corrected the Excel module name to kdfutils-excel to address a typo.
* Build System: Transitioned the build script from Groovy to Kotlin DSL for streamlined configuration and better maintainability.
* Removed dependency from krangl

Updated

* Dependencies: Updated build.gradle.kts to use api dependency for dataframe-core, ensuring proper referencing and modularity.

## v1.2

* removed krangl dependencies from internalized data-sets
* Added renaming utilizes clean up column names, and to toggle column names between came, kebap and snake case
* 
## v1.1



## v1.0

Initial Release



