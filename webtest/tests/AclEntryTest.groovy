class AclEntryTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/aclEntry/search'
		assertContentContains 'AclEntry Search'
		assertContentDoesNotContain 'Showing'

		form('aclEntrySearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 10 out of 175.'
	}

	void testFindByOid() {
		get '/aclEntry/search'

		form('aclEntrySearchForm') {
			aclObjectIdentity = '60'
			click 'search_submit'
		}

		assertContentContains '1 through 2 out of 2.'

		assertContentContains '60'
		assertContentContains '211'
		assertContentContains '212'
		assertContentContains '>user1</a>'
		assertContentContains '>admin</a>'
		assertContentDoesNotContain '>user2</a>'
		assertContentContains 'BasePermission[...............................R=1]'
		assertContentContains 'BasePermission[...........................A....=16]'
	}

	void testFindByAceOrder() {
		get '/aclEntry/search'

		form('aclEntrySearchForm') {
			aceOrder = '2'
			click 'search_submit'
		}

		assertContentContains '1 through 7 out of 7.'

		assertContentContains '93'
		assertContentContains '96'
		assertContentContains '99'
		assertContentContains '113'
		assertContentContains '116'
		assertContentContains '262'
		assertContentContains '265'
	}

	void testFindByMask() {
		get '/aclEntry/search'

		form('aclEntrySearchForm') {
			mask = '1'
			click 'search_submit'
		}

		assertContentContains '1 through 10 out of 72.'
	}

	void testUniqueOrder() {
		get '/aclEntry/create'
		assertContentContains 'Create AclEntry'

		form('aclEntryCreateForm') {
			fields['aclObjectIdentity.id'].value = '10'
			aceOrder = '1'
			selects['sid.id'].select '2'
			mask = '2'
			click 'create_submit'
		}

		assertContentContains 'must be unique'
	}

	void testCreateAndEdit() {

		String newOrder = Math.abs(new Random().nextInt())
		// make sure it doesn't exist
		get '/aclEntry/search'

		form('aclEntrySearchForm') {
			aclObjectIdentity = '10'
			aceOrder = newOrder
			click 'search_submit'
		}

		assertContentContains 'No results'

		// create
		get '/aclEntry/create'
		assertContentContains 'Create AclEntry'

		form('aclEntryCreateForm') {
			fields['aclObjectIdentity.id'].value = '10'
			aceOrder = newOrder
			selects['sid.id'].select '1'
			mask = '2'
			click 'create_submit'
		}
		assertContentContains 'Edit AclEntry'
		assertContentContains newOrder

		// edit
		form('aclEntryEditForm') {
			aceOrder = (newOrder.toInteger() + 1).toString()
			click 'update_submit'
		}
		assertContentContains 'Edit AclEntry'
		assertContentContains((newOrder.toInteger() + 1).toString())

		// delete
		String instanceId = findHiddenId()
		post('/aclEntry/delete') {
			id = instanceId
		}

		assertContentContains "AclEntry $instanceId deleted"
	}
}
