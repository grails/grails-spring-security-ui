package page.user

import geb.module.Checkbox
import geb.module.TextInput
import page.CreatePage

class UserCreatePage extends CreatePage {

	static url = 'user/create'

	static typeName = { 'User' }

	static content = {
		username { $(name: 'username').module(TextInput) }
		enabled { $(name: 'enabled').module(Checkbox) }
		accountExpired { $(name: 'accountExpired').module(Checkbox) }
		accountLocked { $(name: 'accountLocked').module(Checkbox) }
		passwordExpired { $(name: 'passwordExpired').module(Checkbox) }
	}
}
