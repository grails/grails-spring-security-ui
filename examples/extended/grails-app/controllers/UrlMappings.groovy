class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?(.$format)?"{}

		"/"(view: '/index')

		"404"(view: '/error404')
		"500"(view: '/error')
	}
}
