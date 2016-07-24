package grails.plugin.springsecurity.ui

@grails.validation.Validateable
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
