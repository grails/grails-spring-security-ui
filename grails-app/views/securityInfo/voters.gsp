<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.voters'/></title>
	</head>
	<body>
		<br/>
		<table>
			<thead>
				<tr><th><g:message code='spring.security.ui.menu.securityInfo.voters'/></th></tr>
			</thead>
			<tbody>
			<g:each var='voter' in='${voters}'>
				<tr><td>${voter.getClass().name}</td></tr>
			</g:each>
			</tbody>
		</table>
	</body>
</html>
