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
package grails.plugin.springsecurity.ui

import grails.converters.JSON

import org.springframework.dao.DataIntegrityViolationException

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class AclSidController extends AbstractS2UiController {

	def create() {
		[aclSid: lookupClass().newInstance(params)]
	}

	def save() {
		withForm {
			def aclSid = lookupClass().newInstance(params)
			if (!aclSid.save(flush: true)) {
				render view: 'create', model: [aclSid: aclSid]
				return
			}

			flash.message = "${message(code: 'default.created.message', args: [message(code: 'aclSid.label', default: 'AclSid'), aclSid.id])}"
			redirect action: 'edit', id: aclSid.id
		}.invalidToken {
			response.status = 500
			log.warn("User: ${springSecurityService.currentUser.id} possible CSRF or double submit: $params")
			flash.message = "${message(code: 'spring.security.ui.invalid.save.form', args: [params.className])}"
			redirect action: 'create'
			return
		}
	}

	def edit() {
		def aclSid = findById()
		if (!aclSid) return

		[aclSid: aclSid]
	}

	def update() {
		withForm {
			def aclSid = findById()
			if (!aclSid) return
			if (!versionCheck('aclSid.label', 'AclSid', aclSid, [aclSid: aclSid])) {
				return
			}

			if (!springSecurityUiService.updateAclSid(aclSid, params.sid, params.principal == 'on')) {
				render view: 'edit', model: [aclSid: aclSid]
				return
			}

			flash.message = "${message(code: 'default.updated.message', args: [message(code: 'aclSid.label', default: 'AclSid'), aclSid.id])}"
			redirect action: 'edit', id: aclSid.id
		}.invalidToken {
			response.status = 500
			log.warn("User: ${springSecurityService.currentUser.id} possible CSRF or double submit: $params")
			flash.message = "${message(code: 'spring.security.ui.invalid.update.form', args: [params.className])}"
			redirect action: 'search'
			return
		}
	}

	def delete() {
		def aclSid = findById()
		if (!aclSid) return

		try {
			withForm {
				springSecurityUiService.deleteAclSid aclSid
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'aclSid.label', default: 'AclSid'), params.id])}"
				redirect action: 'search'
			}.invalidToken {
				response.status = 500
				log.warn("User: ${springSecurityService.currentUser.id} possible CSRF or double submit: $params")
				flash.message = "${message(code: 'spring.security.ui.invalid.delete.form', args: [params.className])}"
				redirect action: 'search'
			}
		}
		catch (DataIntegrityViolationException e) {
			flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'aclSid.label', default: 'AclSid'), params.id])}"
			redirect action: 'edit', id: params.id
		}
	}

	def search() {
		[principal: 0]
	}

	def aclSidSearch() {

		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0
		
		Integer max = params.int('max')
		Integer offset = params.int('offset')
		
		def cs = lookupClass().createCriteria()
		
		def results = cs.list(max: max, offset: offset) {
			firstResult: offset
			maxResults: max
			if(params['sid']) {
				ilike('sid','%' + params['sid'] + '%')
			}
			Integer value = params.int('principal')
			if (value) {
				eq('principal',value == 1)
			}
			if (params.sort) {
				order(params.sort,params.order ?: 'ASC')
			}
		}
		def model = [results: results, totalCount: results.totalCount, searched: true]

		// add query params to model for paging
		for (name in ['sid', 'principal']) {
		 	model[name] = params[name]
		}

		render view: 'search', model: model
	}

	/**
	 * Ajax call used by autocomplete textfield.
	 */
	def ajaxAclSidSearch() {

		def jsonData = []

		if (params.term?.length() > 2) {
			String sid = params.term
			setIfMissing 'max', 10, 100

			def cs = lookupClass().createCriteria()
			
			def results = cs.list(max: params.int('max')) {
				maxResults: params.int('max')
				ilike('sid','%' + sid + '%')
				order('sid','DESC')
				projections{
					distinct('sid')
				}
			}
			for (result in results) {
				jsonData << [value: result]
			}
		}

		render text: jsonData as JSON, contentType: 'text/plain'
	}

	protected findById() {
		def aclSid = lookupClass().get(params.id)
		if (!aclSid) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'aclSid.label', default: 'AclSid'), params.id])}"
			redirect action: 'search'
		}

		aclSid
	}

	protected String lookupClassName() {
		'grails.plugin.springsecurity.acl.AclSid'
	}

	protected Class<?> lookupClass() {
		grailsApplication.getDomainClass(lookupClassName()).clazz
	}
}
