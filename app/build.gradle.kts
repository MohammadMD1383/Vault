import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
}

android {
	namespace = "ir.mmd.androidDev.vault"
	compileSdk = 34
	
	defaultConfig {
		applicationId = "ir.mmd.androidDev.vault"
		minSdk = 26
		targetSdk = 34
		versionCode = 12
		versionName = "2.6.0"
		
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}
	
	buildTypes {
		release {
			isMinifyEnabled = true
			isShrinkResources = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
			archivesName.set("Vault-${defaultConfig.versionName}")
		}
	}
	
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_19
		targetCompatibility = JavaVersion.VERSION_19
	}
	
	kotlinOptions {
		jvmTarget = "19"
	}
	
	buildFeatures {
		compose = true
	}
	
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.9"
	}
	
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	implementation("androidx.room:room-runtime:2.6.1")
	annotationProcessor("androidx.room:room-compiler:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	implementation("androidx.fragment:fragment-ktx:1.6.2")
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
	implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
	implementation("androidx.activity:activity-compose:1.8.2")
	implementation("androidx.core:core-splashscreen:1.0.1")
	implementation("androidx.biometric:biometric:1.1.0")
	implementation(platform("androidx.compose:compose-bom:2024.02.00"))
	implementation("androidx.navigation:navigation-compose:2.7.7")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.material:material-icons-core-android")
	implementation("androidx.compose.material:material-icons-extended-android")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}