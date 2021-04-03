Please add ' maven { url "https://jitpack.io" } ' into repositories{} in allprojects in build.gradle (Project) before test codes

EX:  Directory: Gradle Scripts > build.gradle (Project)
  allprojects {
      repositories {
          google()
          jcenter()
          maven { url "https://jitpack.io" }
      }
  }
  
  Please add ' implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0 ' into dependencies{} in build.gradle (Module) before test codes
  Ex: Directory: Gradle Scripts > build.gradle (Module)
    dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

}
