<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Mappings</title>
</head>

<body>

<table>
	<caption>SecurityConfigType: ${securityConfigType}</caption>
	<thead>
	<tr>
		<th>Name</th>
		<th>Value</th>
	</tr>
	</thead>
	<tbody>
	<g:each var='entry' in='${configAttributeMap}'>
	<tr>
		<td>${entry.key}</td>
		<td>${entry.value}</td>
	</tr>
	</g:each>
	</tbody>
</table>
</body>
