<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.mappings'/></title>
	</head>
	<body>
		<br/>
		<h4><g:message code='spring.security.ui.info.mappings.type' args='[securityConfigType]'/></h4>
		<br/>
		<table>
			<thead>
			<tr>
				<th><g:message code='spring.security.ui.info.mappings.header.pattern'/></th>
				<th><g:message code='spring.security.ui.info.mappings.header.attribute'/></th>
				<th><g:message code='spring.security.ui.info.mappings.header.method'/></th>
			</tr>
			</thead>
			<tbody>
			<g:each var='iu' in='${configAttributes}'>
			<g:set var='closure' value='${iu.configAttributes.any { it.getClass().name.contains("ClosureConfigAttribute") }}'/>
				<tr>
					<td>${iu.pattern}</td>
					<td>${closure ? '&lt;closure&gt;' : iu.configAttributes.toString()[1..-2]}</td>
					<td>${iu.httpMethod ?: 'N/A'}</td>
				</tr>
			</g:each>
			</tbody>
		</table>
	</body>
</html>
