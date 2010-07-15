<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Mappings</title>
</head>

<body>

<table>
	<thead>
	<tr><th>Logout Handlers</th></tr>
	</thead>
	<tbody>
	<g:each var='handler' in='${handlers}'>
	<tr><td>${handler.getClass().name}</td></tr>
	</g:each>
	</tbody>
</table>
</body>
