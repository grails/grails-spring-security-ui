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
		build(':release:1.0.0') { export = false }
		
		compile ':jquery:1.7.1'
		compile ':jquery-ui:1.8.15'
		compile ':mail:0.9'
		compile ':spring-security-acl:1.1'
		compile ':spring-security-core:1.2.7'
		compile ':famfamfam:1.0'
	}
}
