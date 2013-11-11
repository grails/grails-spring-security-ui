class UserTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/user/search'
		assertContentContains 'User Search'
		assertContentDoesNotContain 'Showing'

		form('userSearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 10 out of 151436'
		assertContentContains '15144'
	}

	void testFindByUsername() {
		get '/user/search'

		form('userSearchForm') {
			username = 'foo'
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 10 out of 19'

		assertContentContains 'foon_2'
		assertContentContains 'foolkiller'
		assertContentContains 'foostra'
	}

	void testFindByDisabled() {
		get '/user/search'

		form('userSearchForm') {
			enabled = '-1'
			click 'search_submit'
		}

		assertContentContains 'szhang1999'
		assertContentContains 'Showing 1 through 1 out of 1.'
	}

	void testFindByAccountExpired() {
		get '/user/search'

		form('userSearchForm') {
			accountExpired = '1'
			click 'search_submit'
		}

		assertContentContains 'achen'
		assertContentContains 'szhang1999'
		assertContentContains 'aaaaaasd'
		assertContentContains 'Showing 1 through 3 out of 3.'
	}

	void testFindByAccountLocked() {
		get '/user/search'

		form('userSearchForm') {
			accountLocked = '1'
			click 'search_submit'
		}

		assertContentContains 'achen'
		assertContentContains 'szhang1999'
		assertContentContains 'aaaaaasd'
		assertContentContains 'Showing 1 through 3 out of 3.'
	}

	void testFindByPasswordExpired() {
		get '/user/search'

		form('userSearchForm') {
			passwordExpired = '1'
			click 'search_submit'
		}

		assertContentContains 'achen'
		assertContentContains 'szhang1999'
		assertContentContains 'aaaaaasd'
		assertContentContains 'Showing 1 through 3 out of 3.'
	}

	void testEdit() {
		get '/user/edit/201050'
		assertContentContains 'Edit User'
		assertContentContains 'aaaaaasd'
		assertContentDoesNotContain '<input type="checkbox" name="ROLE_ADMIN" checked="checked" id="ROLE_ADMIN"  />'

//		form('userEditForm') {
//			passwordExpired = '1'
//			click 'update_submit'
//		}
	}
}
