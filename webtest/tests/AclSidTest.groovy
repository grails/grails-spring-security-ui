class AclSidTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/aclSid/search'
		assertContentContains 'AclSid Search'
		assertContentDoesNotContain 'Showing'

		form('aclSidSearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 3 out of 3.'
	}

	void testFindBySid() {
		get '/aclSid/search'

		form('aclSidSearchForm') {
			sid = 'user'
			click 'search_submit'
		}

		assertContentContains '1 through 2 out of 2.'

		assertContentContains 'user1'
		assertContentContains 'user2'
	}

	void testFindByPrincipal() {
		get '/aclSid/search'

		form('aclSidSearchForm') {
			principal = '1'
			click 'search_submit'
		}

		assertContentContains 'user1'
		assertContentContains 'user2'
		assertContentContains 'admin'
	}

	void testUniqueName() {
		get '/aclSid/create'
		assertContentContains 'Create AclSid'

		form('aclSidCreateForm') {
			sid = 'user1'
			principal = true
			click 'create_submit'
		}

		assertContentContains 'must be unique'
	}

	void testCreateAndEdit() {

		String newName = 'newuser' + System.currentTimeMillis()
		// make sure it doesn't exist
		get '/aclSid/search'
		form('aclSidSearchForm') {
			sid = newName
			click 'search_submit'
		}
		assertContentContains 'No results'

		// create
		get '/aclSid/create'
		assertContentContains 'Create AclSid'

		form('aclSidCreateForm') {
			sid = newName
			principal = true
			click 'create_submit'
		}
		assertContentContains 'Edit AclSid'
		assertContentContains newName

		// edit
		form('aclSidEditForm') {
			sid = newName + '_new'
			click 'update_submit'
		}
		assertContentContains 'Edit AclSid'
		assertContentContains newName + '_new'
	}
}
