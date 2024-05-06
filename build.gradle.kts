import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "de.jackBeBack"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

val koin_version = "3.5.6"
dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    api("com.apple:AppleJavaExtensions:1.4")
    implementation(platform("io.insert-koin:koin-bom:$koin_version"))
    implementation("io.insert-koin:koin-core")
    //persistent key-value data
    implementation("com.russhwolf:multiplatform-settings:1.1.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Konnect"
            packageVersion = "1.0.0"
        }

        jvmArgs("--add-opens", "java.desktop/com.apple.eawt.event=ALL-UNNAMED")
    }
}

tasks.withType<JavaCompile> {
    val compileArgs = options.compilerArgs
    compileArgs.add("-XDignore.symbol.file")
    compileArgs.add("--add-exports java.desktop/com.apple.eawt.event=ALL-UNNAMED")
}
