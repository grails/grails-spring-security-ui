package module

import geb.Module

/**
 * Module representing the Roles tab which is displayed on the User edit page
 */
class RolesTab extends Module {

    static content = {
        tab { $("a", href: "#tab-roles") }
    }

    void select() {
        tab.click()
        waitFor { $("li", "aria-controls": "tab-roles", "aria-selected": "true") }
    }

    int totalRoles() {
        return $("input", type: "checkbox", id: startsWith("ROLE_")).size().toInteger()
    }

    int totalEnabledRoles() {
        return $("input", type: "checkbox", id: startsWith("ROLE_"), checked: "checked").size().toInteger()
    }

    void enableRole(String roleName) {
        $("input", type: "checkbox", id: roleName).value(true)
    }
}
