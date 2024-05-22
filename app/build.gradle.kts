plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    //plugin de Sonarqube, para checkear la seguridad
    id("org.sonarqube") version "5.0.0.4638"
}

android {
    namespace = "com.example.proyectofinalmovil"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyectofinalmovil"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding{
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Corutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //Retrofit 2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    //Activity
    implementation("androidx.activity:activity-ktx:1.8.2")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //Material3 para la interfaz
    implementation("com.google.android.material:material:1.12.0")
    //Blurry para el fondo del login
    implementation("jp.wasabeef:blurry:4.0.1")
    //Espresso para los tests
    implementation("androidx.test.espresso:espresso-core:3.5.1")
}

