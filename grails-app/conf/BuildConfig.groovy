if (System.getenv('TRAVIS_BRANCH')) {
	grails.project.repos.grailsCentral.username = System.getenv('GRAILS_CENTRAL_USERNAME')
	grails.project.repos.grailsCentral.password = System.getenv('GRAILS_CENTRAL_PASSWORD')
}

grails.project.work.dir = 'target'
grails.project.docs.output.dir = 'target/docs/manual'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}

	plugins {
		compile ':spring-security-core:2.0.0'
		compile ':asset-pipeline:2.6.10'

		compile ':spring-security-acl:2.0.1', {
			export = false
		}

		build ':release:3.1.2', ':rest-client-builder:2.1.1', {
			export = false
		}
	}
}
