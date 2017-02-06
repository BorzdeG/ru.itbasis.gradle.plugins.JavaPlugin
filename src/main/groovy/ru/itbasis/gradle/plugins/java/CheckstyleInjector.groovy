package ru.itbasis.gradle.plugins.java

import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin

class CheckstyleInjector {
	public static final String PROPERTY_USE_CHECKSTYLE     = 'useCheckstyle'
	public static final String PROPERTY_CHECKSTYLE_VERSION = 'checkstyleVersion'

	public static final String MESSAGE_CHECKSTYLE_DISABLED  = 'Checkstyle: disabled'
	public static final String MESSAGE_CHECKSTYLE_NOT_FOUND = 'Checkstyle: not found config file "{}"'

	static void applyCheckStyle(ProjectInternal project) {
		if (project.plugins.hasPlugin(CheckstylePlugin)) {
			return
		}

		final logger = project.logger

		final checkstyleEnabled = project.hasProperty(PROPERTY_USE_CHECKSTYLE) ? project.property(PROPERTY_USE_CHECKSTYLE) : true
		if (!checkstyleEnabled) {
			logger.info(MESSAGE_CHECKSTYLE_DISABLED)
			return
		}

		final configFile = project.rootProject.file('config/checkstyle.xml')
		if (!configFile.exists()) {
			logger.warn(MESSAGE_CHECKSTYLE_NOT_FOUND, configFile.path)
			logger.info(MESSAGE_CHECKSTYLE_DISABLED)
			return
		}

		project.plugins.apply(CheckstylePlugin)

		final checkstyleExtension = project.extensions.getByType(CheckstyleExtension)
		checkstyleExtension.toolVersion = (project.properties[PROPERTY_CHECKSTYLE_VERSION] ?: JavaModulePlugin.VERSION_LATEST_RELEASE) as String
		checkstyleExtension.configFile = configFile
		checkstyleExtension.showViolations = true
	}
}
