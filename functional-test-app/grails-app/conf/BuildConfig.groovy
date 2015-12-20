def file = new File('testconfig')
String testconfig = file.exists() ? file.text.trim() : ''
boolean extended = testconfig == 'extended'
System.setProperty 'testconfig', testconfig

grails.server.port.http = 8238

grails.testing.patterns = ['Register', 'RegistrationCode', 'Requestmap', 'Role', 'User']
if (extended) {
	grails.testing.patterns.addAll 'AclClass', 'AclEntry', 'AclObjectIdentity', 'AclSid',
	                               'ExtendedMenu', 'ExtendedSecurityInfo', 'PersistentLogin'
}
else {
	grails.testing.patterns.addAll 'DefaultMenu', 'DefaultSecurityInfo'
}

grails.servlet.version = '3.0'
grails.project.work.dir = 'target'
grails.project.target.level = 1.7
grails.project.source.level = 1.7

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {
	inherits 'global'
	log 'warn'
	checksums true
	legacyResolve false

	repositories {
		inherits true

		mavenLocal()
		grailsCentral()
		mavenCentral()
	}

	String gebVersion = '0.12.2'
	String seleniumVersion = '2.48.2'

	dependencies {
		runtime 'com.h2database:h2:1.4.190'

		test('dumbster:dumbster:1.6') {
			excludes 'mail', 'activation'
		}

		test "org.gebish:geb-spock:$gebVersion"

		test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
		test "org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion"
		test "org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion"
//		test 'com.github.detro:phantomjsdriver:1.2.0', {
//			transitive = false
//		}
		// TODO switch back to com.github.detro:phantomjsdriver when this
		// issue is resolved: https://github.com/detro/ghostdriver/issues/397
		test 'com.codeborne:phantomjsdriver:1.2.1', {
			transitive = false
		}
	}

	plugins {
		build ':tomcat:8.0.20'
		compile ':asset-pipeline:2.6.10'
		runtime ':hibernate4:4.3.8.1'
		runtime ':mail:1.0.7'

		compile ':spring-security-ui:1.0-SNAPSHOT'

		if (extended) {
			runtime ':spring-security-acl:2.0.1'
		}

		test ":geb:$gebVersion"
	}
}
