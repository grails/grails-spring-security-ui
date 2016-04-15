<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.usercache'/></title>
	</head>
	<body>
		<g:if test='${cache}'>
		<p/>
		<g:message code='spring.security.ui.info.usercache.classname' args='[cache.getClass().name]'/>
		<s2ui:securityInfoTable type='usercache' headerCodes='attribute,value'>
			<tr class='even'>
				<td><g:message code='spring.security.ui.info.usercache.label.size'/></td>
				<td>${cache.size}</td>
			</tr>
			<tr class='odd'>
				<td><g:message code='spring.security.ui.info.usercache.label.status'/></td>
				<td>${cache.status}</td>
			</tr>
			<tr class='even'>
				<td><g:message code='spring.security.ui.info.usercache.label.name'/></td>
				<td>${cache.name}</td>
			</tr>
			<tr class='odd'>
				<td><g:message code='spring.security.ui.info.usercache.label.guid'/></td>
				<td>${cache.guid - 'Precision.M4800/'}</td>
			</tr>
			<tr class='even'>
				<td colspan='2'>
					<s2ui:securityInfoTable type='usercache.statistics' headerCodes='attribute,value'>
						<tr>
							<td><g:message code='spring.security.ui.info.usercache.label.stats.cacheHits'/></td>
							<td>${cache.statistics.cacheHitCount()}</td>
						</tr>
						<tr>
							<td><g:message code='spring.security.ui.info.usercache.label.stats.memoryHits'/></td>
							<td>${cache.statistics.localHeapHitCount()}</td>
						</tr>
						<tr>
							<td><g:message code='spring.security.ui.info.usercache.label.stats.diskHits'/></td>
							<td>${cache.statistics.localDiskHitCount()}</td>
						</tr>
						<tr>
							<td><g:message code='spring.security.ui.info.usercache.label.stats.cacheMisses'/></td>
							<td>${cache.statistics.cacheMissCount()}</td>
						</tr>
						<tr>
							<td><g:message code='spring.security.ui.info.usercache.label.stats.objectCount'/></td>
							<td>${cache.statistics.size}</td>
						</tr>
						<tr>
							<td><g:message code='spring.security.ui.info.usercache.label.stats.memoryObjectCount'/></td>
							<td>${cache.statistics.localHeapSize}</td>
						</tr>
						<tr>
							<td><g:message code='spring.security.ui.info.usercache.label.stats.diskObjectCount'/></td>
							<td>${cache.statistics.localDiskSize}</td>
						</tr>
						<tr>
							<td><g:message code='spring.security.ui.info.usercache.label.stats.evictionCount'/></td>
							<td>${cache.statistics.cacheEvictedCount()}</td>
						</tr>
					</s2ui:securityInfoTable>
				</td>
			</tr>
			<tr>
				<td colspan='2'>
					<s2ui:securityInfoTable type='usercache.cachedUsers' items='${cache.keys}' headerCodes='username,user' captionArgs='[cache.size]'>
						<td>${it}</td>
						<td>${cache.get(it)?.value}</td>
					</s2ui:securityInfoTable>
				</td>
			</tr>
		</s2ui:securityInfoTable>
		</g:if>
		<g:else>
		<h3><g:message code='spring.security.ui.info.usercache.disabled'/></h3>
		</g:else>
	</body>
</html>
