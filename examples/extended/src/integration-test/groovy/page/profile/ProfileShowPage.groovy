package page.profile

import geb.module.Select
import geb.module.TextInput
import page.AbstractSecurityPage

class ProfileShowPage extends AbstractSecurityPage {

	static at = { title == 'Show Profile' }


	static url = 'profile/show'


	static content = {
		editProfileBtn { $('a.edit')}
	}


	void deleteProfile() {
		withConfirm(true) { $("input", class: "delete").click() }
	}
}
