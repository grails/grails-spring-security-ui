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
		runtime ':famfamfam:1.0'
		runtime ':jquery:1.4.2.5'
		runtime ':jquery-ui:1.8.2.3'
		runtime ':mail:1.0'
		runtime ':spring-security-acl:1.1'
		runtime ':spring-security-core:1.2.7.1'

	}
}
