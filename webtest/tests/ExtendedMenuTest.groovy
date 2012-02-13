class ExtendedMenuTest extends AbstractSecurityWebTest {

	void testIndex() {
		get '/'

		String content = response.contentAsString

		assertTrue content.contains('Spring Security Management Console')

		assertTrue content.contains('Users')

		assertTrue content.contains('Roles')

		assertTrue content.contains('Requestmaps')

		assertTrue content.contains('Registration Code')

		assertTrue content.contains('Configuration')
		assertTrue content.contains('Mappings')
		assertTrue content.contains('Current Authentication')
		assertTrue content.contains('User Cache')
		assertTrue content.contains('Filter Chains')
		assertTrue content.contains('Logout Handlers')
		assertTrue content.contains('Voters')
		assertTrue content.contains('Authentication Providers')

		assertTrue content.contains('Persistent Logins')

		assertTrue content.contains('ACL')
		assertTrue content.contains('SID')
		assertTrue content.contains('OID')
		assertTrue content.contains('Entry')
	}
}
