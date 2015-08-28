<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code="spring.security.ui.appinfo.title.auth" default="Current Authentication"/></title>
</head>

<body>

<h3><g:message code="spring.security.ui.appinfo.title.auth" default="Current Authentication"/></h3>

<br/>

<table>
	<thead>
	<tr>
		<th><g:message code='spring.security.ui.appinfo.label.name' default='Name'/></th>
		<th><g:message code='spring.security.ui.appinfo.label.value' default='Value'/></th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td><g:message code='spring.security.ui.appinfo.label.authorities' default='Authorities'/></td>
		<td>${auth.authorities}</td>
	</tr>
	<tr>
		<td><g:message code='spring.security.ui.appinfo.label.details' default='Details'/></td>
		<td>${auth.details}</td>
	</tr>
	<tr>
		<td><g:message code='spring.security.ui.appinfo.label.principal' default='Principal'/></td>
		<td>${auth.principal}</td>
	</tr>
	<tr>
		<td><g:message code='spring.security.ui.appinfo.label.name' default='Name'/></td>
		<td>${auth.name}</td>
	</tr>
	</tbody>
</table>
</body>

</html>
