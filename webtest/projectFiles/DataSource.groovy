dataSource {
	pooled = true
	username = 'sa'
	password = ''
	driverClassName = 'org.h2.Driver'
	url = 'jdbc:h2:./db/testDb'
//	logSql = true
}

hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = false
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
	format_sql = true
	use_sql_comments = true
}
