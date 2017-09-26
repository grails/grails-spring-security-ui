package page

abstract class CreatePage extends AbstractSecurityPage {

	static at = { title == 'Create ' + typeName() }

	static content = {
		form { $('createForm') }
		submit { $('a', id: 'create') }
	}

	boolean assertNotUnique() {
		assertContentContains 'must be unique'
	}
}
