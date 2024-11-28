import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    alias(libs.plugins.googleGmsGoogleServices)
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization")
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.example.comprinhas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.comprinhas"
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "0.9.8"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationVariants.all {
                outputs.all {
                    (this as BaseVariantOutputImpl).outputFileName = "${namespace}_$name-$versionName.apk"
                }
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.10"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    dependencies {
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.okhttp)
        implementation(libs.kotlinx.serialization.json)

        implementation(libs.play.services.base)
        implementation(libs.play.services.code.scanner)

        implementation(libs.androidx.material.icons.extended)

        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.work.runtime.ktx)
        implementation(libs.material)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
    }
}
dependencies {
    // Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.coil.compose)
    ksp(libs.hilt.android.compiler)

    // Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.credentials.play.services.auth)

    // Firebase
    implementation(libs.firebase.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore.ktx)

    // Datastore
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)

    // Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}
