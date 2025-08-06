import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  id(libs.plugins.android.application.get().pluginId)
  id(libs.plugins.kotlin.android.get().pluginId)
  id(libs.plugins.compose.compiler.get().pluginId)
  id("com.google.devtools.ksp")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
  namespace = "io.getstream.webrtc.sample.compose"
  compileSdk = Configurations.compileSdk

  defaultConfig {
    applicationId = "io.getstream.webrtc.sample.compose"
    minSdk = Configurations.minSdk
    targetSdk = Configurations.targetSdk
    versionCode = Configurations.versionCode
    versionName = Configurations.versionName

    buildConfigField(
      "String",
      "SIGNALING_SERVER_IP_ADDRESS",
      localProperties["SIGNALING_SERVER_IP_ADDRESS"].toString()
    )

//    ndk {
//      abiFilters.add("arm64-v8a")
//    }
  }


//  buildTypes {
//    release {
//      isMinifyEnabled = true
//      isShrinkResources = true
//      proguardFiles(
//        getDefaultProguardFile("proguard-android-optimize.txt"),
//        "proguard-rules.pro"
//      )
//    }
//  }
  kotlinOptions {
    jvmTarget = "11"
  }

  buildFeatures {
    compose = true
    buildConfig = true
    viewBinding = true
  }

  packagingOptions {
    resources {
      excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
  }

  lint {
    abortOnError = false
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

dependencies {
  // compose
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.ui)

  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.foundation.layout)

  implementation(libs.androidx.compose.constraintlayout)

  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.tooling.preview)

  // image loading
  implementation(libs.landscapist.glide)

  // webrtc
  implementation(libs.webrtc)
  implementation(libs.okhttp.logging)

  // coroutines
  implementation(libs.kotlinx.coroutines.android)

  //  mlkit
  implementation(libs.mlkit.obj.detection)
  implementation(libs.mlkit.obj.detection.custom)

 // to convert frame to bitmap
  implementation(libs.libyuv.android)
 // to convert frame to bitmap
  implementation(libs.androidx.navigation.compose)

    implementation ("io.coil-kt:coil-compose:2.5.0")


  implementation("androidx.compose.material:material-icons-core:1.7.8")
  implementation ("com.google.zxing:core:3.5.1")
  implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
  implementation("com.airbnb.android:lottie-compose:4.0.0")
  implementation ("androidx.compose.material:material-icons-extended:1.6.0")
  implementation ("androidx.navigation:navigation-compose:2.7.3")

  implementation(libs.stream.log)
  implementation(libs.androidx.material3.android)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)



  // Media pipe Library/audio classification
  implementation(libs.tasks.audio)
  implementation(libs.gson)
  implementation(libs.androidx.navigation.compose.android)


  /// room db
  val room_version = "2.7.2"

  implementation("androidx.room:room-runtime:$room_version")
  ksp("androidx.room:room-compiler:$room_version")


}