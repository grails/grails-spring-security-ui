<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code="spring.security.ui.appinfo.title.providers" default="Authentication Providers"/></title>
</head>

<body>

<h3><g:message code="spring.security.ui.appinfo.title.providers" default="Authentication Providers"/></h3>

<br/>

<table>
	<thead>
	<tr><th><g:message code="spring.security.ui.appinfo.label.providers" default="Authentication Providers"/></th></tr>
	</thead>
	<tbody>
	<g:each var='provider' in='${providers}'>
	<tr><td>${provider.getClass().name}</td></tr>
	</g:each>
	</tbody>
</table>
</body>

</html>
