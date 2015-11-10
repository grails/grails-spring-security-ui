/* Copyright 2006-2013 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
includeTargets << new File(springSecurityCorePluginDir, "scripts/_S2Common.groovy")

functionalTestPluginVersion = '1.2.7'
projectfiles = new File(basedir, 'webtest/projectFiles')
grailsHome = null
dotGrails = null
projectDir = null
appName = null
pluginVersion = null
testprojectRoot = null
deleteAll = false
grailsVersion = null

target(createS2UiTestApp: 'Creates test apps for functional tests') {

	def configFile = new File(basedir, 'testapps.config.groovy')
	if (!configFile.exists()) {
		error "$configFile.path not found"
	}

	new ConfigSlurper().parse(configFile.text).each { name, config ->
		printMessage "\nCreating app based on configuration $name: ${config.flatten()}\n"
		init name, config
		createApp()
		installPlugins(false)
		runQuickstart()
		copySampleFiles()
		copyTests(false)

		printMessage "\nCreating extended app based on configuration $name: ${config.flatten()}\n"
		init name + '_ext', config
		createApp()
		installPlugins(true)
		runQuickstart()
		copySampleFiles()
		copyTests(true)
		runCreatePersistentToken()
	}
}

private void init(String name, config) {

	pluginVersion = config.pluginVersion
	if (!pluginVersion) {
		error "pluginVersion wasn't specified for config '$name'"
	}

	def pluginZip = new File(basedir, "grails-spring-security-ui-${pluginVersion}.zip")
	if (!pluginZip.exists()) {
		error "plugin $pluginZip.absolutePath not found"
	}

	grailsHome = config.grailsHome
	if (!new File(grailsHome).exists()) {
		error "Grails home $grailsHome not found"
	}

	projectDir = config.projectDir
	appName = 'spring-security-ui-test-' + name
	testprojectRoot = "$projectDir/$appName"
	grailsVersion = config.grailsVersion
	dotGrails = config.dotGrails + '/' + grailsVersion
}

private void createApp() {

	ant.mkdir dir: projectDir

	deleteDir testprojectRoot
	deleteDir "$dotGrails/projects/$appName"

	callGrails grailsHome, projectDir, 'dev', 'create-app', [appName]
}

private void copySampleFiles() {
	ant.unzip src: "$projectfiles.path/testDb.zip", dest: "$testprojectRoot/db"
	ant.copy file: "$projectfiles.path/DataSource.groovy", todir: "$testprojectRoot/grails-app/conf"
	ant.copy file: "$projectfiles.path/error.gsp", todir: "$testprojectRoot/grails-app/views", overwrite: true
	ant.copy file: "$projectfiles.path/index.gsp", todir: "$testprojectRoot/grails-app/views", overwrite: true

	new File("$testprojectRoot/grails-app/conf/Config.groovy").withWriterAppend {
		it.writeLine 'grails {'
		it.writeLine '   mail {'
		it.writeLine '      host = "localhost"'
		it.writeLine '      port = 1025'
		it.writeLine '      "default" {'
		it.writeLine '         from = "do.not.reply@server.com"'
		it.writeLine '      }'
		it.writeLine '   }'
		it.writeLine '}'
	}
}

private void installPlugins(boolean includeAcl) {

	File buildConfig = new File(testprojectRoot, 'grails-app/conf/BuildConfig.groovy')
	String contents = buildConfig.text

	contents = contents.replace('grails.project.class.dir = "target/classes"', "grails.project.work.dir = 'target'")
	contents = contents.replace('grails.project.test.class.dir = "target/test-classes"', '')
	contents = contents.replace('grails.project.test.reports.dir = "target/test-reports"', '')

	contents = contents.replace('//mavenLocal()', 'mavenLocal()')
	contents = contents.replace('repositories {', '''repositories {
mavenRepo 'http://repo.spring.io/milestone' // TODO remove
''')

	contents = contents.replace('grails.project.fork', 'grails.project.forkDISABLED')

	contents = contents.replace('plugins {', """plugins {
runtime ":mail:1.0.1"
runtime ":famfamfam:1.0"
runtime ":jquery:1.10.2"
runtime ":jquery-ui:1.10.3"
test ":functional-test:$functionalTestPluginVersion"
runtime ":spring-security-ui:$pluginVersion"
${includeAcl ? 'runtime ":spring-security-acl:2.0-RC1"' : ''}
""")

	contents = contents.replace('dependencies {', """dependencies {
runtime 'com.h2database:h2:1.3.163'
test('dumbster:dumbster:1.6') {
	excludes 'mail', 'activation'
}
""")

	buildConfig.withWriter { it.writeLine contents }

	callGrails grailsHome, testprojectRoot, 'dev', 'compile', null, true // can fail when installing the functional-test plugin
	callGrails grailsHome, testprojectRoot, 'dev', 'compile'
}


private void runQuickstart() {
	callGrails grailsHome, testprojectRoot, 'dev', 's2-quickstart', ['com.testapp', 'User', 'Role', 'Requestmap']

	File user = new File(testprojectRoot, 'grails-app/domain/com/testapp/User.groovy')
	String contents = user.text
	contents = contents.replace('boolean passwordExpired', '''boolean passwordExpired
	String email''')
	contents = contents.replace('static constraints = {', '''static constraints = {
		email blank: false, email: true, unique: true
''')
	contents = contents.replace("password column: '`password`'", '')

	user.withWriter { it.writeLine contents }

	File config = new File(testprojectRoot, 'grails-app/conf/Config.groovy')
	contents = config.text

	contents += '''
grails.plugin.springsecurity.fii.rejectPublicInvocations = false
grails.plugin.springsecurity.rejectIfNoRule = false
'''

	config.withWriter { it.writeLine contents }

	File requestmap = new File(testprojectRoot, 'grails-app/domain/com/testapp/Requestmap.groovy')
	contents = requestmap.text

	contents = contents.replace('HttpMethod httpMethod', '')
	contents = contents.replace('httpMethod nullable: true', '')
	contents = contents.replace("unique: 'httpMethod'", 'unique: true')

	requestmap.withWriter { it.writeLine contents }
}

private void copyTests(boolean extraTests) {

	// copy all of the tests but configure which ones to run based on core set plus extraTests
	ant.copy(todir: "${testprojectRoot}/test/functional") {
		fileset(dir: "$basedir/webtest/tests")
	}

	def tests = []
	if (!extraTests) {
		tests.addAll 'DefaultMenu', 'DefaultSecurityInfo'
	}
	tests.addAll 'User', 'Register', 'RegistrationCode', 'Requestmap', 'Role'
	if (extraTests) {
		tests.addAll 'PersistentLogin', 'ExtendedMenu', 'ExtendedSecurityInfo', 'AclClass', 'AclEntry', 'AclObjectIdentity', 'AclSid'
	}
	new File("$testprojectRoot/grails-app/conf/BuildConfig.groovy").withWriterAppend {
		it.writeLine 'grails.testing.patterns = ["' + tests.join('", "') + '"]'
	}
}

private void runCreatePersistentToken() {
	callGrails grailsHome, testprojectRoot, 'dev', 's2-create-persistent-token', ['com.testapp.PersistentToken']
}

private void deleteDir(String path) {
	if (new File(path).exists() && !deleteAll) {
		String code = "confirm.delete.$path"
		ant.input message: "$path exists, ok to delete?", addproperty: code, validargs: 'y,n,a'
		def result = ant.antProject.properties[code]
		if ('a'.equalsIgnoreCase(result)) {
			deleteAll = true
		}
		else if (!'y'.equalsIgnoreCase(result)) {
			printMessage "\nNot deleting $path"
			exit 1
		}
	}

	ant.delete dir: path
}

void error(String message) {
	errorMessage "\n\nERROR: $message\n\n"
	exit 1
}

private void callGrails(String grailsHome, String dir, String env, String action, List extraArgs = null, boolean ignoreFailure = false) {

	String resultproperty = 'exitCode' + System.currentTimeMillis()
	String outputproperty = 'execOutput' + System.currentTimeMillis()

	println "Running 'grails $env $action ${extraArgs?.join(' ') ?: ''}'"

	ant.exec(executable: "${grailsHome}/bin/grails", dir: dir, failonerror: false,
				resultproperty: resultproperty, outputproperty: outputproperty) {
		ant.env key: 'GRAILS_HOME', value: grailsHome
		ant.arg value: env
		ant.arg value: action
		extraArgs.each { ant.arg value: it }
		ant.arg value: '--stacktrace'
	}

	println ant.project.getProperty(outputproperty)

	int exitCode = ant.project.getProperty(resultproperty) as Integer
	if (exitCode && !ignoreFailure) {
		exit exitCode
	}
}


setDefaultTarget 'createS2UiTestApp'
