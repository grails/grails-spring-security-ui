modules = {
    'spring-security-ui' {
        dependsOn 'jquery-ui'

        // not sure if line below is required
        //resource url:[plugin: 'spring-security-ui', dir: 'css/smoothness', file: 'jquery-ui-1.8.2.custom.css']

        resource url: [dir: 'css/smoothness', file: 'jquery-ui-1.8.2.custom.css', plugin: 'spring-security-ui']
        resource url: [dir: 'css', file: 'jquery.jgrowl.css', plugin: 'spring-security-ui']
        resource url: [dir: 'css', file: 'jquery.safari-checkbox.css', plugin: 'spring-security-ui']
        resource url: [dir: 'css', file: 'date_input.css', plugin: 'spring-security-ui']
        resource url: [dir: 'css', file: 'jquery.jdMenu.css', plugin: 'spring-security-ui']
        resource url: [dir: 'css', file: 'jquery.jdMenu.slate.css', plugin: 'spring-security-ui']
        resource url: [dir: 'css', file: 'table.css', plugin: 'spring-security-ui']
        resource url: [dir: 'css', file: 'spring-security-ui.css', plugin: 'spring-security-ui']
        resource url: [dir: 'css', file: 'reset.css', plugin: 'spring-security-ui']

        resource url: [dir: 'js/jquery', file: 'jquery.jgrowl.js', plugin: 'spring-security-ui']
        resource url: [dir: 'js/jquery', file: 'jquery.checkbox.js', plugin: 'spring-security-ui']
        resource url: [dir: 'js/jquery', file: 'jquery.date_input.js', plugin: 'spring-security-ui']
        resource url: [dir: 'js/jquery', file: 'jquery.positionBy.js', plugin: 'spring-security-ui']
        resource url: [dir: 'js/jquery', file: 'jquery.bgiframe.js', plugin: 'spring-security-ui']
        resource url: [dir: 'js/jquery', file: 'jquery.jdMenu.js', plugin: 'spring-security-ui']
        resource url: [dir: 'js/jquery', file: 'jquery.dataTables.min.js', plugin: 'spring-security-ui']
        resource url: [dir: 'js', file: 'spring-security-ui.js', plugin: 'spring-security-ui']

    }
    
    'ajax-login' {
        dependsOn 'jquery'
        resource url: [dir: 'js/jquery', file: 'jquery.form.js', plugin: 'spring-security-ui']
        resource url: [dir: 'js', file: 'ajaxLogin.js', plugin:'spring-security-ui']
    }

}