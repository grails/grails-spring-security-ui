grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.docs.output.dir = 'docs/manual' // for backwards-compatibility, the docs are checked into gh-pages branch
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.16'
    }

    plugins {
        runtime ":resources:1.1.6"

        build(':release:1.0.0') {
            export = false
        }
        runtime ':famfamfam:1.0.1'
        runtime ':functional-test:1.2.7'
        runtime ":hibernate:$grailsVersion"
        runtime ':jquery:1.7.1'
        runtime ':jquery-ui:1.8.15'
        runtime ':mail:1.0'
        runtime ':spring-security-acl:1.1'
        runtime ':spring-security-core:1.2.7'
        ////build ':svn:1.0.1'
        build ":tomcat:$grailsVersion"
	}
}
