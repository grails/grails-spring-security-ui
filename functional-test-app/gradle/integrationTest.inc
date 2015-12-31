
integrationTest {
	def file = new File('testconfig')
	assert file.exists()
	String testconfig = file.text.trim().toLowerCase()
	def tests
	def sharedTests = ['Register', 'RegistrationCode', 'Requestmap', 'Role', 'User']
	def extendedTests = ['AclClass', 'AclEntry', 'AclObjectIdentity', 'AclSid', 'ExtendedMenu', 'ExtendedSecurityInfo', 'PersistentLogin']
	def simpleTests = ['DefaultMenu', 'DefaultSecurityInfo']
	switch (testconfig) {
		case 'extended': tests = sharedTests + extendedTests; break
		case 'simple':   tests = sharedTests + simpleTests; break
		default: throw new GradleException('File testconfig must exist and contain either extended or simple')
	}

	tests.each { include "**/${it}Spec.class" }

	doFirst {
		logger.quiet "\n - Running test(s) $tests for configuration '$testconfig' with Grails $grailsVersion"
	}

	beforeTest { descriptor -> logger.quiet " -- $descriptor" }
}
