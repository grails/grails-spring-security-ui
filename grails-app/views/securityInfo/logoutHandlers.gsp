<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code="spring.security.ui.appinfo.title.logout" default="Logout Handlers"/></title>
</head>

<body>

<h3><g:message code="spring.security.ui.appinfo.title.logout" default="Logout Handlers"/></h3>

<br/>

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

</html>
