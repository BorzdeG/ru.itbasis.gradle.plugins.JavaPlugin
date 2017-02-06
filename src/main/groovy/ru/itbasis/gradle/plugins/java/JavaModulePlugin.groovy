package ru.itbasis.gradle.plugins.java

import org.gradle.api.InvalidUserDataException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.compile.JavaCompile

class JavaModulePlugin implements Plugin<ProjectInternal> {
	public static final String PROPERTY_JAVA_VERSION            = 'javaVersion'
	public static final String EXCEPTION_NOT_FOUND_JAVA_VERSION = 'Could not find property \'' + PROPERTY_JAVA_VERSION + '\''


	public static final String VERSION_LATEST_RELEASE = 'latest.release'

	@Override
	void apply(ProjectInternal project) {
		project.afterEvaluate({
			applyJava(project)
			CheckstyleInjector.applyCheckStyle(project)
			LombokInjector.applyLombok(project)
		})
	}

	private static void applyJava(ProjectInternal project) {
		project.plugins.apply(JavaPlugin)

		if (!project.hasProperty(PROPERTY_JAVA_VERSION)) {
			throw new InvalidUserDataException(EXCEPTION_NOT_FOUND_JAVA_VERSION)
		}

		final JavaVersion javaVersion = JavaVersion.toVersion(project.property(PROPERTY_JAVA_VERSION))

		project.tasks.withType(JavaCompile) { task ->
			task.sourceCompatibility = javaVersion.toString()
			task.targetCompatibility = javaVersion.toString()
		}
	}

}
