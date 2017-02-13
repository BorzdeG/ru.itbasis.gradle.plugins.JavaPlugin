package ru.itbasis.gradle.plugins.java

import org.gradle.api.internal.project.ProjectInternal

class LombokInjector {
	public static final String PROPERTY_USE_LOMBOK     = 'useLombok'
	public static final String PROPERTY_LOMBOK_VERSION = 'lombokVersion'
	public static final String MESSAGE_LOMBOK_DISABLED = 'Lombok: disabled'

	static void applyLombok(ProjectInternal project) {
		final lombokEnabled = project.hasProperty(PROPERTY_USE_LOMBOK) ? project.property(PROPERTY_USE_LOMBOK) : true
		if (!lombokEnabled) {
			project.logger.info(MESSAGE_LOMBOK_DISABLED)
			return
		}

		final String lombokVersion = project.properties[PROPERTY_LOMBOK_VERSION] ?: JavaModulePlugin.VERSION_LATEST_RELEASE
		project.dependencies {
			compile('org.projectlombok:lombok:' + lombokVersion)
		}
	}

}
