package page.profile

import page.AbstractSecurityPage

class ProfileListPage extends AbstractSecurityPage {

	static url = 'profile'
	static at = { title == 'Profile List' }
}
