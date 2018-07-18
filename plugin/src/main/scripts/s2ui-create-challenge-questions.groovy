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


import grails.util.GrailsNameUtils
import groovy.transform.Field

@Field String usageMessage = '''
grails s2ui-create-challenge-questions <domain-class-package> <challenge-qa-class-name> <user-domain-class-name> [number-of-questions]

domain-class-package is required and is the package where domainName will live 
challenge-qa-class-name  is required and is the Name of the domain class used to store this information
user-domain-class-name is required and is the package and name of the User class.  In most circumstance I would put this in the same package but it will work if they are different. 
number-of-questions is optional but if not given will default to 2. 

Example: s2ui-create-challenge-questions com.mycompany Profile com.mycompany.User  4
'''


@Field Map templateAttributes

description 'Creates Domian Objects, Service Listener, Controller, I18N and updates the application.groovy file so it can be used', {
	usage usageMessage
	argument name: 'Domain class package',     description: 'The package to use for the domain classes'
	argument name: 'Challenge QA class name',   description: 'The name of challenge questions and answers class'
	argument name: 'User class name',	       description: 'The name of the User/Person class'
	argument name: 'Number Of Questions', 	   description: 'The number of challenge questions generated', required: false
}


String domainPackage = args[0].toLowerCase()
String domainName =args[1]
Model saModel = model(domainPackage + '.' + domainName)
Integer numberOfQuestions = args.size() > 3 ? args[3].toInteger() : 2
Model userModel = model(args[2])


templateAttributes = [
		packageName: saModel.packageName,
		saClassName: saModel.simpleName,
		saClassProperty: saModel.modelName,
		numberOfQuestions: numberOfQuestions,
		userPropName: userModel.modelName.toLowerCase(),
		propertyName: saModel.simpleName.toLowerCase(),
		userDomainName: (userModel.packageName.toLowerCase() == saModel.packageName  ?  "" : userModel.packageName.toLowerCase() + '.') + userModel.modelName.toLowerCase().capitalize()
]

render template('ChallengeQuestionsService.groovy.template'),
		file("grails-app/services/${saModel.packageName}/${saModel.simpleName}Service.groovy"),
		templateAttributes, false

render template('ChallengeQuestionsListenerService.groovy.template'),
		file("grails-app/services/${saModel.packageName}/${saModel.simpleName}ListenerService.groovy"),
		templateAttributes, false

render template('ChallengeQuestions.groovy.template'),
		file("grails-app/domain/${saModel.packageName}/${saModel.simpleName}.groovy"),
		templateAttributes, false

render template('ChallengeQuestionsController.groovy.template'),
		file("grails-app/controllers/${saModel.packageName}/${saModel.simpleName}Controller.groovy"),
		templateAttributes, false

render template('ChallengeQuestionsEdit.gsp.template'),
		file("grails-app/views/${saModel.simpleName.toLowerCase()}/edit.gsp"),
		templateAttributes, false

render template('ChallengeQuestionsCreate.gsp.template'),
		file("grails-app/views/${saModel.simpleName.toLowerCase()}/create.gsp"),
		templateAttributes, false

render template('ChallengeQuestionsIndex.gsp.template'),
		file("grails-app/views/${saModel.simpleName.toLowerCase()}/index.gsp"),
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
	writer.writeLine "spring.security.ui.menu.${saModel.packageName}.${saModel.simpleName}=${saModel.simpleName} Questions"
	writer.newLine()
}
println("Finished s2ui-create-challenge-questions!")