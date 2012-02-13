grails.project.work.dir = 'target'
grails.project.docs.output.dir = 'docs/manual' // for backwards-compatibility, the docs are checked into gh-pages branch

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsPlugins()
		grailsHome()
		grailsCentral()

		mavenLocal()
		mavenCentral()
		mavenRepo 'http://download.java.net/maven/2/'
	}

//	plugins {
//		compile ':spring-security-core:1.2.7.2'
//		compile ':mail:1.0'
//		compile ':jquery:1.7.1'
//		compile ':jquery-ui:1.8.15'
//		compile ':famfamfam:1.0.1'
//	}
}
