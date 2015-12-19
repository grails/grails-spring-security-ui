<html>

<head>
	<meta name="layout" content="${layoutUi}"/>
	<title>Secure Channel Definition</title>
</head>

<body>

<br/>

<g:if test='${requestMap}'>
<table>
<br/>
	<thead>
		<tr>
			<th>Pattern</th>
			<th>ConfigAttributes</th>
		</tr>
	</thead>
	<tbody>
	<g:each var='entry' in='${requestMap}'>
		<tr>
			<td>${entry.key}</td>
			<td>${entry.value}</td>
		</tr>
	</g:each>
	</tbody>
</table>
</g:if>
<g:else>
<h4>Not using Channel Security</h4>
</g:else>

</body>

</html>
