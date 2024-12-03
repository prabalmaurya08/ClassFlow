plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.gms.google-services")
  id ("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.classflow"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.classflow"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation(libs.androidx.navigation.fragment.ktx.v260)
    implementation (libs.androidx.navigation.ui.ktx.v260)

    //for chart
    implementation(libs.mpandroidchart)

    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.kotlinx.coroutines.play.services)


}