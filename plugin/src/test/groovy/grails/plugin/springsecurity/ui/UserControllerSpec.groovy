package grails.plugin.springsecurity.ui

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class UserControllerSpec extends Specification implements ControllerUnitTest<UserController> {
    static final Map ADMIN_ROLE = [authority: "ROLE_ADMIN"]
    static final Map SUPER_ADMIN_ROLE = [authority: "ROLE_SUPER_ADMIN"]
    static final Map USER_ROLE = [authority: "ROLE_USER"]

    void "verify proper construction of roleMap for user with roles #rolesAssignedToUser"() {
        given: "the authority name field has been set to the default name of 'authority'"
        controller.authorityNameField = "authority"

        and: "we mock the returning of all Role instances within the database"
        controller.metaClass.sortedRoles = {
            [
                    ADMIN_ROLE,
                    SUPER_ADMIN_ROLE,
                    USER_ROLE
            ]
        }

        when: "we call buildRoleMap with the role names associated to the user"
        Map results = controller.buildRoleMap(rolesAssignedToUser)

        then: "the user is only granted access to roles with which they are associated"
        results == expectedResults

        where:
        rolesAssignedToUser                                | expectedResults
        [ADMIN_ROLE.authority, USER_ROLE.authority] as Set | [(ADMIN_ROLE): true, (SUPER_ADMIN_ROLE): false, (USER_ROLE): true]
        [] as Set                                          | [:]
        null                                               | [:]
    }
}
