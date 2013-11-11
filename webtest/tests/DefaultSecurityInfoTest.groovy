class DefaultSecurityInfoTest extends AbstractSecurityWebTest {

	void testConfig() {
		get '/securityInfo/config'
		assertContentContains 'atr.rememberMeClass'
		assertContentContains 'org.springframework.security.authentication.RememberMeAuthenticationToken'
	}

	void testMappings() {
		get '/securityInfo/mappings'

		assertContentContains '/j_spring_security_switch_user'
		String content = response.contentAsString
		assert content.contains('IS_AUTHENTICATED_FULLY, ROLE_RUN_AS') || content.contains('ROLE_RUN_AS, IS_AUTHENTICATED_FULLY')

		assertContentContains '/secure/**'
		assertContentContains 'ROLE_ADMIN'
	}

	void testCurrentAuth() {
		get '/securityInfo/currentAuth'

		assertContentContains 'org.springframework.security.web.authentication.WebAuthenticationDetails'
		assertContentContains '__grails.anonymous.user__'
	}

	void testUsercache() {
		get '/securityInfo/usercache'
		assertContentContains 'Not Caching Users'
	}

	void testFilterChain() {
		get '/securityInfo/filterChain'
		assertContentContains '/**'
		assertContentContains 'org.springframework.security.web.context.SecurityContextPersistenceFilter'
		assertContentContains 'grails.plugin.springsecurity.web.authentication.logout.MutableLogoutFilter'
		assertContentContains 'grails.plugin.springsecurity.web.authentication.RequestHolderAuthenticationFilter'
		assertContentContains 'org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter'
		assertContentContains 'grails.plugin.springsecurity.web.filter.GrailsRememberMeAuthenticationFilter'
		assertContentContains 'grails.plugin.springsecurity.web.filter.GrailsAnonymousAuthenticationFilter'
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
		assertContentContains 'grails.plugin.springsecurity.web.access.expression.WebExpressionVoter'
	}

	void testProviders() {
		get '/securityInfo/providers'
		assertContentContains 'org.springframework.security.authentication.dao.DaoAuthenticationProvider'
		assertContentContains 'grails.plugin.springsecurity.authentication.GrailsAnonymousAuthenticationProvider'
		assertContentContains 'org.springframework.security.authentication.RememberMeAuthenticationProvider'
	}
}
