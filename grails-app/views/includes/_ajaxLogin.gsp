<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" %>
<style>
input.login {
	display: block;
	width: 400px;
}
</style>

<span id='s2ui_login_link_container'>

<nobr>

<span id="logoutLink" style="display:none">
<g:link elementId='_logout' controller='logout'/>
<a href="${request.contextPath}${SpringSecurityUtils.securityConfig.logout.afterLogoutUrl}" id="_afterLogout"></a>
</span>

<div id='loginLinkContainer'>
<sec:ifLoggedIn>
Logged in as <sec:username/> (<g:link controller='logout' elementId='logout'>Logout</g:link>)
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
	<a href='#' id='loginLink'>Login</a>
</sec:ifNotLoggedIn>

<sec:ifSwitched>
<a href='${request.contextPath}/j_spring_security_exit_user'>
	Resume as <sec:switchedUserOriginalUsername/>
</a>
</sec:ifSwitched>
</div>
</nobr>

</span>

<div class='s2ui_center'>

<div id="loginFormContainer" style='display:none' title="${message(code:'spring.security.ui.login.signin')}">
	<g:form controller='j_spring_security_check' name="loginForm" autocomplete='off'>

	<label for="username"><g:message code='spring.security.ui.login.username'/></label>
	<input class='login' name="j_username" id="username" size="20" />

	<label for="password"><g:message code='spring.security.ui.login.password'/></label>
	<input class='login' type="password" name="j_password" id="password" size="20" />

	<input type="checkbox" class="checkbox" name="_spring_security_remember_me" id="remember_me" checked="checked" />
	<label for='remember_me'><g:message code='spring.security.ui.login.rememberme'/></label> |

	<g:link controller='register' action='forgotPassword'><g:message code='spring.security.ui.login.forgotPassword'/></g:link>
	<g:link controller='register'><g:message code='spring.security.ui.login.register'/></g:link>

	<input type='submit' class='s2ui_hidden_button' />

	</g:form>
	<div id='loginMessage' style='color: red; margin-top: 10px;'></div>
</div>
</div>

<script>
var loginButtonCaption = "<g:message code='spring.security.ui.login.login'/>";
var loggingYouIn = "<g:message code='spring.security.ui.login.loggingYouIn'/>";
</script>
