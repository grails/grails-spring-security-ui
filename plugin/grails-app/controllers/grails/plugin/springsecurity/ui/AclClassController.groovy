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

import grails.plugin.springsecurity.SpringSecurityUtils

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class AclClassController extends AbstractS2UiDomainController {

	def create() {
		super.create()
	}

	def save() {
		withForm {
			doSave uiAclStrategy.saveAclClass(params)
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
			doUpdate { aclClass ->
				uiAclStrategy.updateAclClass params, aclClass
			}
		} {
			response.status = 500
			log.warn("User: ${SpringSecurityUtils.authentication.principal.id} possible CSRF or double submit: $params")
			flash.message = "${message(code: 'spring.security.ui.invalid.update.form', args: [params.username])}"
			redirectToSearch()
			return
		}
	}

	def delete() {
		withForm {
			tryDelete { aclClass ->
				uiAclStrategy.deleteAclClass aclClass
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
			return
		}

		def results = doSearch { ->
			like 'className', delegate
		}

		renderSearch([results: results, totalCount: results.totalCount], 'className')
	}

	protected Class<?> getClazz() { AclClass }
	protected String getClassLabelCode() { 'aclClass.label' }
	protected String getSimpleClassName() { 'AclClass' }
	protected Map model(aclClass, String action) {
		[aclClass: aclClass]
	}
}
