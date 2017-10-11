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
class AclObjectIdentityController extends AbstractS2UiDomainController {

	def create() {
		super.create()
	}

	def save() {
		withForm {
			doSave uiAclStrategy.saveAclObjectIdentity(params)
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
			doUpdate { aclObjectIdentity ->
				uiAclStrategy.updateAclObjectIdentity params, aclObjectIdentity
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
			tryDelete { aclObjectIdentity ->
				uiAclStrategy.deleteAclObjectIdentity aclObjectIdentity
			}
		}.invalidToken {
			response.status = 500
			log.warn("User: ${SpringSecurityUtils.authentication.principal.id} possible CSRF or double submit: $params")
			flash.message = "${message(code: 'spring.security.ui.invalid.delete.form', args: [params.username])}"
			redirectToSearch()
		}
	}

	def search() {
		if (!isSearch('aclClass.id', 'owner.id')) {
			// show the form
			return [classes: AclClass.list(), sids: AclSid.list()]
		}

		def results = doSearch { ->
			eqLongId  'aclClass', delegate
			eqLong    'objectId', delegate
			eqLongId  'owner', delegate
			eqLongId  'parent', delegate
			eqBoolean 'entriesInheriting', delegate
		}

		renderSearch([results: results, totalCount: results.totalCount,
		              classes: AclClass.list(), sids: AclSid.list()],
		             'aclClass.id', 'entriesInheriting', 'objectId', 'owner.id', 'parent.id')
	}

	protected Class<?> getClazz() { AclObjectIdentity }
	protected String getClassLabelCode() { 'aclObjectIdentity.label' }
	protected String getSimpleClassName() { 'AclObjectIdentity' }
	protected Map model(aclObjectIdentity, String action) {
		[aclObjectIdentity: aclObjectIdentity, classes: AclClass.list(), sids: AclSid.list()]
	}

	protected Class<?> AclObjectIdentity

	void afterPropertiesSet() {
		super.afterPropertiesSet()
		AclObjectIdentity = getDomainClassClass('grails.plugin.springsecurity.acl.AclObjectIdentity')
	}
}
