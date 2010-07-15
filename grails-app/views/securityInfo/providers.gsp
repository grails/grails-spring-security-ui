<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Mappings</title>
</head>

<body>

<table>
	<thead>
	<tr><th>Authentication Providers</th></tr>
	</thead>
	<tbody>
	<g:each var='provider' in='${providers}'>
	<tr><td>${provider.getClass().name}</td></tr>
	</g:each>
	</tbody>
</table>
</body>
