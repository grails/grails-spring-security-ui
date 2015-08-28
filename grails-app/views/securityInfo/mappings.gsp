<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code="spring.security.ui.appinfo.title.mappings" default="Mappings"/></title>
</head>

<body>

<h3><g:message code="spring.security.ui.appinfo.title.mappings" default="Mappings"/></h3>

<br/>

<h4><g:message code="spring.security.ui.appinfo.label.SecurityConfigType" default="SecurityConfigType"/>: ${securityConfigType}</h4>

<br/>

<table>
	<thead>
	<tr>
		<th><g:message code="spring.security.ui.appinfo.label.pattern" default="Pattern"/></th>
		<th><g:message code="spring.security.ui.appinfo.label.configAttrs" default="Config Attributes"/></th>
		<th><g:message code="spring.security.ui.appinfo.label.HTTPMethod" default="HTTP Method"/></th>
	</tr>
	</thead>
	<tbody>
	<g:each var='iu' in='${configAttributes}'>
<%
boolean closure = iu.configAttributes.any { it.getClass().name.contains('ClosureConfigAttribute') }
%>
	<tr>
		<td>${iu.pattern}</td>
		<td>${closure ? '&lt;closure&gt;' : iu.configAttributes.toString()[1..-2]}</td>
		<td>${iu.httpMethod ?: 'N/A'}</td>
	</tr>
	</g:each>
	</tbody>
</table>
</body>

</html>
