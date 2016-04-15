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
package grails.plugin.springsecurity.ui

import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.strategy.MailStrategy
import grails.plugin.springsecurity.ui.strategy.PropertiesStrategy
import grails.plugin.springsecurity.ui.strategy.RegistrationCodeStrategy
import groovy.text.SimpleTemplateEngine

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class RegisterController extends AbstractS2UiController {

	static defaultAction = 'register'

	/** Dependency injection for the 'saltSource' bean. */
	def saltSource

	/** Dependency injection for the 'uiMailStrategy' bean. */
	MailStrategy uiMailStrategy

	/** Dependency injection for the 'uiRegistrationCodeStrategy' bean. */
	RegistrationCodeStrategy uiRegistrationCodeStrategy

	/** Dependency injection for the 'uiPropertiesStrategy' bean. */
	PropertiesStrategy uiPropertiesStrategy

	def register(RegisterCommand registerCommand) {

		if (!request.post) {
			return [registerCommand: new RegisterCommand()]
		}

		if (registerCommand.hasErrors()) {
			return [registerCommand: registerCommand]
		}

		def user = uiRegistrationCodeStrategy.createUser(registerCommand)
		String salt = saltSource instanceof NullSaltSource ? null : registerCommand.username
		RegistrationCode registrationCode = uiRegistrationCodeStrategy.register(user, registerCommand.password, salt)

		if (registrationCode == null || registrationCode.hasErrors()) {
			// null means problem creating the user
			flash.error = message(code: 'spring.security.ui.register.miscError')
			return [registerCommand: registerCommand]
		}

		sendVerifyRegistrationMail registrationCode, user, registerCommand.email

		[emailSent: true, registerCommand: registerCommand]
	}

	protected void sendVerifyRegistrationMail(RegistrationCode registrationCode, user, String email) {
		String url = generateLink('verifyRegistration', [t: registrationCode.token])

		def body = registerEmailBody
		if (body.contains('$')) {
			body = evaluate(body, [user: user, url: url])
		}

		uiMailStrategy.sendVerifyRegistrationMail(
			to: email,
			from: registerEmailFrom,
			subject: registerEmailSubject,
			html: body.toString())
	}

	def verifyRegistration() {

		String token = params.t

		RegistrationCode registrationCode = token ? RegistrationCode.findByToken(token) : null
		if (!registrationCode) {
			flash.error = message(code: 'spring.security.ui.register.badCode')
			redirect uri: successHandlerDefaultTargetUrl
			return
		}

		def user = uiRegistrationCodeStrategy.finishRegistration(registrationCode)

		if (!user) {
			flash.error = message(code: 'spring.security.ui.register.badCode')
			redirect uri: successHandlerDefaultTargetUrl
			return
		}

		if (user.hasErrors()) {
			// expected to be handled already by ErrorsStrategy.handleValidationErrors
			return
		}

		flash.message = message(code: 'spring.security.ui.register.complete')
		redirect uri: registerPostRegisterUrl ?: successHandlerDefaultTargetUrl
	}

	def forgotPassword(ForgotPasswordCommand forgotPasswordCommand) {

		if (!request.post) {
			return [forgotPasswordCommand: new ForgotPasswordCommand()]
		}

		if (forgotPasswordCommand.hasErrors()) {
			return [forgotPasswordCommand: forgotPasswordCommand]
		}

		def user = findUserByUsername(forgotPasswordCommand.username)
		if (!user) {
			forgotPasswordCommand.errors.rejectValue 'username',
				'spring.security.ui.forgotPassword.user.notFound'
			return [forgotPasswordCommand: forgotPasswordCommand]
		}

		String email = uiPropertiesStrategy.getProperty(user, 'email')
		if (!email) {
			forgotPasswordCommand.errors.rejectValue 'username',
				'spring.security.ui.forgotPassword.noEmail'
			return [forgotPasswordCommand: forgotPasswordCommand]
		}

		RegistrationCode registrationCode = uiRegistrationCodeStrategy.sendForgotPasswordMail(
				forgotPasswordCommand.username, email) { String registrationCodeToken ->

			String url = generateLink('resetPassword', [t: registrationCodeToken])
			String body = forgotPasswordEmailBody
			if (body.contains('$')) {
				body = evaluate(body, [user: user, url: url])
			}

			body
		}
		[emailSent: true, forgotPasswordCommand: forgotPasswordCommand]
	}

	def resetPassword(ResetPasswordCommand resetPasswordCommand) {

		String token = params.t

		def registrationCode = token ? RegistrationCode.findByToken(token) : null
		if (!registrationCode) {
			flash.error = message(code: 'spring.security.ui.resetPassword.badCode')
			redirect uri: successHandlerDefaultTargetUrl
			return
		}

		if (!request.post) {
			return [token: token, resetPasswordCommand: new ResetPasswordCommand()]
		}

		resetPasswordCommand.username = registrationCode.username
		resetPasswordCommand.validate()
		if (resetPasswordCommand.hasErrors()) {
			return [token: token, resetPasswordCommand: resetPasswordCommand]
		}

		def user = uiRegistrationCodeStrategy.resetPassword(resetPasswordCommand, registrationCode)
		if (user.hasErrors()) {
			// expected to be handled already by ErrorsStrategy.handleValidationErrors
		}

		flash.message = message(code: 'spring.security.ui.resetPassword.success')

		redirect uri: registerPostResetUrl ?: successHandlerDefaultTargetUrl
	}

	protected String generateLink(String action, linkParams) {
		createLink(base: "$request.scheme://$request.serverName:$request.serverPort$request.contextPath",
		           controller: 'register', action: action, params: linkParams)
	}

	protected String evaluate(s, binding) {
		new SimpleTemplateEngine().createTemplate(s).make(binding)
	}

	protected String forgotPasswordEmailBody
	protected String registerEmailBody
	protected String registerEmailFrom
	protected String registerEmailSubject
	protected String registerPostRegisterUrl
	protected String registerPostResetUrl
	protected String successHandlerDefaultTargetUrl

	protected static int passwordMaxLength
	protected static int passwordMinLength
	protected static String passwordValidationRegex

	void afterPropertiesSet() {
		super.afterPropertiesSet()

		RegisterCommand.User = User
		RegisterCommand.usernamePropertyName = usernamePropertyName

		forgotPasswordEmailBody = conf.ui.forgotPassword.emailBody ?: ''
		registerEmailBody = conf.ui.register.emailBody ?: ''
		registerEmailFrom = conf.ui.register.emailFrom ?: ''
		registerEmailSubject = conf.ui.register.emailSubject ?: ''
		registerPostRegisterUrl = conf.ui.register.postRegisterUrl ?: ''
		registerPostResetUrl = conf.ui.register.postResetUrl ?: ''
		successHandlerDefaultTargetUrl = conf.successHandler.defaultTargetUrl ?: '/'

		passwordMaxLength = conf.ui.password.maxLength instanceof Number ? conf.ui.password.maxLength : 64
		passwordMinLength = conf.ui.password.minLength instanceof Number ? conf.ui.password.minLength : 8
		passwordValidationRegex = conf.ui.password.validationRegex ?: '^.*(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&]).*$'
	}

	static final passwordValidator = { String password, command ->
		if (command.username && command.username == password) {
			return 'command.password.error.username'
		}

		if (!checkPasswordMinLength(password, command) ||
		    !checkPasswordMaxLength(password, command) ||
		    !checkPasswordRegex(password, command)) {
			return 'command.password.error.strength'
		}
	}

	static boolean checkPasswordMinLength(String password, command) {
		password && password.length() >= passwordMinLength
	}

	static boolean checkPasswordMaxLength(String password, command) {
		password && password.length() <= passwordMaxLength
	}

	static boolean checkPasswordRegex(String password, command) {
		password && password.matches(passwordValidationRegex)
	}

	static final password2Validator = { value, command ->
		if (command.password != command.password2) {
			return 'command.password2.error.mismatch'
		}
	}
}

class ForgotPasswordCommand implements CommandObject {
	String username
}

class RegisterCommand implements CommandObject {

	protected static Class<?> User
	protected static String usernamePropertyName

	String username
	String email
	String password
	String password2

	static constraints = {
		username validator: { value, command ->
			if (!value) {
				return
			}

			if (User.findWhere((usernamePropertyName): value)) {
				return 'registerCommand.username.unique'
			}
		}
		email email: true
		password validator: RegisterController.passwordValidator
		password2 nullable: true, validator: RegisterController.password2Validator
	}
}

class ResetPasswordCommand implements CommandObject {

	String username
	String password
	String password2

	static constraints = {
		password validator: RegisterController.passwordValidator
		password2 nullable: true, validator: RegisterController.password2Validator
	}
}
