package test

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.transaction.Transactional

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder as SCH

@Transactional
class TestDataService {

	SpringSecurityService springSecurityService

	void reset() {
		deleteData()
		createData()
	}

	void createData() {
		createUsersAndRoles()
		createRequestmaps()
		createRegistrationCodes()
	}

	void deleteData() {
		Report.list()*.delete()
		UserRole.list()*.delete()
		User.list()*.delete()
		Role.list()*.delete()
		Requestmap.list()*.delete()
		PersistentToken.list()*.delete()
		RegistrationCode.list()*.delete()

		User.withSession { it.flush() }
	}

	protected void createRegistrationCodes() {
		registrationCodeData.each { String token, String username ->
			save new RegistrationCode(username: username, token: token)
		}
	}

	protected void createRequestmaps() {
		save new Requestmap('/secure/**', 'ROLE_ADMIN')
		save new Requestmap('/j_spring_security_switch_user', 'ROLE_RUN_AS,IS_AUTHENTICATED_FULLY')
		save new Requestmap('/**', 'permitAll')
		springSecurityService.clearCachedRequestmaps()
	}

	protected void createUsersAndRoles() {

		def grantRole = { user, String roleName ->
			UserRole.create user, Role.findByAuthority('ROLE_' + roleName.toUpperCase())
		}

		roleNames.each { save new Role("ROLE_${it.toUpperCase()}") }

		(1..3).each {
			def user = save new User("user$it", "password", "user$it@test.com")
			grantRole user, 'user'
		}

		def admin = save new User('admin', 'password', 'admin@test.com')
		['user', 'admin', 'run_as', 'switch_user'].each { grantRole admin, it }

		def flags = [:]
		def create = { String... names -> names.each { save new User([username: it, password: 'x'] + flags) } }

		create 'foon_2','foolkiller', 'foostra', 'sonnyboy', 'abzstrak', 'orchidtemple', 'drake', 'ajc_322'

		flags = [passwordExpired: true]
		create 'hhheeeaaatt', 'mscanio', 'kittal'

		flags = [accountLocked: true]
		create 'achen', 'szhang1999', 'aaaaaasd'

		flags = [accountExpired: true]
		create 'maryrose', 'rome20c', 'ratuig'

		flags = [enabled: false]
		create 'billy9494'
	}

	protected save(instance) {
		instance.save(failOnError: true)
	}

	private static final registrationCodeData = [
		'e81b1e53648a47e6aef31a937154c7cb': 'registration_test_1',
		'4a7f88afec3746f7aab2f5d0d8df6d8e': 'registration_test_1',
		'c7ac5f23be70495f93e4450a78a27cb4': 'registration_test_1',
		'a50e061e0e2f424fb7fbc2ff3dae597d': 'registration_test_1',
		'd6938ad63c414a69a0da30a8c0619a60': 'registration_test_2',
		'4a589c642ea143abb2ecaea57fa0a0cc': 'registration_test_2',
		'0a154624f36d42e4aa68991a9477bd04': 'registration_test_2',
		'3842a6ae102a431c8e48177c16720713': 'registration_test_3',
		'84cefa66465a460c82f46120d9098686': 'registration_test_3',
		'fd1e40a7b31f4e8282a2a789135ed21d': 'registration_test_3',
		'89f9bbc658b14808ae4c77c6e17e551a': 'registration_test_3',
		'f6779a5e8f2045288b810a3c5e317855': 'registration_test_3',
		'3a85f41311f0421ab0f72ff003dc2aeb': 'registration_test_4',
		'1d5720125c9441688246bda276b2ada8': 'registration_test_4'
	]

	private static final roleNames = [
		'user', 'admin', 'coffee', 'instead', 'melodrama', 'nevertheless', 'phalanger',
		'plagiarisation', 'run_as', 'speechwriter', 'switch_user', 'virtual']
}
