package spec
import spock.lang.IgnoreIf

@IgnoreIf({
	if (!System.getProperty('geb.env')) {
		return true
	}
	if (System.getProperty('geb.env') == 'phantomjs' && !System.getProperty('phantomjs.binary.path')) {
		return true
	}
	if (System.getProperty('geb.env') == 'chrome' && !System.getProperty('webdriver.chrome.driver')) {
		return true
	}
	false
})
class ExtendedMenuSpec extends AbstractSecuritySpec {

	void testIndex() {
		when:
		go ''
		String html = pageSource

		then:
		html.contains 'Spring Security Management Console'

		html.contains 'Users'

		html.contains 'Roles'

		html.contains 'Requestmaps'

		html.contains 'Registration Code'

		html.contains 'Configuration'
		html.contains 'Mappings'
		html.contains 'Current Authentication'
		html.contains 'User Cache'
		html.contains 'Filter Chains'
		html.contains 'Logout Handlers'
		html.contains 'Voters'
		html.contains 'Authentication Providers'

		html.contains 'Persistent Logins'

		html.contains 'ACL'
		html.contains 'SID'
		html.contains 'OID'
		html.contains 'Entry'
	}
}
