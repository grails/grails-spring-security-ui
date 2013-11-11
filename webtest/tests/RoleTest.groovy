class RoleTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/role/search'
		assertContentContains 'Role Search'
		assertContentDoesNotContain 'Showing'

		form('roleSearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 10 out of 162.'
		assertContentContains '17'
	}

	void testFindByAuthority() {
		get '/role/search'

		form('roleSearchForm') {
			authority = 'ad'
			click 'search_submit'
		}

		assertContentContains '1 through 10 out of 11.'

		assertContentContains 'ROLE_ADMIN'
		assertContentContains 'ROLE_INSTEAD'
	}

	void testUniqueName() {
		get '/role/create'
		assertContentContains 'Create Role'

		form('roleCreateForm') {
			authority = 'ROLE_ADMIN'
			click 'create_submit'
		}

		assertContentContains 'must be unique'
	}

	void testCreateAndEdit() {

		String newName = 'ROLE_NEW_TEST' + System.currentTimeMillis()
		// make sure it doesn't exist
		get '/role/search'
		form('roleSearchForm') {
			authority = newName
			click 'search_submit'
		}
		assertContentContains 'No results'

		// create
		get '/role/create'
		assertContentContains 'Create Role'

		form('roleCreateForm') {
			authority = newName
			click 'create_submit'
		}
		assertContentContains 'Edit Role'
		assertContentContains newName

		// edit
		form('roleEditForm') {
			authority = newName + '_new'
			click 'update_submit'
		}
		assertContentContains 'Edit Role'
		assertContentContains newName + '_new'

		// delete
		String instanceId = findHiddenId()
		post('/role/delete') {
			id = instanceId
		}

		assertContentContains "Role $instanceId deleted"
	}
}
