grails.controllers.defaultScope = 'singleton'
grails.converters.encoding = 'UTF-8'
grails.enable.native2ascii = true
grails.exceptionresolver.params.exclude = ['password']
grails.hibernate.cache.queries = false
grails.hibernate.pass.readonly = false
grails.hibernate.osiv.readonly = false
grails.json.legacy.builder = false
grails.logging.jul.usebridge = true
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [
	all:           '*/*',
	atom:          'application/atom+xml',
	css:           'text/css',
	csv:           'text/csv',
	form:          'application/x-www-form-urlencoded',
	html:          ['text/html','application/xhtml+xml'],
	js:            'text/javascript',
	json:          ['application/json', 'text/json'],
	multipartForm: 'multipart/form-data',
	rss:           'application/rss+xml',
	text:          'text/plain',
	hal:           ['application/hal+json','application/hal+xml'],
	xml:           ['text/xml', 'application/xml']
]
grails.project.groupId = appName
grails.scaffolding.templates.domainSuffix = ''
grails.spring.bean.packages = []
grails.views.default.codec = 'html'
grails {
	views {
		gsp {
			encoding = 'UTF-8'
			htmlcodec = 'xml'
			codecs {
				expression = 'html'
				scriptlet = 'html'
				taglib = 'none'
				staticparts = 'none'
			}
		}
	}
}
grails.web.disable.multipart = false

environments {
	production {
		grails.logging.jul.usebridge = false
	}
}

log4j.main = {
	appenders {
		file name: 'file', file: 'logs/gra.log', append: false
	}

	error additivity: false, file: ['org.codehaus.groovy.grails', 'org.springframework',
	                                'org.hibernate', 'net.sf.ehcache.hibernate']
	error additivity: false, file: ['grails.plugin.springsecurity']
	debug additivity: false, file: [
//		'org.hibernate.SQL',
//		'org.springframework.security'
	]
	trace additivity: false, file: [
//		'grails.plugin.springsecurity',
//		'grails.plugin.springsecurity.ui',
//		'org.hibernate.type.descriptor.sql.BasicBinder'
	]
}

grails.mail.default.from = 'do.not.reply@server.com'
grails.mail.host = 'localhost'
grails.mail.port = 1025

grails {
	plugin {
		springsecurity {
			filterChain.chainMap = [
			   '/assets/**':      'none',
			   '/**/js/**':       'none',
			   '/**/css/**':      'none',
			   '/**/images/**':   'none',
			   '/**/favicon.ico': 'none',
			   '/**':             'JOINED_FILTERS'
			]
			secureChannel.definition = [
			   '/testsecure/**':   'REQUIRES_SECURE_CHANNEL',
			   '/testinsecure/**': 'REQUIRES_INSECURE_CHANNEL',
			   '/testany/**':      'ANY_CHANNEL'
			]

			authority.className = 'test.Role'
			cacheUsers = true
			rememberMe.persistent = System.getProperty('testconfig') == 'extended'
			rememberMe.persistentToken.domainClassName = 'test.PersistentToken'
			requestMap.className = 'test.Requestmap'
			securityConfigType = 'Requestmap'
			userLookup {
				authorityJoinClassName = 'test.UserRole'
				userDomainClassName = 'test.User'
			}
			useSwitchUserFilter = true
		}
	}
}
