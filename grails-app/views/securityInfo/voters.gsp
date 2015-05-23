<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code='spring.security.ui.menu.appinfo.title.voters' default='Voters'/></title>
</head>

<body>

<br/>

<table>
	<thead>
	<tr><th><g:message code='spring.security.ui.menu.appinfo.title.voters' default='Voters'/></th></tr>
	</thead>
	<tbody>
	<g:each var='voter' in='${voters}'>
	<tr><td>${voter.getClass().name}</td></tr>
	</g:each>
	</tbody>
</table>
</body>

</html>
