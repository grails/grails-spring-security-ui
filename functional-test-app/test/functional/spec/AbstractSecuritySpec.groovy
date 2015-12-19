package spec

import geb.spock.GebReportingSpec

abstract class AbstractSecuritySpec extends GebReportingSpec {

	void setup() {
		logout()
	}

	protected void logout() {
		go 'j_spring_security_logout'
	}

	protected String getPageSource() {
		browser.driver.pageSource
	}

	protected void assertContentContains(String expected) {
		assert $().text().contains(expected)
	}

	// used to verify hidden content like menus and jGrowl flash messages
	protected void assertHtmlContains(String expected) {
		assert browser.driver.pageSource.contains(expected)
	}

	protected void assertContentContainsOne(String expected1, String expected2) {
		assert $().text().contains(expected1) || $().text().contains(expected2)
	}

	protected void assertContentMatches(String regex) {
		assert $().text() ==~ regex
	}

	protected void assertContentDoesNotContain(String unexpected) {
		assert !$().text().contains(unexpected)
	}
}
