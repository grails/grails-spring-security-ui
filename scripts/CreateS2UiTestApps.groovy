includeTargets << grailsScript('_GrailsBootstrap')

functionalTestPluginVersion = '1.2.7'
projectfiles = new File(basedir, 'webtest/projectFiles')
grailsHome = null
dotGrails = null
projectDir = null
appName = null
pluginVersion = null
pluginZip = null
testprojectRoot = null
deleteAll = false

target(createS2UiTestApp: 'Creates test apps for functional tests') {

	def configFile = new File(basedir, 'testapps.config.groovy')
	if (!configFile.exists()) {
		error "$configFile.path not found"
	}

	new ConfigSlurper().parse(configFile.text).each { name, config ->
		echo "\nCreating app based on configuration $name: ${config.flatten()}\n"
		init name, config
		createApp()
		installPlugins()
		runQuickstart()
		copySampleFiles([])
		copyTests()
	}
}

private void init(String name, config) {

	pluginVersion = config.pluginVersion
	if (!pluginVersion) {
		error "pluginVersion wasn't specified for config '$name'"
	}

	pluginZip = new File(basedir, "grails-spring-security-ui-${pluginVersion}.zip")
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
	dotGrails = config.dotGrails
}

private void createApp() {

	ant.mkdir dir: projectDir

	deleteDir testprojectRoot
	deleteDir "$dotGrails/projects/$appName"

	callGrails(grailsHome, projectDir, 'dev', 'create-app') {
		ant.arg value: appName
	}
}

private void copySampleFiles(extraTests) {
	ant.unzip src: "$projectfiles.path/testDb.zip", dest: "$testprojectRoot/db"
	ant.copy file: "$projectfiles.path/DataSource.groovy", todir: "$testprojectRoot/grails-app/conf"
	ant.copy file: "$projectfiles.path/index.gsp", todir: "$testprojectRoot/grails-app/views"
	ant.copy file: "$projectfiles.path/User.groovy", todir: "$testprojectRoot/grails-app/domain/com/testapp", overwrite: true

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

	def tests = ['DefaultMenu', 'User', 'Register', 'RegistrationCode', 'Role', 'SecurityInfo']
	tests.addAll extraTests
	new File("$testprojectRoot/grails-app/conf/BuildConfig.groovy").withWriterAppend {
		it.writeLine 'grails.testing.patterns = ["' + tests.join('", "') + '"]'
	}
}

private void installPlugins() {

	// install plugins in local dir to make optional STS setup easier
	ant.copy file: "$projectfiles.path/BuildConfig.groovy", todir: "$testprojectRoot/grails-app/conf"

	ant.mkdir dir: "${testprojectRoot}/plugins"
	callGrails(grailsHome, testprojectRoot, 'dev', 'install-plugin') {
		ant.arg value: "functional-test ${functionalTestPluginVersion}"
	}
	callGrails(grailsHome, testprojectRoot, 'dev', 'install-plugin') {
		ant.arg value: pluginZip.absolutePath
	}
}

private void runQuickstart() {
	callGrails(grailsHome, testprojectRoot, 'dev', 's2-quickstart') {
		['com.testapp', 'User', 'Role', 'Requestmap'].each { ant.arg value: it }
	}
}

private void copyTests() {
	ant.copy(todir: "${testprojectRoot}/test/functional") {
		fileset(dir: "$basedir/webtest/tests")
	}
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
			ant.echo "\nNot deleting $path"
			exit 1
		}
	}

	ant.delete dir: path
}

private void error(String message) {
	ant.echo "\nERROR: $message"
	exit 1
}

private void callGrails(String grailsHome, String dir, String env, String action, extraArgs = null) {
	ant.exec(executable: "${grailsHome}/bin/grails", dir: dir, failonerror: 'true') {
		ant.env key: 'GRAILS_HOME', value: grailsHome
		ant.arg value: env
		ant.arg value: action
		extraArgs?.call()
	}
}

setDefaultTarget 'createS2UiTestApp'
