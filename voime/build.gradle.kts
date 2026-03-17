// use appropriate version of Gradle, JDK, NDK, and AGP
// for a particular version of Android Studio
// e.g. Narwhal 4 Feature Drop | 2025.1.4
// - Gradle >= 8.13
// - BUILDTOOLS >= 35.0.1
// - JDK >= 17
// - NDK >= N/A (27.1.12297006)
// - CMAKE >= 4.1.1
// - AGP >= 8.13.0
//   https://developer.android.com/build/releases/gradle-plugin#compatibility

// keep Gradle wrapper in sync with AGP
// ./gradlew wrapper --gradle-version 8.13
// https://docs.gradle.org/current/userguide/gradle_wrapper.html#sec:upgrading_wrapper

// Android SDK and JVM compatibility considerations:
// - Oboe with AAudio API requires at least SDK 27 (consider as minSdk, see also issue #33)
// - Infinix Note 40 is our main device running Android 15 (consider as targetSdk)
// - set compileSdk as high as required by project dependencies
// - set Java compatibility version according to
//   https://developer.android.com/build/jdks
//   https://developer.android.com/studio/write/java8-support

// UPDATE: SDK diatur ke 35 untuk target dan compile
val sdk by extra(intArrayOf(33, 35, 35)) // minSdk <= targetSdk <= compileSdk
val jvm by extra(17) // sourceCompatibility == targetCompatibility == jvmTarget

// release version information
val releaseVersionName by extra("3.0")
val releaseVersionCode by extra(13)

plugins {
  alias(libs.plugins.android.gradle.plugin)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.jetbrains.kotlin.compose)
}

android {
  namespace = "ran.day.voime"
  
  // UPDATE: Deklarasi eksplisit Build Tools dan NDK agar tidak download ulang
  buildToolsVersion = "35.0.1"
  ndkVersion = "27.1.12297006"

  defaultConfig {
    applicationId = "ran.day.voime"
    versionName = releaseVersionName
    versionCode = releaseVersionCode
    minSdk = sdk[0]
    targetSdk = sdk[1]
    ndk {
      // restrict ABIs as supplied by Oboe
      // see https://github.com/google/oboe/blob/main/build_all_android.sh
      abiFilters += listOf("arm64-v8a")
    }
    externalNativeBuild {
      cmake {
        // restrict ABIs as supplied by Oboe
        // see https://github.com/google/oboe/blob/main/build_all_android.sh
        abiFilters += listOf("arm64-v8a")
        // force shared library variant of libc++ as required by Oboe
        arguments += listOf("-DANDROID_STL=c++_shared")
        // enable release build and thus runtime optimizations by default
        arguments += listOf("-DCMAKE_BUILD_TYPE=Release")
        // redirect CPM.cmake cache to avoid re-downloading dependencies for each ABI
        arguments += listOf("-DCPM_SOURCE_CACHE=${project.projectDir}/.cpm")
      }
    }
  }

  compileSdk = sdk[2]
  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(jvm)
    targetCompatibility = JavaVersion.toVersion(jvm)
  }
  kotlinOptions {
    // consider jvmTarget as non-officially deprecated
    // https://stackoverflow.com/q/77363060
    // https://youtrack.jetbrains.com/issue/KT-27301#focus=Comments-27-6565858.0-0
    @Suppress("DEPRECATION")
    jvmTarget = jvm.toString()
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      isShrinkResources = false
    }
  }

  buildFeatures {
    // enable compose UI feature
    compose = true
    // enable import of prefab dependencies as required by Oboe
    prefab = true
  }

  externalNativeBuild {
    cmake {
      path = file("src/main/cpp/ran/day/voime/CMakeLists.txt")
      // UPDATE: Deklarasi eksplisit versi CMake (sesuaikan dengan versi yang sudah terinstal di environment Anda, standar Android biasanya 3.22.1)
      version = "4.1.1" 
    }
  }
}

dependencies {
  implementation(libs.androidx.activity)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.preference)
  implementation(libs.jetbrains.compose.runtime)
  implementation(libs.jna) { artifact { type = "aar" } } // don't remove aar artifact!
  implementation(libs.oboe)
}
