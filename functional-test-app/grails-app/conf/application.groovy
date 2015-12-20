grails.mail.default.from = 'do.not.reply@server.com'
grails.mail.host = 'localhost'
grails.mail.port = 1025

String testconfig = new File('testconfig').text.trim().toLowerCase()
System.setProperty 'testconfig', testconfig

grails {
	plugin {
		springsecurity {
			filterChain.chainMap = [
				[pattern: '/assets/**',      filters: 'none'],
				[pattern: '/**/favicon.ico', filters: 'none'],
				[pattern: '/**/css/**',      filters: 'none'],
				[pattern: '/**/images/**',   filters: 'none'],
				[pattern: '/**/js/**',       filters: 'none'],
				[pattern: '/**',             filters: 'JOINED_FILTERS']
			]
			secureChannel.definition = [
				[pattern: '/testsecure/**',   access: 'REQUIRES_SECURE_CHANNEL'],
				[pattern: '/testinsecure/**', access: 'REQUIRES_INSECURE_CHANNEL'],
				[pattern: '/testany/**',      access: 'ANY_CHANNEL']
			]

			authority.className = 'test.Role'
			cacheUsers = true
			rememberMe.persistent = testconfig == 'extended'
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
