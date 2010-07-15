grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir	= 'target/test-reports'
grails.project.docs.output.dir = 'docs' // for backwards-compatibility, the docs are checked into gh-pages branch

grails.project.dependency.resolution = {

	inherits('global')

	log 'warn'

	repositories {
		grailsPlugins()
		grailsHome()
		grailsCentral()

		mavenCentral()
		mavenRepo 'http://download.java.net/maven/2/'
	}

	dependencies {
		test('dumbster:dumbster:1.6') {
			excludes 'mail', 'activation'
		}
	}
}
