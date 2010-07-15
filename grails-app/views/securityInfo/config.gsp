<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Security Configuration</title>
</head>

<body>

<table>
	<thead>
	<tr>
		<th>Name</th>
		<th>Value</th>
	</tr>
	</thead>
	<tbody>
	<g:each var='entry' in='${conf}'>
	<tr>
		<td>${entry.key}</td>
		<td>${entry.value}</td>
	</tr>
	</g:each>
	</tbody>
</table>
</body>
