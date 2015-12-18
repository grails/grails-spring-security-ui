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
package grails.plugin.springsecurity.ui

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils

import org.springframework.dao.DataIntegrityViolationException

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class PersistentLoginController extends AbstractS2UiController {

	def edit() {
		def persistentLogin = findById()
		if (!persistentLogin) return

			[persistentLogin: persistentLogin]
	}

	def update() {
		def persistentLogin = findById()
		if (!persistentLogin) return
			if (!versionCheck('persistentLogin.label', 'PersistentLogin', persistentLogin, [persistentLogin: persistentLogin])) {
				return
			}

		if (!springSecurityUiService.updatePersistentLogin(persistentLogin, params)) {
			render view: 'edit', model: [persistentLogin: persistentLogin]
			return
		}

		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'persistentLogin.label', default: 'PersistentLogin'), persistentLogin.id])}"
		redirect action: 'edit', id: persistentLogin.id
	}

	def delete() {
		def persistentLogin = findById()
		if (!persistentLogin) return

			try {
				springSecurityUiService.deletePersistentLogin persistentLogin
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'persistentLogin.label', default: 'PersistentLogin'), params.id])}"
				redirect action: 'search'
			}
			catch (DataIntegrityViolationException e) {
				flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'persistentLogin.label', default: 'PersistentLogin'), params.id])}"
				redirect action: 'edit', id: params.id
			}
	}

	def search() {}

	def persistentLoginSearch() {

		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0

		Integer max = params.int('max')
		Integer offset = params.int('offset')

		def cs = lookupPersistentLoginClass().createCriteria()
		def results = cs.list(max: max, offset: offset) {
			firstResult: offset
			maxResults: max
			for (name in ['username', 'series', 'token']) {
				String param = params[name]
				if (param) {
					if (name == 'series') name = 'id' // aliased primary key
					ilike(name,'%' + param + '%')
				}
			}
			
			if (params.sort) {
				order(params.sort,params.order ?: 'ASC')
			}
		}
		def model = [results: results, totalCount: results.totalCount, searched: true]

		// add query params to model for paging
		for (name in ['username', 'series', 'token']) {
			model[name] = params[name]
		}

		render view: 'search', model: model
	}

	/**
	 * Ajax call used by autocomplete textfield.
	 */
	def ajaxPersistentLoginSearch() {

		def jsonData = []

		if (params.term?.length() > 2) {
			String username = params.term

			setIfMissing 'max', 10, 100
			
			def cs = lookupPersistentLoginClass().createCriteria()
			def results = cs.list(max: params.int('max')) {
				maxResults: params.int('max')
				ilike('username','%' + username + '%')			
				order('username','DESC')
				projections{
					distinct('username')
				}
			}	
			for (result in results) {
				jsonData << [value: result]
			}
		}

		render text: jsonData as JSON, contentType: 'text/plain'
	}

	protected findById() {
		def persistentLogin = lookupPersistentLoginClass().get(params.id)
		if (!persistentLogin) {
			flash.error = "${message(code: 'default.not.found.message', args: [message(code: 'persistentLogin.label', default: 'PersistentLogin'), params.id])}"
			redirect action: 'search'
		}

		persistentLogin
	}

	protected String lookupPersistentLoginClassName() {
		SpringSecurityUtils.securityConfig.rememberMe.persistentToken.domainClassName
	}

	protected Class<?> lookupPersistentLoginClass() {
		grailsApplication.getDomainClass(lookupPersistentLoginClassName()).clazz
	}
}
