plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}
ext {
    // Define the version of androidx.appcompat library
    appCompatVersion = "1.4.0" // or the latest version available
}
var appCompatVersion = "1.4.0" // or the latest version available

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Import the BoM for the Firebase platform
    implementation ("com.google.firebase:firebase-firestore:23.0.3")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation ("androidx.biometric:biometric:1.0.1")
    implementation ("com.squareup.picasso:picasso:2.71828") // or the latest version
    implementation ("androidx.preference:preference-ktx:1.1.1")
    implementation ("androidx.appcompat:appcompat:$appCompatVersion")
    implementation ("androidx.annotation:annotation:1.7.1") // or the version you are using
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation ("com.google.code.gson:gson:2.8.8") // Or the latest version

    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database")
     // or the latest version
    implementation ("com.github.bumptech.glide:glide:4.14.0")
    implementation("androidx.preference:preference:1.2.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("com.android.support:recyclerview-v7:28.0.0") // or the version you are using
    implementation ("androidx.recyclerview:recyclerview:1.0.0'")// or the version you are using


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.android.material:material:1.3.0-alpha03")


    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.4.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.4.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")



}



configurations.all {
    resolutionStrategy {
        eachDependency {
            if ((requested.group == "org.jetbrains.kotlin") && (requested.name.startsWith("kotlin-stdlib"))) {
                useVersion("1.8.0")
            }
        }
    }
}