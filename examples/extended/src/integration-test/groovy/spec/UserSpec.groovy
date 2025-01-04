package spec

import grails.testing.mixin.integration.Integration
import page.user.UserCreatePage
import page.user.UserEditPage
import page.user.UserSearchPage
import spock.lang.Stepwise

@Stepwise
@Integration
class UserSpec extends AbstractSecuritySpec {

	void testFindAll() {
		when:
		to UserSearchPage

		then:
		assertNotSearched()

		when:
		submit()

		then:
		at UserSearchPage
		assertResults 1, 10, 22
	}

	void testFindByUsername() {
		when:
		to UserSearchPage

		username = 'foo'
		submit()

		then:
		at UserSearchPage
		assertResults 1, 3, 3

		assertContentContains 'foon_2'
		assertContentContains 'foolkiller'
		assertContentContains 'foostra'
	}

	void testFindByDisabled() {
		when:
		to UserSearchPage

		// Temporary workaround for problem with Geb RadioButtons module
		//enabled.checked = '-1'
		$('input', type: 'radio', name: 'enabled', value: '-1').click()
		submit()

		then:
		at UserSearchPage
		assertResults 1, 1, 1
		assertContentContains 'billy9494'
	}

	void testFindByAccountExpired() {
		when:
		to UserSearchPage

		// Temporary workaround for problem with Geb RadioButtons module
		//accountExpired.checked = '1'
		$('input', type: 'radio', name: 'accountExpired', value: '1').click()

		submit()

		then:
		at UserSearchPage
		assertResults 1, 3, 3
		assertContentContains 'maryrose'
		assertContentContains 'ratuig'
		assertContentContains 'rome20c'
	}

	void testFindByAccountLocked() {
		when:
		to UserSearchPage

		// Temporary workaround for problem with Geb RadioButtons module
		//accountLocked.checked = '1'
		$('input', type: 'radio', name: 'accountLocked', value: '1').click()

		submit()

		then:
		at UserSearchPage
		assertResults 1, 3, 3
		assertContentContains 'aaaaaasd'
		assertContentContains 'achen'
		assertContentContains 'szhang1999'
	}

	void testFindByPasswordExpired() {
		when:
		to UserSearchPage

		// Temporary workaround for problem with Geb RadioButtons module
		//passwordExpired.checked = '1'
		$('input', type: 'radio', name: 'passwordExpired', value: '1').click()

		submit()

		then:
		at UserSearchPage
		assertResults 1, 3, 3
		assertContentContains 'hhheeeaaatt'
		assertContentContains 'mscanio'
		assertContentContains 'kittal'
	}

	void testCreateAndEdit() {
		given:
		String newUsername = 'newuser' + UUID.randomUUID()

		// make sure it doesn't exist
		when:
		to UserSearchPage

		username = newUsername
		submit()

		then:
		at UserSearchPage
		assertNoResults()

		// create
		when:
		to UserCreatePage

		username = newUsername
		$('#password') << 'password'
		enabled.check()
		submit()

		then:
		at UserEditPage
		username == newUsername
		enabled.checked
		!accountExpired.checked
		!accountLocked.checked
		!passwordExpired.checked

		// edit
		when:
		String updatedName = newUsername + '_updated'
		username = updatedName
		enabled.uncheck()
		accountExpired.check()
		accountLocked.check()
		passwordExpired.check()
		submit()

		then:
		at UserEditPage
		username == updatedName
		!enabled.checked
		accountExpired.checked
		accountLocked.checked
		passwordExpired.checked

		// delete
		when:
		delete()

		then:
		at UserSearchPage

		when:
		username = updatedName
		submit()

		then:
		at UserSearchPage
		assertNoResults()
	}
}
