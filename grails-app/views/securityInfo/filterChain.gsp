<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code="spring.security.ui.appinfo.title.filters" default="Filter Chains"/></title>
</head>

<body>

<h3><g:message code="spring.security.ui.appinfo.title.filters" default="Filter Chains"/></h3>

<br/>

<table>
	<thead>
	<tr>
		<th><g:message code='spring.security.ui.appinfo.label.URLPattern' default='URL Pattern'/></th>
		<th><g:message code='spring.security.ui.appinfo.label.filters' default='Filters'/></th>
	</tr>
	</thead>
	<tbody>
	<g:each var='entry' in='${filterChainMap}'>
	<tr>
		<td>${entry.key}</td>
		<td>
			<g:each var='filter' in='${entry.value}'>
			${filter.getClass().name}<br/>
			</g:each>
		</td>
	</tr>
	</g:each>
	</tbody>
</table>
</body>

</html>
