<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.providers'/></title>
	</head>
	<body>
		<br/>
		<table>
			<thead>
				<tr><th><g:message code='spring.security.ui.menu.securityInfo.providers'/></th></tr>
			</thead>
			<tbody>
			<g:each var='provider' in='${providers}'>
				<tr><td>${provider.getClass().name}</td></tr>
			</g:each>
			</tbody>
		</table>
	</body>
</html>
