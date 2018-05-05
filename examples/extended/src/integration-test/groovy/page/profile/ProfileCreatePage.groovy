package page.profile

import geb.module.Select
import geb.module.TextInput
import page.AbstractSecurityPage
import page.CreatePage

class ProfileCreatePage extends AbstractSecurityPage {

	static at = { title == 'Create Profile' }


	static url = 'profile/create'


	static content = {
		myQuestion { $('#myQuestion').module(TextInput) }
		myQuestion2 { $('#myQuestion2').module(TextInput) }
		myAnswer2 { $('#myAnswer2').module(TextInput) }
		myAnswer { $('#myAnswer').module(TextInput) }
		submit { $("#create")}
	}

	void create(String userName) {
		def userSelect = $(name: "user.id").module(Select)
		userSelect.selected = "User(username:"+userName+")"
		myQuestion = "Count to 8"
		myQuestion2 = "Count to 4"
		myAnswer2  = "1234"
		myAnswer = "12345678"
		submit()
	}
}
