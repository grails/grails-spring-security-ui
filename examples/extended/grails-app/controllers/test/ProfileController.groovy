package test

import grails.plugin.springsecurity.annotation.Secured

@Secured(["ROLE_USER"])
class ProfileController {
    static scaffold=Profile
}
