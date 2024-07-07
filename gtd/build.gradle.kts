import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    id("kotlin-kapt")
    alias(libs.plugins.ktlint)
}

class SecretsFetcher {
    private val secretProperties: Properties by lazy {
        Properties().apply {
            rootProject
                .file("secrets.properties")
                .takeIf { it.exists() }
                ?.let {
                    load(FileInputStream(it))
                }
        }
    }

    private operator fun get(key: String): String = secretProperties[key]?.toString() ?: requireNotNull(System.getenv(key))

    val clickUpClientId: String
        get() = this["CLICKUP_CLIENT_ID"]
    val clickUpClientSecret: String
        get() = this["CLICKUP_CLIENT_SECRET"]
    val keystoreKeyAlias: String
        get() = this["APP_KEYSTORE_KEY_ALIAS"]
    val keystoreKeyPassword: String
        get() = this["APP_KEYSTORE_KEY_PASSWORD"]
    val keystoreStorePassword: String
        get() = this["APP_KEYSTORE_STORE_PASSWORD"]
}

android {
    namespace = "fyi.atom.lifesuite"
    compileSdk = 34

    defaultConfig {
        applicationId = "fyi.atom.lifesuite"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val secretsFetcher = SecretsFetcher()
        buildConfigField("String", "CLICKUP_CLIENT_ID", "\"${secretsFetcher.clickUpClientId}\"")
        buildConfigField("String", "CLICKUP_CLIENT_SECRET", "\"${secretsFetcher.clickUpClientSecret}\"")
    }

    signingConfigs {
        getByName("debug") {
            val secretsFetcher = SecretsFetcher()
            keyAlias = secretsFetcher.keystoreKeyAlias
            keyPassword = secretsFetcher.keystoreKeyPassword
            storeFile = rootProject.file("life-suite.debug")
            storePassword = secretsFetcher.keystoreStorePassword
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.ktor.android)
    implementation(libs.ktor.core)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.serialization)
    implementation(libs.timber)
    implementation(libs.treessence)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.process)
    kapt(libs.hilt.compiler)
    implementation(libs.flowredux.jvm)
    implementation(libs.appauth)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.navigation.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set("1.3.1")
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
//    baseline.set(file("my-project-ktlint-baseline.xml"))
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.HTML)
    }
    kotlinScriptAdditionalPaths {
        include(fileTree("scripts/"))
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    baseline = file("$projectDir/config/detekt/baseline.xml")
}
