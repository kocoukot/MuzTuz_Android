plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.muztus.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 27
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.kotlinCompilerExtensionVersion
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2022.10.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.toolingPreview)
    implementation(Dependencies.Compose.toolingUi)
    implementation(Dependencies.Compose.animation)

    implementation(Dependencies.AndroidX.fragment)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.activity)

    implementation(Dependencies.Navigation.fragment)
    implementation(Dependencies.Navigation.ui)

    implementation(Dependencies.Koin.android)

}