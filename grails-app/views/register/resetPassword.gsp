<html>
	<head>
		<meta name="layout" content="${layoutRegister}"/>
		<s2ui:title messageCode='spring.security.ui.resetPassword.title'/>
	</head>
	<body>
		<p/>
		<s2ui:formContainer type='resetPassword' focus='password' width='475px'>
			<s2ui:form beanName='resetPasswordCommand'>
				<g:hiddenField name='t' value='${token}'/>
				<div class="sign_in">
				<br/>
				<h4><g:message code='spring.security.ui.resetPassword.description'/></h4>
				<table>
					<s2ui:passwordFieldRow name='password' labelCode='resetPasswordCommand.password.label' labelCodeDefault='Password'/>
					<s2ui:passwordFieldRow name='password2' labelCode='resetPasswordCommand.password2.label' labelCodeDefault='Password (again)'/>
				</table>
				<s2ui:submitButton elementId='submit' messageCode='spring.security.ui.resetPassword.submit'/>
				</div>
			</s2ui:form>
		</s2ui:formContainer>
	</body>
</html>
