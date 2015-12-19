<html>
	<head>
		<meta name="layout" content="${layoutRegister}"/>
		<s2ui:title messageCode='spring.security.ui.forgotPassword.title'/>
	</head>
	<body>
		<p/>
		<s2ui:formContainer type='forgotPassword' focus='username' width='400px'>
			<s2ui:form>
				<g:if test='${emailSent}'>
				<br/>
				<g:message code='spring.security.ui.forgotPassword.sent'/>
				</g:if>
				<g:else>
				<br/>
				<h4><g:message code='spring.security.ui.forgotPassword.description'/></h4>
				<table>
					<tr>
						<td><label for="username"><g:message code='spring.security.ui.forgotPassword.username'/></label></td>
						<td><g:textField name='username' size='25'/></td>
					</tr>
				</table>
				<s2ui:submitButton elementId='submit' messageCode='spring.security.ui.forgotPassword.submit'/>
				</g:else>
			</s2ui:form>
		</s2ui:formContainer>
	</body>
</html>
