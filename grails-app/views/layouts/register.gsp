<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title><g:layoutTitle default='User Registration'/></title>

<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon"/>

<s2ui:resources module='register' />
<%--

The 'resources' tag in SecurityUiTagLib renders these tags if you're not using the resources plugin:

	<asset:javascript src="jquery/dist/jquery"/>
	<asset:stylesheet src="reset"/>
	<asset:stylesheet src="spring-security-ui"/>
	<asset:javascript src="jquery-ui/jquery-ui"/>			
	<asset:stylesheet src="jquery-ui/themes/smoothness/jquery-ui"/>
	<asset:stylesheet src="jquery.jgrowl.css"/>	
	<asset:stylesheet src="jquery.safari-checkbox.css"/>
	<asset:stylesheet src="auth"/>

or these if you are:

   <r:require module="register"/>
   <r:layoutResources/>

If you need to customize the resources, replace the <s2ui:resources> tag with
the explicit tags above and edit those, not the taglib code.
--%>

<g:layoutHead/>

</head>

<body>

<s2ui:layoutResources module='register' />
<g:layoutBody/>
<%--
<asset:javascript src="jquery.jgrowl.js"/>	
<asset:javascript src="jquery.checkbox.js"/>	
<asset:javascript src="spring-security-ui.js"/>
--%>

<s2ui:showFlash/>

</body>
</html>
