class PersistentLoginTest extends AbstractSecurityWebTest {

	void testFindAll() {
		get '/persistentLogin'
		assertContentContains 'PersistentLogin Search'
		// assert not contains table

		form('persistentLoginSearchForm') {
			click 'search_submit'
		}
	}
}
//s2-create-persistent-token