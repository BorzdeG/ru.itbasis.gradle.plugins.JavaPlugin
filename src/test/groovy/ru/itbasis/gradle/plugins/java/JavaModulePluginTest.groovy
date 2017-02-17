package ru.itbasis.gradle.plugins.java

import org.gradle.internal.impldep.com.google.common.io.Files
import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class JavaModulePluginTest {
	@Rule
	public final TemporaryFolder testProjectDir = new TemporaryFolder()
	private      GradleRunner    gradleRunner

	@Before
	void setUp() throws Exception {
		new File(testProjectDir.root, 'src/main/java').mkdirs()

		gradleRunner = GradleRunner.create()
		                           .withDebug(true)
		                           .withProjectDir(testProjectDir.root)
		                           .withPluginClasspath()
	}

	private void copyTestBuildScript(String fileName) {
		final buildFile = new File(this.class.classLoader.getResource(fileName).toURI())
		Files.copy(buildFile, testProjectDir.newFile('build.gradle'))
	}

	private void copyTestCheckstyleConfig() {
		final buildFile = new File(this.class.classLoader.getResource('checkstyle.xml').toURI())
		final configDir = testProjectDir.newFolder('config')
		Files.copy(buildFile, new File(configDir, 'checkstyle.xml'))
	}

	@Test
	void testJavaVersion7() throws Exception {
		copyTestBuildScript('javaVersion7.build.gradle')
		final result = gradleRunner.withArguments('build').build()
		Assert.assertTrue(result.output.contains('java.version: 1.7'))
	}

	@Test
	void testJavaVersion8() throws Exception {
		copyTestBuildScript('javaVersion8.build.gradle')
		final result = gradleRunner.withArguments('build').build()
		Assert.assertTrue(result.output.contains('java.version: 1.8'))
	}

	@Test
	void testLombokLatestRelease() throws Exception {
		copyTestBuildScript('lombokLatestVersion.build.gradle')
		final result = gradleRunner.withArguments('dependencies').build()
		Assert.assertTrue(result.output.contains('org.projectlombok:lombok:latest.release ->'))
	}

	@Test
	void testLombokDisabled() throws Exception {
		copyTestBuildScript('lombokDisabled.build.gradle')
		final result = gradleRunner.withArguments('-i', 'dependencies').build()
		Assert.assertTrue(result.output.contains(LombokInjector.MESSAGE_LOMBOK_DISABLED))
		Assert.assertFalse(result.output.contains('org.projectlombok:lombok'))
	}

	@Test
	void testLombokSpecifiedVersion() throws Exception {
		copyTestBuildScript('lombokSpecifiedVersion.build.gradle')
		copyTestCheckstyleConfig()
		final result = gradleRunner.withArguments('dependencies').build()
		Assert.assertTrue(result.output.contains('org.projectlombok:lombok:1.16.6'))
	}

	@Test
	void testCheckstyleLatestVersion() throws Exception {
		copyTestBuildScript('checkstyleLatestVersion.build.gradle')
		copyTestCheckstyleConfig()
		final result = gradleRunner.withArguments('dependencies').build()
		Assert.assertTrue(result.output.contains('com.puppycrawl.tools:checkstyle:latest.release ->'))
	}

	@Test
	void testCheckstyleSpecifiedVersion() throws Exception {
		copyTestBuildScript('checkstyleSpecifiedVersion.build.gradle')
		copyTestCheckstyleConfig()
		final result = gradleRunner.withArguments('dependencies').build()
		Assert.assertTrue(result.output.contains('com.puppycrawl.tools:checkstyle:7.4'))
	}

	@Test
	void testCheckstyleSpecifiedVersionUsingPlugin() throws Exception {
		copyTestBuildScript('checkstyleSpecifiedVersionUsingPlugin.build.gradle')
		final result = gradleRunner.withArguments('dependencies').build()
		Assert.assertTrue(result.output.contains('com.puppycrawl.tools:checkstyle:7.4'))
	}

	@Test
	void testCheckstyleDisabled() throws Exception {
		copyTestBuildScript('checkstyleDisabled.build.gradle')
		final result = gradleRunner.withArguments('-i', 'dependencies').build()
		Assert.assertTrue(result.output.contains(CheckstyleInjector.MESSAGE_CHECKSTYLE_DISABLED))
		Assert.assertFalse(result.output.contains('com.puppycrawl.tools:checkstyle'))
	}

}
