<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.logoutHandlers'/></title>
	</head>
	<body>
		<table>
			<thead>
				<tr><th><g:message code='spring.security.ui.menu.securityInfo.logoutHandlers'/></th></tr>
			</thead>
			<tbody>
			<g:each var='handler' in='${handlers}'>
				<tr><td>${handler.getClass().name}</td></tr>
			</g:each>
			</tbody>
		</table>
	</body>
</html>
