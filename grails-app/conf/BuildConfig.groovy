grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir	= 'target/test-reports'
grails.project.docs.output.dir = 'docs/manual' // for backwards-compatibility, the docs are checked into gh-pages branch

grails.project.dependency.resolution = {

	inherits 'global'

	log 'warn'

	repositories {
		grailsPlugins()
		grailsHome()
		grailsCentral()

		mavenCentral()
		mavenRepo 'http://download.java.net/maven/2/'
	}

	plugins {
		build(':release:1.0.0') {
			export = false
		}
		//build(':famfamfam:1.0') { export = false }
		//build(':jquery:1.4.2.5') { export = false }
		//build(':jquery-ui:1.8.2.3') { export = false }
		//build(':resources:1.1.5') { export = false }
		//build(':spring-security-acl:1.1') { export = false }
		//build(':spring-security-core:1.2.7.1') { export = false }

	}
}
