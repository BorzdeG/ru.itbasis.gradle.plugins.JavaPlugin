package ru.itbasis.gradle.plugins.java

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile

class JavaModulePlugin implements Plugin<ProjectInternal> {
	public static final String PROPERTY_JAVA_VERSION            = 'javaVersion'
	public static final String EXCEPTION_NOT_FOUND_JAVA_VERSION = 'Could not find property \'' + PROPERTY_JAVA_VERSION +
	                                                              '\'. using runtime JVM version'


	public static final String VERSION_LATEST_RELEASE = 'latest.release'

	@Override
	void apply(ProjectInternal project) {
		project.plugins.apply(JavaPlugin)
		configurateResolutions(project)

		project.afterEvaluate({
			applyJava(project)
			CheckstyleInjector.applyCheckStyle(project)
			LombokInjector.applyLombok(project)

			project.dependencies {
				testCompile('org.powermock:powermock-module-junit4:latest.release')
				testCompile('org.powermock:powermock-module-junit4-rule:latest.release')
			}
		})

	}

	private static configurateResolutions(ProjectInternal project) {
		project.configurations.all {
			resolutionStrategy {
				failOnVersionConflict()

				force 'org.objenesis:objenesis:latest.release'
			}
		}
	}

	private static void applyJava(ProjectInternal project) {
		final JavaVersion javaVersion

		if (!project.hasProperty(PROPERTY_JAVA_VERSION)) {
			project.logger.info(EXCEPTION_NOT_FOUND_JAVA_VERSION)
			javaVersion = JavaVersion.toVersion(System.getProperty("java.specification.version"))

		} else {
			javaVersion = JavaVersion.toVersion(project.property(PROPERTY_JAVA_VERSION))
		}

		project.tasks.withType(JavaCompile) { task ->
			task.sourceCompatibility = javaVersion.toString()
			task.targetCompatibility = javaVersion.toString()
		}
	}

}
