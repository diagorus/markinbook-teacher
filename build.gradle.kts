plugins {
    kotlin("js") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

group = "com.thefuh.markinbook"
version = "1.0"

repositories {
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
    maven("https://kotlin.bintray.com/kotlinx/")
    maven("https://dl.bintray.com/korlibs/korlibs")
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/cfraser/muirwik")
    maven("https://dl.bintray.com/subroh0508/maven")
}

dependencies {
    implementation(kotlin("stdlib-js"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    val ktorVersion = "1.4.2"
    implementation("io.ktor:ktor-client-js:$ktorVersion")
    implementation("io.ktor:ktor-client-json-js:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization-js:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-js:$ktorVersion")

    val kotlinVersion = "1.4.10"

    val reactVersion = "17.0.0"
    //React, React DOM + Wrappers
    implementation("org.jetbrains:kotlin-react:$reactVersion-pre.129-kotlin-$kotlinVersion")
    implementation("org.jetbrains:kotlin-react-dom:$reactVersion-pre.129-kotlin-$kotlinVersion")

    implementation(npm("react", reactVersion))
    implementation(npm("react-dom", reactVersion))

    implementation("org.jetbrains:kotlin-react-router-dom:5.2.0-pre.129-kotlin-1.4.10")
    implementation(npm("react-router-dom", "5.2.0"))

    //Kotlin Styled
    val styledVersion = "5.2.0"
    implementation("org.jetbrains:kotlin-styled:$styledVersion-pre.129-kotlin-$kotlinVersion")
    implementation(npm("styled-components", styledVersion))

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0")

    //klock: date/time
    implementation("com.soywiz.korlibs.klock:klock-js:2.0.0")

    //Material ui kotlin/js
    implementation("com.ccfraser.muirwik:muirwik-components:0.6.3")

    api(npm("@date-io/core", "^2.6.0"))
    api(npm("@date-io/date-fns", "^2.6.0"))
    api(npm("date-fns", "^2.12.0"))
    //Material ui
    implementation(npm("@material-ui/core", "4.11.0"))
    implementation(npm("@material-ui/icons", "4.9.1"))
}

kotlin {
    js {
        browser {
            webpackTask {
                cssSupport.enabled = true
            }

            runTask {
                cssSupport.enabled = true
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
        binaries.executable()
    }
}