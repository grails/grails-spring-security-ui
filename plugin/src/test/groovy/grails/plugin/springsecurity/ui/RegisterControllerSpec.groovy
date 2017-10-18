package grails.plugin.springsecurity.ui

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.IgnoreRest
import spock.lang.Specification

class RegisterControllerSpec extends Specification implements ControllerUnitTest<RegisterController> {

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

		'command.password.error.length' == RegisterController.passwordValidator(password, command)[0]
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

		'command.password.error.length' == RegisterController.passwordValidator(password, command)[0]
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

    @IgnoreRest
    void "verify generateLink functionality"() {
		given: "the grails.serverUrl is set"
		config.grails.serverUrl='http://grails.org'

        when: "the generateLink method is called"
        def results = controller.generateLink(action, linkParams, absolute)

		then: "the configured grails.serverUrl is used if the absolute parameter is true"
		results == expectedUrl

        where:
        absolute | action | linkParams               | expectedUrl
        false    | 'shipit' | [foo: 'foo', bar: 'bar'] | 'http://localhost:80/register/shipit?foo=foo&bar=bar'
		true     | 'shipit' | [foo: 'foo', bar: 'bar'] | 'http://grails.org/register/shipit?foo=foo&bar=bar'
    }

	private void updateFromConfig() {
		new RegisterController().afterPropertiesSet()
	}
}
