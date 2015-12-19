<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.currentAuth'/></title>
	</head>
	<body>
		<table>
			<thead>
				<tr>
					<th><g:message code='spring.security.ui.info.auth.header.name'/></th>
					<th><g:message code='spring.security.ui.info.auth.header.value'/></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><g:message code='spring.security.ui.info.auth.label.authorities'/></td>
					<td>${auth.authorities}</td>
				</tr>
				<tr>
					<td><g:message code='spring.security.ui.info.auth.label.details'/></td>
					<td>${auth.details}</td>
				</tr>
				<tr>
					<td><g:message code='spring.security.ui.info.auth.label.principal'/></td>
					<td>${auth.principal}</td>
				</tr>
				<tr>
					<td><g:message code='spring.security.ui.info.auth.label.name'/></td>
					<td>${auth.name}</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>
