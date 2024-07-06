import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinx.serialization)
    id("kotlin-kapt")
}

class SecretsFetcher {
    private val secretProperties: Properties by lazy {
        Properties().apply {
            rootProject.file("secrets.properties")
                .takeIf { it.exists() }
                ?.let {
                    load(FileInputStream(it))
                }
        }
    }

    operator fun get(key: String): String = secretProperties[key]?.toString() ?: System.getenv(key)

    val clickUpClientId: String
        get() = this["clickup.client.id"]
    val clickUpClientSecret: String
        get() = this["clickup.client.secret"]
    val keystoreKeyAlias: String
        get() = this["app.keystore.keyAlias"]
    val keystoreKeyPassword: String
        get() = this["app.keystore.keyPassword"]
    val keystoreStorePassword: String
        get() = this["app.keystore.storePassword"]
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
            storeFile = rootProject.file("life-suite.keystore")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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