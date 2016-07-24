package grails.plugin.springsecurity.ui

@grails.validation.Validateable
class ResetPasswordCommand implements CommandObject {

	String username
	String password
	String password2

	static constraints = {
		password validator: RegisterController.passwordValidator
		password2 nullable: true, validator: RegisterController.password2Validator
	}
}