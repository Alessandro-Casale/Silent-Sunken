import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    `java-library`
    `maven-publish`
    id("net.neoforged.moddev") version "2.0.142"
    idea
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}

val mod_id: String by project
val mod_name: String by project
val mod_license: String by project
val mod_version: String by project
val mod_group_id: String by project
val minecraft_version: String by project
val minecraft_version_range: String by project
val neo_version: String by project

version = mod_version
group = mod_group_id

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")

            exclude("**/*.bbmodel")
            exclude("src/generated/**/.cache")
        }
    }
}

repositories { }

base {
    archivesName.set(mod_id)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(25))

neoForge {
    version = neo_version

    runs {
        create("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("data") {
            clientData()

            programArguments.addAll(
                "--mod", mod_id,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")

            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

configurations {
    val localRuntime = create("localRuntime")
    named("runtimeClasspath") {
        extendsFrom(localRuntime)
    }
}

dependencies {
    // Example optional mod dependency with JEI
    // The JEI API is declared for compile time use, while the full JEI artifact is used at runtime
    // compileOnly("mezz.jei:jei-${mc_version}-common-api:${jei_version}")
    // compileOnly("mezz.jei:jei-${mc_version}-neoforge-api:${jei_version}")
    // We add the full version to localRuntime, not runtimeOnly, so that we do not publish a dependency on it
    // localRuntime "mezz.jei:jei-${mc_version}-neoforge:${jei_version}"

    // Example mod dependency using a mod jar from ./libs with a flat dir repository
    // This maps to ./libs/coolmod-${mc_version}-${coolmod_version}.jar
    // The group id is ignored when searching -- in this case, it is "blank"
    // implementation("blank:coolmod-${mc_version}:${coolmod_version}")

    // Example mod dependency using a file as dependency
    // implementation(files("libs/coolmod-${mc_version}-${coolmod_version}.jar"))

    // Example project dependency using a sister or child project:
    // implementation(project(":myproject"))

    // For more info:
    // http://www.gradle.org/docs/current/userguide/artifact_dependencies_tutorial.html
    // http://www.gradle.org/docs/current/userguide/dependency_management.html
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "neo_version" to neo_version,
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_license" to mod_license,
        "mod_version" to mod_version
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri(file("repo"))
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

publishMods {
    file.set(tasks.jar.flatMap { it.archiveFile })
    modLoaders.add("neoforge")
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formattedDate: String = today.format(formatter)
    val changelogFile = layout.projectDirectory.file("CHANGELOG.md")
    val formattedVersion = mod_version.substringBeforeLast("-")

    when {
        mod_version.contains("alpha", true) -> {
            type.set(ALPHA)
            changelog.set(
                """
                        ## [$formattedVersion] - $formattedDate
                        This is an alpha version meant to be used only by developers!   
                        Changelog can be found in Discord server.
                    """.trimIndent()
            )
        }
        mod_version.contains("beta", true) -> {
            type.set(BETA)
            changelog.set(
                """
                        ## [$formattedVersion] - $formattedDate
                        This is a beta version meant to be used only by developers!   
                        Changelog can be found in Discord server.
                    """.trimIndent()
            )
        }
        else -> {
            type.set(STABLE)
            changelog.set(providers.fileContents(changelogFile).asText.orElse("No changelog provided."))
        }
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set("Alessandro-Casale/Silent-Sunken")
        val version = mod_version.substringBeforeLast("-")
        val branch = mod_version.substringAfterLast("-")
        commitish.set(branch.toMcRange())
        tagName.set("v$mod_version")

        displayName.set("Silent Sunken $mod_version")

        announcementTitle.set("Download from GitHub")
    }

    curseforge {
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))
        projectId.set("1669183")
        minecraftVersions.add(minecraft_version)
        changelogType.set("markdown")
        clientRequired = true
        serverRequired = true

        displayName.set("silentsunken-$mod_version")

        projectSlug.set("silent-sunken") // For discord setup
        announcementTitle.set("Download from CurseForge") // For discord setup
    }

//    modrinth {
//        accessToken.set(providers.environmentVariable("MODRINTH_API_KEY"))
//        projectId.set("6wy8fmIk")
//        minecraftVersions.add(minecraft_version)
//        optional(
//            "rei", "jei", "kubejs",
//            "in-control", "jade"
//        )
//
//        displayName.set("astages-$mod_version")
//
//        if (type.get() == ReleaseType.STABLE) {
//            changelog.set(
//                providers.fileContents(changelogFile)
//                    .asText
//                    .map { it.lineSequence().drop(3).joinToString("\n") }
//            )
//        } else {
//            changelog.set(changelog.get().dropFirstLine())
//        }
//
//        announcementTitle.set("Download from Modrinth")
//    }

//    discord {
//        webhookUrl.set(providers.environmentVariable("DISCORD_WEBHOOK"))
//        username.set("AServer")
//        avatarUrl.set(logoLocation)
//        content.set(changelog)
//        setPlatforms(publishMods.platforms["curseforge"], publishMods.platforms["modrinth"])
//
//        style {
//            thumbnailUrl = logoLocation
//            look = "MODERN"
//            link = "BUTTON"
//        }
//    }
}

fun String.toMcRange(): String {
    return this.substringBeforeLast(".") + ".X"
}

fun String.dropFirstLine(): String {
    return lines().drop(1).joinToString("\n")
}