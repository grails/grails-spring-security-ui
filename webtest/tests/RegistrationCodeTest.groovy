class RegistrationCodeTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/registrationCode/search'
		assertContentContains 'Registration Code Search'
		assertContentDoesNotContain 'Showing'

		form('registrationCodeSearchForm') {
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 10 out of 14.'
		assertContentContains 'registration_test_2'
		assertContentContains '0a154624f36d42e4aa68991a9477bd04'
	}

	void testFindByToken() {
		get '/registrationCode/search'

		form('registrationCodeSearchForm') {
			token = '4a7f88afec3746f7aab2f5d0d8df6d8e'
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 1 out of 1.'
		assertContentContains 'registration_test_1'
		assertContentContains '4a7f88afec3746f7aab2f5d0d8df6d8e'
	}

	void testFindByUsername() {
		get '/registrationCode/search'

		form('registrationCodeSearchForm') {
			username = 'registration_test_3'
			click 'search_submit'
		}

		assertContentContains 'Showing 1 through 5 out of 5.'
		assertContentContains 'registration_test_3'
		assertContentContains '89f9bbc658b14808ae4c77c6e17e551a'
	}

	void testEdit() {
		get '/registrationCode/edit/4'

		assertContentContains 'Edit RegistrationCode'
		assertContentContains 'registration_test_1'
		assertContentContains 'a50e061e0e2f424fb7fbc2ff3dae597d'

		form('registrationCodeEditForm') {
			username = 'new_user'
			token = 'new_token'
			click 'update_submit'
		}

		assertContentContains 'Edit RegistrationCode'
		assertContentContains 'new_user'
		assertContentContains 'new_token'

		get '/registrationCode/edit/4'
		assertContentContains 'Edit RegistrationCode'
		assertContentContains 'new_user'
		assertContentContains 'new_token'
	}
}
