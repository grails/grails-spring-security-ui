class AclObjectIdentityTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/aclObjectIdentity/search'
		assertContentContains 'AclObjectIdentity Search'
		assertContentDoesNotContain 'Showing'

		form('aclObjectIdentitySearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 10 out of 100.'
	}

	void testFindById() {
		get '/aclObjectIdentity/search'

		form('aclObjectIdentitySearchForm') {
			objectId = '10'
			click 'search_submit'
		}

		assertContentContains '1 through 1 out of 1.'

		assertContentContains '10'
		assertContentContains 'com.burtbeckwith.testapp.domain.Report'
	}

	void testFindByOwner() {
		get '/aclObjectIdentity/search'

		form('aclObjectIdentitySearchForm') {
			ownerSid = '1'
			click 'search_submit'
		}

		assertContentContains '1 through 10 out of 98.'
	}

	void testUniqueId() {
		get '/aclObjectIdentity/create'
		assertContentContains 'Create AclObjectIdentity'

		form('aclObjectIdentityCreateForm') {
			selects['aclClass.id'].select '1'
			objectId = 1
			selects['owner.id'].select '2'
			click 'create_submit'
		}

		assertContentContains 'must be unique'
	}

	void testCreateAndEdit() {

		String newId = Math.abs(new Random().nextInt())
		// make sure it doesn't exist
		get '/aclObjectIdentity/search'
		form('aclObjectIdentitySearchForm') {
			objectId = newId
			click 'search_submit'
		}
		assertContentContains 'No results'

		// create
		get '/aclObjectIdentity/create'
		assertContentContains 'Create AclObjectIdentity'

		form('aclObjectIdentityCreateForm') {
			selects['aclClass.id'].select '1'
			objectId = newId
			selects['owner.id'].select '2'
			click 'create_submit'
		}
		assertContentContains 'Edit AclObjectIdentity'
		assertContentContains newId

		// edit
		form('aclObjectIdentityEditForm') {
			objectId = (newId.toInteger() + 1).toString()
			click 'update_submit'
		}
		assertContentContains 'Edit AclObjectIdentity'
		assertContentContains((newId.toInteger() + 1).toString())
	}
}
