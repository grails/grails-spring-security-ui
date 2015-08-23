/* Copyright 2009-2013 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.spring.security.ui

import grails.plugin.springsecurity.SpringSecurityUtils

import grails.plugins.*

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class SpringSecurityUiGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.0.4 > *"
	List loadAfter = ['springSecurityCore']	
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
		'docs/**',
		'src/main/docs/**',
		'scripts/CreateS2UiTestApps.groovy',
		'scripts/Fixpdf.groovy',
		'lib/**'		
    ]

    // TODO Fill in these fields
    String title = 'Spring Security UI'
	String author = 'Burt Beckwith'
	String authorEmail = 'burt@burtbeckwith.com'
    String description = 'User interface extensions for the Spring Security plugin'
	
    def profiles = ['web']

    // URL to the plugin's documentation
	String documentation = 'http://grails-plugins.github.io/grails-spring-security-ui/'

    // Extra (optional) plugin metadata

	String license = 'APACHE'
	def organization = [name: 'SpringSource', url: 'http://www.springsource.org/']
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPSPRINGSECURITYUI']
	def scm = [url: 'https://github.com/grails-plugins/grails-spring-security-ui/']

    Closure doWithSpring() { {->
			def conf = SpringSecurityUtils.securityConfig
			if (!conf || !conf.active) {
				return
			}

			println '\nConfiguring Spring Security UI ...'

			SpringSecurityUtils.loadSecondaryConfig 'DefaultUiSecurityConfig'

			println '... finished configuring Spring Security UI\n'
        } 
    }

    void doWithDynamicMethods() {
        // TODO Implement registering dynamic methods to classes (optional)
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
