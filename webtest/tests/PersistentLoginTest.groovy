class PersistentLoginTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/persistentLogin/search'
		assertContentContains 'PersistentLogin Search'
		assertContentDoesNotContain 'Showing'

		form('persistentLoginSearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 10 out of 20.'
	}

	void testFindByUsername() {
		get '/persistentLogin/search'

		form('persistentLoginSearchForm') {
			username = '3'
			click 'search_submit'
		}

		assertContentContains '1 through 5 out of 5.'

		assertContentContains 'persistent_login_test_3'
		assertContentDoesNotContain 'persistent_login_test_1'
		assertContentDoesNotContain 'persistent_login_test_2'
		assertContentDoesNotContain 'persistent_login_test_4'
		assertContentDoesNotContain 'persistent_login_test_5'

		assertContentContains 'series11'
		assertContentContains 'series12'
		assertContentContains 'series13'
		assertContentContains 'series14'
		assertContentContains 'series15'
	}

	void testFindByToken() {
		get '/persistentLogin/search'

		form('persistentLoginSearchForm') {
			token = '3'
			click 'search_submit'
		}

		assertContentContains '1 through 2 out of 2.'

		assertContentContains 'token13'
		assertContentContains 'token3'
	}

	void testFindBySeries() {
		get '/persistentLogin/search'

		form('persistentLoginSearchForm') {
			series = '4'
			click 'search_submit'
		}

		assertContentContains '1 through 2 out of 2.'

		assertContentContains 'series4'
		assertContentContains 'series14'
		assertContentContains 'persistent_login_test_1'
		assertContentContains 'persistent_login_test_3'
	}
}
