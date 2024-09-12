plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.sosapp"
    compileSdk = 34
    android.buildFeatures.buildConfig = true


    defaultConfig {
        applicationId = "com.example.sosapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        debug {
            buildConfigField ( "String","BASE_URL","\"https://dummy.restapiexample.com/api/v1/\"")
        }
        release {
            isShrinkResources=false
            buildConfigField ("String","BASE_URL","\"https://dummy.restapiexample.com/api/v1/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        resources {
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.hiltAndroid)
    implementation(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    implementation (libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation (libs.cameraview)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.play.services.location)
    implementation (libs.lottie)
    implementation(libs.logging.interceptor)
    implementation(libs.stetho.okhttp3)
}