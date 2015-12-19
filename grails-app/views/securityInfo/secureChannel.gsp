<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.secureChannel'/></title>
	</head>
	<body>
		<br/>
		<g:if test='${requestMap}'>
		<br/>
		<table>
			<thead>
				<tr>
					<th><g:message code='spring.security.ui.info.channel.header.pattern'/></th>
					<th><g:message code='spring.security.ui.info.channel.header.attributes'/></th>
				</tr>
			</thead>
			<tbody>
			<g:each var='entry' in='${requestMap}'>
				<tr>
					<td>${entry.key}</td>
					<td>${entry.value}</td>
				</tr>
			</g:each>
			</tbody>
		</table>
		</g:if>
		<g:else>
		<h4><g:message code='spring.security.ui.info.channel.disabled'/></h4>
		</g:else>
	</body>
</html>
