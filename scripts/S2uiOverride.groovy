/* Copyright 2009-2015 the original author or authors.
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
import grails.util.GrailsNameUtils
import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript('_GrailsBootstrap')

USAGE = """
	Usage: grails s2ui-override <type> <controller-package>

	Copies plugin controllers and GSPs to the application so they can be overridden.

	<type> can be one of aclclass, aclentry, aclobjectidentity, aclsid, persistentlogin,
	register, registrationcode, requestmap, role, securityinfo, user, auth, or layout (not case-sensitive)

	<controller-package> is required for any type that has a controller (i.e. all but 'auth' and 'layout')

	Example: grails s2ui-override user com.yourcompany.yourapp
"""

overwriteAll = false
pluginViewsDir = "$springSecurityUiPluginDir/grails-app/views"
appGrailsApp = "$basedir/grails-app"

templateDir = "$springSecurityUiPluginDir/src/templates"
templateEngine = new SimpleTemplateEngine()

controllers = [aclclass:          'AclClass',
               aclentry:          'AclEntry',
               aclobjectidentity: 'AclObjectIdentity',
               aclsid:            'AclSid',
               persistentlogin:   'PersistentLogin',
               register:          'Register',
               registrationcode:  'RegistrationCode',
               requestmap:        'Requestmap',
               role:              'Role',
               securityinfo:      'SecurityInfo',
               user:              'User']

target(s2uiOverride: 'Copy plugin UI files to the project so they can be overridden') {

	String[] typeAndPackage = parseArgs()
	if (!typeAndPackage) {
		return
	}

	File appViewsDir = new File(appGrailsApp, 'views')
	File appLayoutsDir = new File(appViewsDir, 'layouts')
	File pluginLayoutsDir = new File(pluginViewsDir, 'layouts')

	if (typeAndPackage.length == 1) {
		if ('layout' == typeAndPackage[0]) {
			// special case for springSecurityUI.gsp
			copyFile new File(pluginLayoutsDir, 'springSecurityUI.gsp'),
			         appLayoutsDir

			copyFile new File(pluginViewsDir, 'includes/_ajaxLogin.gsp'),
			         new File(appViewsDir, 'includes')
		}
		else if ('auth' == typeAndPackage[0]) {
			// special case for auth.gsp
			copyFile new File(pluginViewsDir, 'login/auth.gsp'),
			         new File(appViewsDir, 'login')
		}
		return
	}

	String type = typeAndPackage[0]
	String controller = controllers[type.toLowerCase()]
	if (!controller) {
		errorMessage "\nUnknown type '$type'\n$USAGE"
		return
	}

	printMessage "Copying $type resources"

	String packageName = typeAndPackage[1].trim()
	if ('grails.plugin.springsecurity.ui' == packageName) {
		errorMessage "\nThe controller package cannot be the same as the plugin controller\n"
		return
	}

	// generate the controller
	generateController controller, packageName

	// copy the GSPs
	String directoryName = GrailsNameUtils.getPropertyName(controller)
	File gspDirectory = new File(appViewsDir, directoryName)

	for (file in new File(pluginViewsDir, directoryName).listFiles()) {
		if (file.name.toLowerCase().endsWith('.gsp')) {
			copyFile file, gspDirectory
		}
	}

	if ('register'.equalsIgnoreCase(type)) {
		copyFile new File(pluginLayoutsDir, 'register.gsp'), appLayoutsDir
	}
}

copyFile = { File file, File destinationDirectory ->

	destinationDirectory.mkdirs()

	if (okToWrite(new File(destinationDirectory, file.name))) {
		ant.copy file: file, todir: destinationDirectory, overwrite: true
	}
}

generateController = { String controller, String packageName ->
	String directoryName = packageName.replaceAll('\\.', '/')
	File destinationDirectory = new File(appGrailsApp, "controllers/$directoryName")
	destinationDirectory.mkdirs()

	File outputFile = new File(destinationDirectory, controller + 'Controller.groovy')
	if (!okToWrite(outputFile)) return

	File templateFile = new File(templateDir, controller + 'Controller.groovy.template')
	if (!templateFile.exists()) {
		errorMessage "\n$templateFile.path doesn't exist"
		return
	}

	outputFile.withWriter { writer ->
		templateEngine.createTemplate(templateFile.text).make(packageDeclaration: "package $packageName").writeTo writer
	}

	printMessage "Generated $outputFile.absolutePath"
}

printMessage = { String message -> event('StatusUpdate', [message]) }
errorMessage = { String message -> event('StatusError', [message]) }

okToWrite = { File file ->

	if (overwriteAll || !file.exists()) {
		return true
	}

	String propertyName = "file.overwrite.$file.name"
	ant.input(addProperty: propertyName, message: "$file exists, ok to overwrite?",
	          validargs: 'y,n,a', defaultvalue: 'y')

	if (ant.antProject.properties."$propertyName" == 'n') {
		return false
	}

	if (ant.antProject.properties."$propertyName" == 'a') {
		overwriteAll = true
	}

	true
}

private parseArgs() {
	args = args ? args.split('\n') : []
	if (args.size() == 2) {
		return args
	}
	if (args.size() == 1 && (args[0].equalsIgnoreCase('auth') || args[0].equalsIgnoreCase('layout'))) {
		return args
	}

	errorMessage USAGE
	null
}

setDefaultTarget 's2uiOverride'
