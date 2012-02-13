class DefaultSecurityInfoTest extends AbstractSecurityWebTest {

	void testConfig() {
		get '/securityInfo/config'
		assertContentContains 'authenticationDetails.authClass'
		assertContentContains 'org.springframework.security.web.authentication.WebAuthenticationDetails'
	}

	void testMappings() {
		get '/securityInfo/mappings'

		assertContentContains '/j_spring_security_switch_user'
		assertContentContains '[IS_AUTHENTICATED_FULLY, ROLE_RUN_AS]'

		assertContentContains '/secure/**'
		assertContentContains '[ROLE_ADMIN]'
	}

	void testCurrentAuth() {
		get '/securityInfo/currentAuth'
		assertContentContains 'org.springframework.security.web.authentication.WebAuthenticationDetails'
		assertContentContains 'anonymousUser'
	}

	void testUsercache() {
		get '/securityInfo/usercache'
		assertContentContains 'Not Caching Users'
	}

	void testFilterChain() {
		get '/securityInfo/filterChain'
		assertContentContains '/**'
		assertContentContains 'org.springframework.security.web.context.SecurityContextPersistenceFilter'
		assertContentContains 'org.codehaus.groovy.grails.plugins.springsecurity.MutableLogoutFilter'
		assertContentContains 'org.codehaus.groovy.grails.plugins.springsecurity.RequestHolderAuthenticationFilter'
		assertContentContains 'org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter'
		assertContentContains 'org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter'
		assertContentContains 'org.springframework.security.web.authentication.AnonymousAuthenticationFilter'
		assertContentContains 'org.springframework.security.web.access.ExceptionTranslationFilter'
		assertContentContains 'org.springframework.security.web.access.intercept.FilterSecurityInterceptor'
	}

	void testLogoutHandler() {
		get '/securityInfo/logoutHandler'
		assertContentContains 'org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices'
		assertContentContains 'org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler'
	}

	void testVoters() {
		get '/securityInfo/voters'
		assertContentContains 'org.springframework.security.access.vote.AuthenticatedVoter'
		assertContentContains 'org.springframework.security.access.vote.RoleHierarchyVoter'
		assertContentContains 'org.codehaus.groovy.grails.plugins.springsecurity.WebExpressionVoter'
	}

	void testProviders() {
		get '/securityInfo/providers'
		assertContentContains 'org.springframework.security.authentication.dao.DaoAuthenticationProvider'
		assertContentContains 'org.springframework.security.authentication.AnonymousAuthenticationProvider'
		assertContentContains 'org.springframework.security.authentication.RememberMeAuthenticationProvider'
	}
}
