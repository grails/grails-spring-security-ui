<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.filterChains'/></title>
	</head>
	<body>
		<br/>
		<table>
			<thead>
			<tr>
				<th><g:message code='spring.security.ui.info.filterChains.header.pattern'/></th>
				<th><g:message code='spring.security.ui.info.filterChains.header.filters'/></th>
			</tr>
			</thead>
			<tbody>
			<g:each var='entry' in='${filterChainMap}'>
				<tr>
					<td>${entry.key}</td>
					<td>
						<g:if test='${entry.value}'>
						<ul>
						<g:each var='filter' in='${entry.value}'>
							<li>${filter.getClass().name}</li>
						</g:each>
						</ul>
						</g:if>
						<g:else>
						<i><g:message code='spring.security.ui.info.filterChains.none'/></i>
						</g:else>
					</td>
				</tr>
			</g:each>
			</tbody>
		</table>
	</body>
</html>
