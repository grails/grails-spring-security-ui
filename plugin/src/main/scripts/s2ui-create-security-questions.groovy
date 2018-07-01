/* Copyright 2009-2016 the original author or authors.
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


import grails.codegen.model.Model
import grails.util.GrailsNameUtils
import groovy.transform.Field

@Field String usageMessage = '''
grails s2ui-create-security-questions <domain-class-package> <security-qa-class-name> [number-of-questions]

domain-class-package is required and is the package where domainName will live 
security-qa-class-name  is required and is the Name of the domain class used to store this information
number-of-questions is optional but if not given will default to 2. 

Example: s2ui-create-security-questions com.mycompany Profile  4
'''

@Field Map templateAttributes

description 'Creates Domian Objects, Service Listener, and updates the YML file so it can be used', {
	usage usageMessage

	argument name: 'Domain class package',     description: 'The package to use for the domain classes'
	argument name: 'Security QA class name',   description: 'The name of security questions and answers class'
	argument name: 'Number Of Questions', 	   description: 'The number of security questions generated', required: false
}


String domainPackage = args[0].toLowerCase()
String domainName =args[1]
Model saModel = model(domainPackage + '.' + domainName)
Integer numberOfQuestions = args.size() > 2 ? args[2].toInteger() : 2
File grailsApp = file('grails-app')
File controllersDir = new File(grailsApp, 'controllers')
File serviceDir = new File(grailsApp, 'services')
File domainDir = new File(grailsApp, 'domain')
File viewsDir = new File(grailsApp, 'views')
File layoutsDir = new File(viewsDir, 'layouts')

templateAttributes = [
		packageName: saModel.packageName,
		saClassName: saModel.simpleName,
		saClassProperty: saModel.modelName,
		numberOfQuestions: numberOfQuestions
		]

render template('SAListenerService.groovy.template'),
		file("grails-app/services/${saModel.packageName}/${saModel.simpleName}ListenerService.groovy"),
		templateAttributes, false

render template('SecuirtyQuestions.groovy.template'),
		file("grails-app/domain/${saModel.packageName}/${saModel.simpleName}.groovy"),
		templateAttributes, false

render template('SecuirtyQuestionsController.groovy.template'),
		file("grails-app/controllers/${saModel.packageName}/${saModel.simpleName}Controller.groovy"),
		templateAttributes, false

file('grails-app/conf/application.groovy').withWriterAppend { BufferedWriter writer ->
	writer.newLine()
	writer.writeLine '// Added by the Spring Security UI plugin:'
	writer.writeLine "grails.plugin.springsecurity.ui.forgotPassword.forgotPasswordExtraValidationDomainClassName = '${saModel?.packageName}.${saModel?.simpleName}'"
	writer.writeLine "grails.plugin.springsecurity.ui.forgotPassword.forgotPasswordExtraValidation = ["
	for(int i = 1; i <= numberOfQuestions; i++) {
		writer.writeLine "\t[labelDomain: 'myQuestion$i', prop:'myAnswer$i'],"
	}
	writer.writeLine ']'
	writer.newLine()
}

file('grails-app/i18n/messages.properties').withWriterAppend { BufferedWriter writer ->
	writer.newLine()
	writer.writeLine "spring.security.ui.menu.${saModel.packageName}.${saModel.simpleName}.index=Security Questions"
	writer.newLine()
}
println("Finished s2ui-create-security-questions!")