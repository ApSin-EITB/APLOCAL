plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "ru.apsin.aplocal"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.apsin.aplocal"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        externalNativeBuild {
            cmake {
               // arguments.add("-DGO_WG_LIB=${projectDir}/src/main/jniLibs/arm64-v8a/libwg-go.so")
            }
        }

        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.appcompat.v161)
}

kotlin {
    jvmToolchain(21) // Kotlin компилятор под Java 21
}
