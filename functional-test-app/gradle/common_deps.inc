	compile 'org.grails:grails-dependencies'
	compile 'org.springframework.boot:spring-boot-autoconfigure'
	compile 'org.springframework.boot:spring-boot-starter-logging'
	compile 'org.springframework.boot:spring-boot-starter-tomcat'

	String pluginVersion = file('../version.txt').text.trim()
	compile 'org.grails.plugins:spring-security-ui:' + pluginVersion

	compile 'com.h2database:h2:1.4.190'
	compile 'dumbster:dumbster:1.6', {
		transitive = false
	}

	compile 'org.grails.plugins:mail:2.0.0.RC4'

	runtime 'org.grails.plugins:asset-pipeline'

	testCompile 'org.grails:grails-plugin-testing'
	testCompile 'org.grails.plugins:geb'

	//	testCompile 'com.github.detro:phantomjsdriver:1.2.0'
	testCompile 'com.codeborne:phantomjsdriver:1.2.1' // TODO switch back to com.github.detro:phantomjsdriver when this
	                                                  // issue is resolved: https://github.com/detro/ghostdriver/issues/397

	String seleniumVersion = '2.48.2'
	testCompile "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
	['chrome', 'firefox'].each { String name ->
		testCompile "org.seleniumhq.selenium:selenium-${name}-driver:$seleniumVersion"
	}

	testCompile 'org.gebish:geb-core:0.12.2'

	console 'org.grails:grails-console'
}
