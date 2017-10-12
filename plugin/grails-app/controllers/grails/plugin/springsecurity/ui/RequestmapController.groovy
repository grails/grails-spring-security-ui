/* Copyright 2009-2016 the original author or authors.
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

import grails.plugin.springsecurity.ReflectionUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ui.strategy.RequestmapStrategy
import org.springframework.http.HttpMethod

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class RequestmapController extends AbstractS2UiDomainController {

	/** Dependency injection for the 'uiRequestmapStrategy' bean. */
	RequestmapStrategy uiRequestmapStrategy

	def springSecurityService

	def create() {
		super.create()
	}

	def save() {
		withForm {
			if (!(param('url'))) params.remove('url') {
				doSave(uiRequestmapStrategy.saveRequestmap(params)) {
					springSecurityService.clearCachedRequestmaps()
				}
			}
		}.invalidToken {
			response.status = 500
			log.warn("User: ${SpringSecurityUtils.authentication.principal.id} possible CSRF or double submit: $params")
			flash.message = "${message(code: 'spring.security.ui.invalid.save.form', args: [params.username])}"
			redirect(action: "create")
			return
		}
	}

	def edit() {
		super.edit()
	}

	def update() {
		withForm {
			if (!(param('url'))) params.remove('url') {
				doUpdate { requestmap ->
					uiRequestmapStrategy.updateRequestmap params, requestmap
				}
			}
		}.invalidToken {
			response.status = 500
			log.warn("User: ${SpringSecurityUtils.authentication.principal.id} possible CSRF or double submit: $params")
			flash.message = "${message(code: 'spring.security.ui.invalid.update.form', args: [params.username])}"
			redirectToSearch()
			return
		}
	}

	def delete() {
		withForm {
			tryDelete { requestmap ->
				uiRequestmapStrategy.deleteRequestmap requestmap
			}
		}.invalidToken {
			response.status = 500
			log.warn("User: ${SpringSecurityUtils.authentication.principal.id} possible CSRF or double submit: $params")
			flash.message = "${message(code: 'spring.security.ui.invalid.delete.form', args: [params.username])}"
			redirectToSearch()
		}
	}

	def search() {
		if (!isSearch()) {
			// show the form
			return [hasHttpMethod: hasHttpMethod]
		}

		def results = doSearch { ->
			like 'configAttribute', delegate
			like 'url', delegate
			if (param('httpMethod')) {
				eq 'httpMethod', HttpMethod.valueOf(params.httpMethod), delegate
			}
		}

		renderSearch([results: results, totalCount: results.totalCount, hasHttpMethod: hasHttpMethod],
		              'configAttribute', 'httpMethod', 'url')
	}

	protected Class<?> getClazz() { Requestmap }
	protected String getClassLabelCode() { 'requestmap.label' }
	protected Map model(requestmap, String action) {
		[requestmap: requestmap, hasHttpMethod: hasHttpMethod]
	}

	protected boolean hasHttpMethod
	protected Class<?> Requestmap

	void afterPropertiesSet() {
		super.afterPropertiesSet()

		if (!conf.requestMap.className) {
			return
		}

		hasHttpMethod = ReflectionUtils.requestmapClassSupportsHttpMethod()

		Requestmap = getDomainClassClass(conf.requestMap.className)
	}
}
