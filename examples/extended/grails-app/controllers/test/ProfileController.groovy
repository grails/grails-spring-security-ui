package  test

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.validation.ValidationException

class ProfileController extends grails.plugin.springsecurity.ui.AbstractS2UiDomainController {

     ProfileService profileService
    
        static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

        def search() {

            }

            protected Class<?> getClazz() { Profile }
            protected String getClassLabelCode() { 'profile.label' }

            protected Map model(profile, String action) {

                    [ profile: profile]

            }
    
        def index(Integer max) {
            params.max = Math.min(max ?: 10, 100)
            params.offset = params.offset ?: 0
            def model = [:]
            model.results = profileService.list(params)
            model.totalCount = profileService.count()
            addQueryParamsToModelForPaging(model,
              
               'myQuestion1','myAnswer1',
               
               'myQuestion2','myAnswer2',
               
            'id', 'user.id'
            )
            render view: 'index', model: model
        }
    
        def show(Long id) {
            respond profileService.get(id)
        }
    
        def create() {
            respond new Profile(params), model : ['users': User.list(),'lookupProp':SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName]
        }
    
        def save(Profile profile) {
            if (profile == null) {
                notFound()
                return
            }
    
            try {
                profileService.save(profile)
            } catch (ValidationException e) {
                respond profile.errors, view:'create', model : ['users': User.list(),'lookupProp':SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName]
                return
            }
    
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'profile.label', default: 'Security Questions'), profile.id])
                    redirect profile
                }
                '*' { respond profile, [status: CREATED] }
            }
        }
    
        def edit(Long id) {
            respond profileService.get(id), model : ['users': User.list(),'lookupProp':SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName]
        }
    
        def update(Profile profile) {
            if (profile == null) {
                notFound()
                return
            }
    
            try {
                profileService.save(profile)
            } catch (ValidationException e) {
                respond profile.errors, view:'edit'
                return
            }
    
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'profile.label', default: 'Security Questions'), profile.id])
                    redirect profile
                }
                '*'{ respond profile, [status: OK] }
            }
        }
    
        def delete(Long id) {
            if (id == null) {
                notFound()
                return
            }
    
            profileService.delete(id)
    
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'profile.label', default: 'Profile'), id])
                    redirect action:"index", method:"GET"
                }
                '*'{ render status: NO_CONTENT }
            }
        }
    
        protected void notFound() {
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.not.found.message', args: [message(code: 'profile.label', default: 'Profile'), params.id])
                    redirect action: "index", method: "GET"
                }
                '*'{ render status: NOT_FOUND }
            }
        }

}