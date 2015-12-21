/* Copyright 2009-2015 the original author or authors.
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
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ui.strategy.DefaultAclStrategy
import grails.plugin.springsecurity.ui.strategy.DefaultErrorsStrategy
import grails.plugin.springsecurity.ui.strategy.DefaultPersistentLoginStrategy
import grails.plugin.springsecurity.ui.strategy.DefaultPropertiesStrategy
import grails.plugin.springsecurity.ui.strategy.DefaultQueryStrategy
import grails.plugin.springsecurity.ui.strategy.DefaultRegistrationCodeStrategy
import grails.plugin.springsecurity.ui.strategy.DefaultRequestmapStrategy
import grails.plugin.springsecurity.ui.strategy.DefaultRoleStrategy
import grails.plugin.springsecurity.ui.strategy.DefaultUserStrategy
import grails.plugin.springsecurity.ui.strategy.MailPluginMailStrategy

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class SpringSecurityUiGrailsPlugin {

	String version = '1.0-RC3'
	String grailsVersion = '2.3.0 > *'
	List loadAfter = ['springSecurityCore', 'springSecurityAcl']
	List pluginExcludes = [
		'docs/**',
		'src/docs/**',
		'scripts/CreateS2UiTestApps.groovy'
	]

	String author = 'Burt Beckwith'
	String authorEmail = 'burt@burtbeckwith.com'
	String title = 'Spring Security UI'
	String description = 'User interface extensions for the Spring Security plugin'
	String documentation = 'http://grails-plugins.github.io/grails-spring-security-ui/'
	String license = 'APACHE'
	def organization = [name: 'Grails', url: 'http://www.grails.org/']
	def issueManagement = [url: 'https://github.com/grails-plugins/grails-spring-security-ui/issues']
	def scm = [url: 'https://github.com/grails-plugins/grails-spring-security-ui/']

	private Logger log = LoggerFactory.getLogger('grails.plugin.springsecurity.ui.SpringSecurityUiGrailsPlugin')

	def doWithSpring = {

		def conf = SpringSecurityUtils.securityConfig
		if (!conf || !conf.active) {
			return
		}

		boolean printStatusMessages = (conf.printStatusMessages instanceof Boolean) ? conf.printStatusMessages : true

		if (printStatusMessages) {
			println '\nConfiguring Spring Security UI ...'
		}

		SpringSecurityUtils.loadSecondaryConfig 'DefaultUiSecurityConfig'

		def serviceRef = { springSecurityUiService = ref('springSecurityUiService') }
		uiAclStrategy DefaultAclStrategy, serviceRef
		uiErrorsStrategy DefaultErrorsStrategy, serviceRef
		uiPersistentLoginStrategy DefaultPersistentLoginStrategy, serviceRef
		uiPropertiesStrategy DefaultPropertiesStrategy, serviceRef
		uiQueryStrategy DefaultQueryStrategy, serviceRef
		uiRegistrationCodeStrategy DefaultRegistrationCodeStrategy, serviceRef
		uiRequestmapStrategy DefaultRequestmapStrategy, serviceRef
		uiRoleStrategy DefaultRoleStrategy, serviceRef
		uiUserStrategy DefaultUserStrategy, serviceRef

		uiMailStrategy(MailPluginMailStrategy) { bean ->
			// can't explicitly add a dependency for the mailService bean (mailService = ref('mailService'))
			// since the mail plugin might not be installed
			bean.autowire = 'byName'
		}

		if (printStatusMessages) {
			println '... finished configuring Spring Security UI\n'
		}
	}

	def doWithApplicationContext = { ctx ->

		def conf = SpringSecurityUtils.securityConfig
		if (!conf || !conf.active) {
			return
		}

		if (log.traceEnabled) {
			// redisplay here to show the merged config
			def sb = new StringBuilder('Spring Security configuration:\n')
			def flatConf = conf.flatten()
			for (key in flatConf.keySet().sort()) {
				def value = flatConf[key]
				sb << '\t' << key << ': '
				if (value instanceof Closure) {
					sb << '(closure)'
				}
				else {
					try {
						sb << value.toString() // eagerly convert to string to catch individual exceptions
					}
					catch (e) {
						sb << '(an error occurred: ' << e.message << ')'
					}
				}
				sb << '\n'
			}
			log.trace sb.toString()
		}

		// can't use InitializingBean because of circular references with the strategy classes
		ctx.springSecurityUiService.initialize()
	}
}
