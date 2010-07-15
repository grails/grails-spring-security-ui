package com.testapp

class User {

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	String email

	static constraints = {
		username blank: false, unique: true
		password blank: false
		email blank: false, email: true, unique: true
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}
}
