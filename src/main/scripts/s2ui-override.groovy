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
import groovy.transform.Field

@Field String usageMessage = '''
grails s2ui-override <type> [controllerPackage]

type can be one of aclclass, aclentry, aclobjectidentity, aclsid, persistentlogin,
register, registrationcode, requestmap, role, securityinfo, user, auth, or layout (not case-sensitive)

controllerPackage is required for any type that has a controller (i.e. all but 'auth' and 'layout')

Example: grails s2ui-override user com.yourcompany.yourapp
'''

description 'Copies plugin controllers and GSPs to the application so they can be overridden', {
	usage usageMessage

	argument name: 'type',              description: 'The group of files to copy'
	argument name: 'controllerPackage', description: 'The package name for the controller', required: false
}

String type = args[0].toLowerCase()
String controllerPackage = args.size() > 1 ? args[1] : ''
File grailsApp = file('grails-app')
File controllersDir = new File(grailsApp, 'controllers')
File viewsDir = new File(grailsApp, 'views')
File layoutsDir = new File(viewsDir, 'layouts')

def controllers = [aclclass:          'AclClass',
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

if (!controllerPackage) {
	if ('layout' == type) {
		// special case for springSecurityUI.gsp
		copy 'layouts/springSecurityUI.gsp', layoutsDir

		copy 'includes/_ajaxLogin.gsp', new File(viewsDir, 'includes')
		return
	}

	if ('auth' == type) {
		// special case for auth.gsp
		copy 'login/auth.gsp', new File(viewsDir, 'login')
		return
	}
}

String controller = controllers[type]
if (!controller) {
	error "Unknown type '${args[0]}'\n\nUsage:\n$usageMessage"
	return false
}

if (!controllerPackage) {
	error "The controller package is required for type '${args[0]}'\n\nUsage:\n$usageMessage"
	return false
}

addStatus "Copying ${args[0]} resources"

if ('grails.plugin.springsecurity.ui' == controllerPackage) {
	error 'The controller package cannot be the same as the plugin controller'
	return false
}

// generate the controller
String directoryName = controllerPackage.replaceAll('\\.', '/')
File destinationDirectory = new File(controllersDir, directoryName)
destinationDirectory.mkdirs()

render template(controller + 'Controller.groovy.template'),
       new File(destinationDirectory, controller + 'Controller.groovy'),
       [packageDeclaration: "package $controllerPackage"], false

// copy the GSPs
directoryName = GrailsNameUtils.getPropertyName(controller)
File gspDirectory = new File(viewsDir, directoryName)

for (resource in resources(directoryName + '/*.gsp')) {
	copy resource, gspDirectory
}

if ('register' == type) {
	copy 'layouts/register.gsp', layoutsDir
}

private void copy(pathOrResource, File destinationDirectory) {
	destinationDirectory.mkdirs()
	copy resource(pathOrResource), destinationDirectory
}
