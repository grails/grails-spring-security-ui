class AclClassTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/aclClass/search'
		assertContentContains 'AclClass Search'
		assertContentDoesNotContain 'Showing'

		form('aclClassSearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 1 out of 1.'
	}

	void testFindByName() {
		get '/aclClass/search'

		form('aclClassSearchForm') {
			className = 'report'
			click 'search_submit'
		}

		assertContentContains '1 through 1 out of 1.'

		assertContentContains 'com.burtbeckwith.testapp.domain.Report'
	}

	void testUniqueName() {
		get '/aclClass/create'
		assertContentContains 'Create AclClass'

		form('aclClassCreateForm') {
			className = 'com.burtbeckwith.testapp.domain.Report'
			click 'create_submit'
		}

		assertContentContains 'must be unique'
	}

	void testCreateAndEdit() {

		String newName = 'com.some.domain.Clazz' + System.currentTimeMillis()
		// make sure it doesn't exist
		get '/aclClass/search'
		form('aclClassSearchForm') {
			className = newName
			click 'search_submit'
		}
		assertContentContains 'No results'

		// create
		get '/aclClass/create'
		assertContentContains 'Create AclClass'

		form('aclClassCreateForm') {
			className = newName
			click 'create_submit'
		}
		assertContentContains 'Edit AclClass'
		assertContentContains newName

		// edit
		form('aclClassEditForm') {
			className = newName + '_new'
			click 'update_submit'
		}
		assertContentContains 'Edit AclClass'
		assertContentContains newName + '_new'

		// delete
		String instanceId = findHiddenId()
		post('/aclClass/delete') {
			id = instanceId
		}

		assertContentContains "AclClass $instanceId deleted"
	}
}
