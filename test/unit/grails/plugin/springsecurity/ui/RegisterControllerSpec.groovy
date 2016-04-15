package grails.plugin.springsecurity.ui

import grails.plugin.springsecurity.SpringSecurityUtils
import spock.lang.Specification

class RegisterControllerSpec extends Specification {

	void setup() {
		SpringSecurityUtils.setSecurityConfig [:] as ConfigObject
		updateFromConfig()
	}

	void cleanup() {
		SpringSecurityUtils.resetSecurityConfig()
	}

	void 'passwordValidator validation fails if the password is the same as the username'() {
		expect:
		'command.password.error.username' == RegisterController.passwordValidator('username', [username: 'username'])
	}

	void 'passwordValidator validation strength check fails if the password is too short'() {
		when:
		def command = [username: 'username']
		String password = 'h!Z7'

		then:
		!RegisterController.checkPasswordMinLength(password, command)
		RegisterController.checkPasswordMaxLength password, command
		RegisterController.checkPasswordRegex password, command

		'command.password.error.strength' == RegisterController.passwordValidator(password, command)
	}

	void 'passwordValidator validation fails if the password is too short'() {
		when:
		def command = [username: 'username']
		String password = 'h!Z7'
		SpringSecurityUtils.securityConfig.ui.password.minLength = 3
		updateFromConfig()

		then:
		RegisterController.checkPasswordMinLength password, command
		RegisterController.checkPasswordMaxLength password, command
		RegisterController.checkPasswordRegex password, command

		!RegisterController.passwordValidator(password, command)
	}

	void 'passwordValidator validation strength check fails if the password is too long'() {
		when:
		def command = [username: 'username']
		String password = 'h!Z7aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1'

		then:
		RegisterController.checkPasswordMinLength password, command
		!RegisterController.checkPasswordMaxLength(password, command)
		RegisterController.checkPasswordRegex password, command

		'command.password.error.strength' == RegisterController.passwordValidator(password, command)
	}

	void 'passwordValidator validation fails if the password is too long'() {
		when:
		def command = [username: 'username']
		String password = 'h!Z7aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1'
		SpringSecurityUtils.securityConfig.ui.password.maxLength = 100
		updateFromConfig()

		then:
		RegisterController.checkPasswordMinLength password, command
		RegisterController.checkPasswordMaxLength password, command
		RegisterController.checkPasswordRegex password, command

		!RegisterController.passwordValidator(password, command)
	}

	void 'passwordValidator validation fails if the default regex does not match'() {
		when:
		def command = [username: 'username']
		String password = 'password'

		then:
		RegisterController.checkPasswordMinLength password, command
		RegisterController.checkPasswordMaxLength password, command
		!RegisterController.checkPasswordRegex(password, command)

		'command.password.error.strength' == RegisterController.passwordValidator(password, command)

		when:
		password = 'h!Z7abcd'

		then:
		RegisterController.checkPasswordMinLength password, command
		RegisterController.checkPasswordMaxLength password, command
		RegisterController.checkPasswordRegex password, command

		!RegisterController.passwordValidator(password, command)
	}

	void 'passwordValidator validation fails if an updated regex does not match'() {
		when:
		def command = [username: 'username']
		String password = 'h!Z7abcd'
		SpringSecurityUtils.securityConfig.ui.password.validationRegex = '^.*s3cr3t.*$'
		updateFromConfig()

		then:
		RegisterController.checkPasswordMinLength password, command
		RegisterController.checkPasswordMaxLength password, command
		!RegisterController.checkPasswordRegex(password, command)

		'command.password.error.strength' == RegisterController.passwordValidator(password, command)

		when:
		password = '123_s3cr3t_asd'

		then:
		RegisterController.checkPasswordMinLength password, command
		RegisterController.checkPasswordMaxLength password, command
		RegisterController.checkPasswordRegex password, command

		!RegisterController.passwordValidator(password, command)
	}

	private void updateFromConfig() {
		new RegisterController().afterPropertiesSet()
	}
}
