/* Copyright 2009-2010 the original author or authors.
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
package grails.plugins.springsecurity.ui

import grails.plugins.springsecurity.ui.AbstractS2UiController

import grails.converters.JSON

import org.springframework.dao.DataIntegrityViolationException

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class RoleController extends AbstractS2UiController {

	def create = {
		[role: lookupRoleClass().newInstance(params)]
	}

	def save = {
		def role = lookupRoleClass().newInstance(params)
		if (!role.save(flush: true)) {
         render view: 'create', model: [role: role]
         return
		}

		flash.message = "${message(code: 'default.created.message', args: [message(code: 'role.label', default: 'Role'), role.id])}"
		redirect action: edit, id: role.id
	}

	def edit = {
		def role = params.name ? lookupRoleClass().findByAuthority(params.name) : null
		if (!role) role = findById()
		if (!role) return

		setIfMissing 'max', 10, 100
		def users = lookupUserRoleClass().findAllByRole(role, params)*.user
		int userCount = lookupUserRoleClass().countByRole(role)

		[role: role, users: users, userCount: userCount]
	}

	def update = {
		def role = findById()
		if (!role) return
		if (!versionCheck('role.label', 'Role', role, [role: role])) {
			return
		}

		if (!springSecurityService.updateRole(role, params)) {
			render view: 'edit', model: [role: role]
			return
		}

		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'role.label', default: 'Role'), role.id])}"
		redirect action: edit, id: role.id
	}

	def delete = {
		def role = findById()
		if (!role) return

		try {
			lookupUserRoleClass().removeAll role
			springSecurityService.deleteRole(role)
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'role.label', default: 'Role'), params.id])}"
			redirect action: search
		}
		catch (DataIntegrityViolationException e) {
			flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'role.label', default: 'Role'), params.id])}"
			redirect action: edit, id: params.id
		}
	}

	def search = {}

	def roleSearch = {

		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0

		String name = params.authority ?: 'ROLE_'
		def roles = search(name, false, 10, params.int('offset'))
		if (roles.size() == 1 && !useOffset) {
			forward action: 'edit', params: [name: roles[0].authority]
			return
		}

		String hql =
			"SELECT COUNT(DISTINCT r) " +
			"FROM ${lookupRoleClassName()} r " +
			"WHERE LOWER(r.authority) LIKE :name"
		int total = lookupRoleClass().executeQuery(hql, [name: "%${name.toLowerCase()}%"])[0]

		render view: 'search', model: [results: roles,
		                               totalCount: total,
		                               authority: params.authority,
		                               searched: true]
	}

	/**
	 * Ajax call used by autocomplete textfield.
	 */
	def ajaxRoleSearch = {

		def jsonData = []

		if (params.term?.length() > 1) {
			setIfMissing 'max', 10, 100
			setIfMissing 'offset', 0

			def names = search(params.term, true, params.int('max'), params.int('offset'))
			for (name in names) {
				jsonData << [value: name]
			}
		}

		render text: jsonData as JSON, contentType: 'text/plain'
	}

	protected search(String name, boolean nameOnly, int max, int offset) {
		String hql =
			"SELECT DISTINCT ${nameOnly ? 'r.authority' : 'r'} " +
			"FROM ${lookupRoleClassName()} r " +
			"WHERE LOWER(r.authority) LIKE :name " +
			"ORDER BY r.authority"
		lookupRoleClass().executeQuery hql, [name: "%${name.toLowerCase()}%"], [max: max, offset: offset]
	}

	protected findById() {
		def role = lookupRoleClass().get(params.id)
		if (!role) {
			flash.error = "${message(code: 'default.not.found.message', args: [message(code: 'role.label', default: 'Role'), params.id])}"
			redirect action: search
		}

		role
	}
}
