# Java Module plugin

## Intention

1. Simple configure JVM version
1. Default enable Lombok support
1. Default enable checkstyle support

## Sample configurations
##### JVM configurations
using JVM target version if not set property 'javaVersion'

* [set Java 7](src/test/resources/javaVersion7.build.gradle)
* [set Java 8](src/test/resources/javaVersion8.build.gradle)

##### Checkstyle configurations

default configuration path: `${rootPath}/config/checkstyle.xml` (If the file is not found in the specified path, the plugin "checkstyle" not applicable)

* [Checkstyle latest version](src/test/resources/checkstyleLatestVersion.build.gradle)
* [Checkstyle specified version](src/test/resources/checkstyleSpecifiedVersion.build.gradle)
* [Checkstyle specified version (Use checkstyle extension)](src/test/resources/checkstyleSpecifiedVersionUsingPlugin.build.gradle)
* [disable Checkstyle](src/test/resources/checkstyleDisabled.build.gradle)

##### Lombok configurations
* [Lombok latest version](src/test/resources/lombokLatestVersion.build.gradle)
* [Lombok specified version](src/test/resources/lombokSpecifiedVersion.build.gradle)
* [disable Checkstyle](src/test/resources/lombokDisabled.build.gradle)

