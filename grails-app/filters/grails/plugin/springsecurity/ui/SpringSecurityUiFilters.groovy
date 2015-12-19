/* Copyright 2015 the original author or authors.
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
package grails.plugin.springsecurity.ui

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ui.strategy.PropertiesStrategy

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class SpringSecurityUiFilters {

	/** Dependency injection for the 'uiPropertiesStrategy' bean. */
	PropertiesStrategy uiPropertiesStrategy

	def filters = {
		ui() {
			after = { Map model ->
				if (model == null) {
					return
				}

				def conf = SpringSecurityUtils.securityConfig
				model.layoutRegister = conf.ui.gsp.layoutRegister ?: 'register'
				model.layoutUi = conf.ui.gsp.layoutUi ?: 'springSecurityUI'
				model.securityConfig = conf
				model.uiPropertiesStrategy = uiPropertiesStrategy
			}
		}
	}
}
