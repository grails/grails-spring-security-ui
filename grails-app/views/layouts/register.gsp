<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title><g:layoutTitle default='User Registration'/></title>

<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'reset.css')}"/>
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'spring-security-ui.css')}"/>
<link rel="stylesheet" media="screen" href="${resource(dir:'css/smoothness',file:'jquery-ui-1.8.2.custom.css',plugin:'spring-security-ui')}"/>
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'jquery.jgrowl.css')}"/>
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'jquery.safari-checkbox.css')}"/>
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'auth.css')}"/>

<script src="${g.resource(dir: 'js/jquery', file: 'jquery-1.4.2.js', plugin: 'jquery')}" type="text/javascript"></script>

<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon"/>

<g:layoutHead/>

</head>

<body>

<script src="${g.resource(dir: 'jquery-ui/js', file: 'jquery-ui-1.8.2.custom.min.js', plugin: 'jquery-ui')}" type="text/javascript"></script>
<g:javascript src='jquery/jquery.jgrowl.js'/>
<g:javascript src='jquery/jquery.checkbox.js'/>
<g:javascript src='spring-security-ui.js'/>
<g:layoutBody/>

<s2ui:showFlash/>

</body>
</html>
