class RequestmapTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/requestmap/search'
		assertContentContains 'Requestmap Search'
		assertContentDoesNotContain 'Showing'

		form('requestmapSearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 2 out of 2.'

		assertContentContains '/j_spring_security_switch_user'
		assertContentContains 'ROLE_RUN_AS,IS_AUTHENTICATED_FULLY'
	}

	void testFindByConfigAttribute() {
		get '/requestmap/search'

		form('requestmapSearchForm') {
			configAttribute = 'run'
			click 'search_submit'
		}

		assertContentContains '1 through 1 out of 1.'

		assertContentContains '/j_spring_security_switch_user'
		assertContentContains 'ROLE_RUN_AS,IS_AUTHENTICATED_FULLY'
	}

	void testFindByUrl() {
		get '/requestmap/search'

		form('requestmapSearchForm') {
			url = 'secure'
			click 'search_submit'
		}

		assertContentContains '1 through 1 out of 1.'

		assertContentContains '/secure/**'
		assertContentContains 'ROLE_ADMIN'
	}

	void testUniqueUrl() {
		get '/requestmap/create'
		assertContentContains 'Create Requestmap'

		form('requestmapCreateForm') {
			url = '/secure/**'
			configAttribute = 'ROLE_FOO'
			click 'create_submit'
		}

		assertContentContains 'must be unique'
	}

	void testCreateAndEdit() {

		String newUrl = '/foo/' + System.currentTimeMillis()
		// make sure it doesn't exist
		get '/requestmap/search'
		form('requestmapSearchForm') {
			url = newUrl
			click 'search_submit'
		}
		assertContentContains 'No results'

		// create
		get '/requestmap/create'
		assertContentContains 'Create Requestmap'

		form('requestmapCreateForm') {
			url = newUrl
			configAttribute = 'ROLE_FOO'
			click 'create_submit'
		}
		assertContentContains 'Edit Requestmap'
		assertContentContains newUrl

		// edit
		form('requestmapEditForm') {
			url = newUrl + '/new'
			click 'update_submit'
		}
		assertContentContains 'Edit Requestmap'
		assertContentContains newUrl + '/new'

		// delete
		String instanceId = findHiddenId()
		post('/requestmap/delete') {
			id = instanceId
		}

		assertContentContains "Requestmap $instanceId deleted"
	}
}
