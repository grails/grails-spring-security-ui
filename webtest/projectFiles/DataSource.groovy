dataSource {
	pooled = true
	username = 'sa'
	password = ''
	driverClassName = 'org.h2.Driver'
	dialect = org.hibernate.dialect.H2Dialect
	url = 'jdbc:h2:./db/testDb'
}

hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
