<head>
<meta name='layout' content='main'/>
<title>Create Account</title>
</head>

<body>

<div class='body'>

	<h4>No user was found with that OpenID but you can register now and associate your OpenID with that account.</h4>

	<br/>

	<g:hasErrors bean="${command}">
	<div class="errors">
		<g:renderErrors bean="${command}" as="list"/>
	</div>
	</g:hasErrors>


	<g:form action='register'>
		<input type='hidden' name='openId' value='${command?.openId}'/>

		<table>
		<tr>
			<td><label for='openid'>Open ID:</label></td>
			<td><span id='openid'>${command?.openId}</span></td>
		</tr>

		<tr>
			<td><label for='username'>Username:</label></td>
			<td><g:textField name='username' value='${command?.username}'/></td>
		</tr>

		<tr>
			<td><label for='password'>Password:</label></td>
			<td><g:passwordField name='password' value='${command?.password}'/></td>
		</tr>

		<tr>
			<td><label for='password2'>Password (again):</label></td>
			<td><g:passwordField name='password2' value='${command?.password2}'/></td>
		</tr>

		</table>

		<input type='submit' value='Register'/>

	</g:form>
</div>

</body>
