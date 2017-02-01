package ru.itbasis.gradle.plugins.java

import org.gradle.api.InvalidUserDataException
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.tasks.compile.JavaCompile

class JavaModulePlugin implements Plugin<ProjectInternal> {
	public static final String PROPERTY_JAVA_VERSION            = 'javaVersion'
	public static final String EXCEPTION_NOT_FOUND_JAVA_VERSION = 'Could not find property \'' + PROPERTY_JAVA_VERSION + '\''

	public static final String PROPERTY_USE_LOMBOK     = 'useLombok'
	public static final String PROPERTY_LOMBOK_VERSION = 'lombokVersion'
	public static final String MESSAGE_LOMBOK_DISABLED = 'Lombok: disabled'

	public static final String PROPERTY_USE_CHECKSTYLE     = 'useCheckstyle'
	public static final String PROPERTY_CHECKSTYLE_VERSION = 'checkstyleVersion'
	public static final String MESSAGE_CHECKSTYLE_DISABLED = 'Checkstyle: disabled'

	public static final String VERSION_LATEST_RELEASE = 'latest.release'

	@Override
	void apply(ProjectInternal project) {
		project.afterEvaluate({
			applyJava(project)
			applyCheckStyle(project)
			applyLombok(project)
		})
	}

	private static void applyJava(ProjectInternal project) {
		project.plugins.apply(JavaPlugin)

		if (!project.hasProperty(PROPERTY_JAVA_VERSION)) {
			throw new InvalidUserDataException(EXCEPTION_NOT_FOUND_JAVA_VERSION)
		}

		final JavaVersion javaVersion = JavaVersion.toVersion(project.property(PROPERTY_JAVA_VERSION));

		project.tasks.withType(JavaCompile) { task ->
			task.sourceCompatibility = javaVersion.toString()
			task.targetCompatibility = javaVersion.toString()
		}
	}

	private static void applyCheckStyle(ProjectInternal project) {
		if (project.plugins.hasPlugin(CheckstylePlugin)) {
			return
		}

		final checkstyleEnabled = project.hasProperty(PROPERTY_USE_CHECKSTYLE) ? project.property(PROPERTY_USE_CHECKSTYLE) : true
		if (!checkstyleEnabled) {
			project.logger.info(MESSAGE_CHECKSTYLE_DISABLED)
			return
		}

		project.plugins.apply(CheckstylePlugin)

		final checkstyleExtension = project.extensions.getByType(CheckstyleExtension)
		checkstyleExtension.toolVersion = project.properties[PROPERTY_CHECKSTYLE_VERSION] ?: VERSION_LATEST_RELEASE
		checkstyleExtension.configFile = project.rootProject.file('config/checkstyle.xml')
		checkstyleExtension.showViolations = true
	}

	private static void applyLombok(ProjectInternal project) {
		final lombokEnabled = project.hasProperty(PROPERTY_USE_LOMBOK) ? project.property(PROPERTY_USE_LOMBOK) : true
		if (!lombokEnabled) {
			project.logger.info(MESSAGE_LOMBOK_DISABLED)
			return
		}

		final String lombokVersion = project.properties[PROPERTY_LOMBOK_VERSION] ?: VERSION_LATEST_RELEASE
		project.dependencies {
			compileOnly 'org.projectlombok:lombok:' + lombokVersion
		}
	}

}
