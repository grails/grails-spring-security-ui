<html>
	<head>
		<meta name="layout" content="${layoutUi}"/>
		<title><g:message code='spring.security.ui.menu.securityInfo.usercache'/></title>
	</head>
	<body>
		<br/>
		<g:if test='${cache}'>
		<h4><g:message code='spring.security.ui.info.usercache.classname' args='[cache.getClass().name]'/></h4>
		<br/>
		<table>
			<tr>
				<td><g:message code='spring.security.ui.info.usercache.label.size'/></td>
				<td>${cache.size}</td>
			</tr>
			<tr>
				<td><g:message code='spring.security.ui.info.usercache.label.status'/></td>
				<td>${cache.status}</td>
			</tr>
			<tr>
				<td><g:message code='spring.security.ui.info.usercache.label.name'/></td>
				<td>${cache.name}</td>
			</tr>
			<tr>
				<td><g:message code='spring.security.ui.info.usercache.label.guid'/></td>
				<td>${cache.guid}</td>
			</tr>
			<tr>
				<td><g:message code='spring.security.ui.info.usercache.label.stats'/></td>
				<td>
					<table>
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
					</table>
				</td>
			</tr>
			<tr><th colspan='2'><g:message code='spring.security.ui.info.usercache.header.userCount' args='[cache.size]'/></th></tr>
			<tr>
				<th><g:message code='spring.security.ui.info.usercache.header.username'/></th>
				<th><g:message code='spring.security.ui.info.usercache.header.user'/></th>
			</tr>
			<g:each var='k' in='${cache.keys}'>
			<tr>
				<td>${k}</td>
				<td>${cache.get(k)?.value}</td>
			</tr>
			</g:each>
		</table>
		</g:if>
		<g:else>
		<h4><g:message code='spring.security.ui.info.usercache.disabled'/></h4>
		</g:else>
	</body>
</html>
