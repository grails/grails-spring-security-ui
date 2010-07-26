<head>
	<meta name='layout' content='springSecurityUI'/>
	<title>Security Configuration</title>

<g:javascript>
	$(document).ready( function () {
		$('#config').dataTable();
	} );
</g:javascript>

</head>

<body>

<div id="configHolder">
<table id="config" cellpadding="0" cellspacing="0" border="0" class="display">
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
</div>

</body>

