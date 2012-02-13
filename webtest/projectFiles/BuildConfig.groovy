grails.project.plugins.dir = 'plugins'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir	= 'target/test-reports'

grails.project.dependency.resolution = {
	inherits 'global'
	log 'warn'
	repositories {
		grailsPlugins()
		grailsHome()
	}
	dependencies {
		runtime 'com.h2database:h2:1.2.131'
		test('dumbster:dumbster:1.6') {
			excludes 'mail', 'activation'
		}
	}
}
