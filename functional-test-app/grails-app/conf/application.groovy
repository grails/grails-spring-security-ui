grails {
    plugin {
        springsecurity {
            rememberMe {
                persistent = System.getProperty('TEST_CONFIG') == 'extended'
            }
        }
    }
}