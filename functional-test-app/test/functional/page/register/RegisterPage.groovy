package page.register

import geb.module.TextInput
import page.AbstractSecurityPage

class RegisterPage extends AbstractSecurityPage {

	static url = 'register'

	static at = { title == 'Register' }

	static content = {
		form { $('registerForm') }

		username { $(name: 'username').module(TextInput) }
		email { $(name: 'email').module(TextInput) }
		password { $(name: 'password').module(TextInput) }
		password2 { $(name: 'password2').module(TextInput) }

		submit { $('a', id: 'submit') }
	}
}
