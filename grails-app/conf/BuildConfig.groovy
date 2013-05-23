grails.project.work.dir = 'target'
grails.project.docs.output.dir = 'docs/manual' // for backwards-compatibility, the docs are checked into gh-pages branch

grails.project.repos.default = 'bintray-stokito-maven-grails-spring-security-ui'
grails.project.repos.'bintray-stokito-maven-grails-spring-security-ui'.url = 'https://api.bintray.com/maven/stokito/maven/grails-spring-security-ui'
grails.project.repos.'bintray-stokito-maven-grails-spring-security-ui'.type = 'maven'

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

    plugins {
        compile ':spring-security-core:1.2.7.2'
        compile ':spring-security-acl:1.1'
        compile ':mail:1.0'
        compile ':jquery:1.7.1'
        compile ':jquery-ui:1.8.15'
        compile ':famfamfam:1.0.1'

        runtime(':resources:1.1.6')
        runtime(':jquery:1.7.1')
        build(":tomcat:${grailsVersion}",
                ':release:2.2.1',
                ':rest-client-builder:1.0.3'
        ) {
            export = false
        }
    }
}
