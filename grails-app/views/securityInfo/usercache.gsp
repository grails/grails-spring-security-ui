<html>

<head>
	<meta name='layout' content='springSecurityUI'/>
	<title><g:message code='spring.security.ui.menu.appinfo.title.userCache' default='User Cache'/></title>
</head>

<body>

<br/>

<g:if test='${cache}'>
<h4><g:message code='spring.security.ui.menu.appinfo.title.userCache' default='User Cache'/>: ${cache.getClass().name}</h4>

<br/>

<table>
	<tr>
		<td><g:message code="spring.security.ui.appinfo.label.size" default="Size"/></td>
		<td>${cache.size}</td>
	</tr>
	<tr>
		<td><g:message code="spring.security.ui.appinfo.label.status" default="Status"/></td>
		<td>${cache.status}</td>
	</tr>
	<tr>
		<td><g:message code="spring.security.ui.appinfo.label.name" default="Name"/></td>
		<td>${cache.name}</td>
	</tr>
	<tr>
		<td><g:message code="spring.security.ui.appinfo.label.GUID" default="GUID"/></td>
		<td>${cache.guid}</td>
	</tr>
	<tr>
		<td><g:message code="spring.security.ui.appinfo.label.statistics" default="Statistics"/></td>
		<td>
			<table>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.cacheHits" default="Cache Hits"/></td>
					<td>${cache.statistics.cacheHits}</td>
				</tr>
				<tr>n
					<td><g:message code="spring.security.ui.appinfo.label.inMemoryHits" default="In-memory Hits"/></td>
					<td>${cache.statistics.inMemoryHits}</td>
				</tr>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.onDiskHits" default="On-disk Hits"/></td>
					<td>${cache.statistics.onDiskHits}</td>
				</tr>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.cacheMisses" default="Cache Misses"/></td>
					<td>${cache.statistics.cacheMisses}</td>
				</tr>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.objectCount" default="Object Count"/></td>
					<td>${cache.statistics.objectCount}</td>
				</tr>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.memoryStoreObjectCount" default="Memory Store Object Count"/></td>
					<td>${cache.statistics.memoryStoreObjectCount}</td>
				</tr>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.diskStoreObjectCount" default="Disk Store Object Count"/></td>
					<td>${cache.statistics.diskStoreObjectCount}</td>
				</tr>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.statisticsAccuracyDescription" default="Statistics Accuracy Description"/></td>
					<td>${cache.statistics.statisticsAccuracyDescription}</td>
				</tr>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.averageGetTime" default="Average Get Time"/></td>
					<td>${cache.statistics.averageGetTime}</td>
				</tr>
				<tr>
					<td><g:message code="spring.security.ui.appinfo.label.evictionCount" default="Eviction Count"/></td>
					<td>${cache.statistics.evictionCount}</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr><th colspan='2'>${cache.size} user${cache.size == 1 ? '' : 's'}</th></tr>
	<tr>
		<th><g:message code="spring.security.ui.appinfo.label.username" default="Username"/></th>
		<th><g:message code="spring.security.ui.appinfo.label.user" default="User"/></th>
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
<h4><g:message code="spring.security.ui.appinfo.label.notCachingUsers" default="Not Caching Users"/></h4>
</g:else>

</body>

</html>
