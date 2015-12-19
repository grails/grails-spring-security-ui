dataSource {
	dbCreate = 'create'
	driverClassName = 'org.h2.Driver'
//	logSql = true
	password = ''
	pooled = true
	url = 'jdbc:h2:mem:devDb;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE;MVCC=TRUE'
	username = 'sa'
}

hibernate {
	cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory'
	cache.use_query_cache = false
	cache.use_second_level_cache = false
	flush.mode = 'manual'
	format_sql = true
	singleSession = true
	use_sql_comments = true
}
