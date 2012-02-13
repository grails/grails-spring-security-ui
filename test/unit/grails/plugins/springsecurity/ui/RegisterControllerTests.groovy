package grails.plugins.springsecurity.ui

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class RegisterControllerTests extends GroovyTestCase {

	void testPasswordValidator_SameAsUsername() {
		assertEquals 'command.password.error.username',
			RegisterController.passwordValidator('username', [username: 'username'])
	}

	void testPasswordValidator_MinLength() {

		SpringSecurityUtils.setSecurityConfig [:] as ConfigObject

		def command = [username: 'username']
		String password = 'h!Z7'

		assertFalse RegisterController.checkPasswordMinLength(password, command)
		assertTrue RegisterController.checkPasswordMaxLength(password, command)
		assertTrue RegisterController.checkPasswordRegex(password, command)

		assertEquals 'command.password.error.strength',
			RegisterController.passwordValidator(password, command)

		SpringSecurityUtils.securityConfig.ui.password.minLength = 3

		assertTrue RegisterController.checkPasswordMinLength(password, command)
		assertTrue RegisterController.checkPasswordMaxLength(password, command)
		assertTrue RegisterController.checkPasswordRegex(password, command)

		assertNull RegisterController.passwordValidator(password, command)
	}

	void testPasswordValidator_MaxLength() {

		SpringSecurityUtils.setSecurityConfig [:] as ConfigObject

		def command = [username: 'username']
		String password = 'h!Z7aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1'

		assertTrue RegisterController.checkPasswordMinLength(password, command)
		assertFalse RegisterController.checkPasswordMaxLength(password, command)
		assertTrue RegisterController.checkPasswordRegex(password, command)

		assertEquals 'command.password.error.strength',
			RegisterController.passwordValidator(password, command)

		SpringSecurityUtils.securityConfig.ui.password.maxLength = 100

		assertTrue RegisterController.checkPasswordMinLength(password, command)
		assertTrue RegisterController.checkPasswordMaxLength(password, command)
		assertTrue RegisterController.checkPasswordRegex(password, command)

		assertNull RegisterController.passwordValidator(password, command)
	}

	void testPasswordValidator_Regex() {

		SpringSecurityUtils.setSecurityConfig [:] as ConfigObject

		def command = [username: 'username']
		String password = 'password'

		assertTrue RegisterController.checkPasswordMinLength(password, command)
		assertTrue RegisterController.checkPasswordMaxLength(password, command)
		assertFalse RegisterController.checkPasswordRegex(password, command)

		assertEquals 'command.password.error.strength',
			RegisterController.passwordValidator(password, command)

		password = 'h!Z7abcd'

		assertTrue RegisterController.checkPasswordMinLength(password, command)
		assertTrue RegisterController.checkPasswordMaxLength(password, command)
		assertTrue RegisterController.checkPasswordRegex(password, command)

		assertNull RegisterController.passwordValidator(password, command)

		SpringSecurityUtils.securityConfig.ui.password.validationRegex = '^.*s3cr3t.*$'

		assertTrue RegisterController.checkPasswordMinLength(password, command)
		assertTrue RegisterController.checkPasswordMaxLength(password, command)
		assertFalse RegisterController.checkPasswordRegex(password, command)

		assertEquals 'command.password.error.strength',
			RegisterController.passwordValidator(password, command)

		password = '123_s3cr3t_asd'

		assertTrue RegisterController.checkPasswordMinLength(password, command)
		assertTrue RegisterController.checkPasswordMaxLength(password, command)
		assertTrue RegisterController.checkPasswordRegex(password, command)

		assertNull RegisterController.passwordValidator(password, command)
	}

	@Override
	protected void tearDown() {
		super.tearDown()
		SpringSecurityUtils.resetSecurityConfig()
	}
}
