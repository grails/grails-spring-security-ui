import com.dumbster.smtp.SimpleSmtpServer

import java.net.ServerSocket

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.commons.ApplicationHolder

class RegisterTest extends AbstractSecurityWebTest {

	private server

	def mailSender

	protected void setUp() {
		super.setUp()
		startMailServer()
	}

	protected void tearDown() {
		super.tearDown()
		server.stop()
	}

	private void startMailServer() {

		if (!mailSender) {
			mailSender = ApplicationHolder.application.mainContext.mailSender
		}

		int port = 1025
		while (true) {
			try {
				new ServerSocket(port).close()
				break
			}
			catch (IOException e) {
				port++
				if (port > 2000) {
					fail 'cannot find open port'
				}
			}
		}
		server = SimpleSmtpServer.start(port)
		mailSender.port = port
	}

	void testRegisterValidation() {
		get '/register'
		assertContentContains 'Create Account'

		form('registerForm') {
			click 'create_submit'
		}

		assertContentContains 'Username is required'
		assertContentContains 'Email is required'
		assertContentContains 'Password is required'

		form('registerForm') {
			username = 'admin'
			email = 'foo'
			password = 'abc'
			password2 = 'def'
			click 'create_submit'
		}

		assertContentContains 'The username is taken'
		assertContentContains 'Please provide a valid email address'
		assertContentContains 'Password must have at least one letter, number, and special character: !@#$%^&'
		assertContentContains 'Passwords do not match'

		form('registerForm') {
			username = 'abcdef123'
			email = 'abcdef@abcdef.com'
			password = 'aaaaaaaa'
			password2 = 'aaaaaaaa'
			click 'create_submit'
		}

		assertContentContains 'Password must have at least one letter, number, and special character: !@#$%^&'
	}

	void testForgotPasswordValidation() {
		get '/register/forgotPassword'
		assertContentContains 'Forgot Password'

		form('forgotPasswordForm') {
			click 'reset_submit'
		}
		assertContentContains 'Please enter your username'

		form('forgotPasswordForm') {
			username = '1111'
			click 'reset_submit'
		}
		assertContentContains 'No user was found with that username'
	}

	void testRegisterAndForgotPassword() {
		String un = 'test_user_abcdef' + System.currentTimeMillis()
		testRegister un

		server.stop()
		startMailServer()

		testForgotPassword un
	}

	private void testRegister(String un) {
		get '/register'
		assertContentContains 'Create Account'

		form('registerForm') {
			username = un
			email = un + '@abcdef.com'
			password = 'aaaaaa1#'
			password2 = 'aaaaaa1#'
			click 'create_submit'
		}

		assertContentContains 'Your account registration email was sent - check your mail!'

		assertEquals 1, server.receivedEmailSize

		def email = server.receivedEmail.next()
		assertEquals 'New Account', email.getHeaderValue('Subject')

		String body = email.body
		assertTrue body.contains('Hi ' + un)

		int index = body.indexOf('/register/verifyRegistration?t=')
		assertTrue index > -1
		int index2 = body.indexOf('"', index + 1)
		String code = body.substring(index + '/register/verifyRegistration?t='.length(), index2)

		get '/register/verifyRegistration?t=' + code
		assertContentContains 'Your registration is complete'
		assertContentContains 'Logged in as ' + un
	}

	private void testForgotPassword(String un) {
		get '/register/forgotPassword'
		assertContentContains 'Forgot Password'

		form('forgotPasswordForm') {
			username = un
			click 'reset_submit'
		}
		assertContentContains 'Your password reset email was sent - check your mail!'

		assertEquals 1, server.receivedEmailSize
		def email = server.receivedEmail.next()

		assertEquals 'Password Reset', email.getHeaderValue('Subject')

		String body = email.body
		assertTrue body.contains('Hi ' + un)

		int index = body.indexOf('/register/resetPassword?t=')
		assertTrue index > -1
		int index2 = body.indexOf('"', index + 1)
		String code = body.substring(index + '/register/resetPassword?t='.length(), index2)

		get '/register/resetPassword?t=123'
		assertContentContains 'Sorry, we have no record of that request, or it has expired'

		get '/register/resetPassword?t=' + code
		assertContentContains 'Reset Password'

		form('resetPasswordForm') {
			click 'reset_submit'
		}
		assertContentContains 'Password is required'

		form('resetPasswordForm') {
			password = 'abc'
			password2 = 'def'
			click 'reset_submit'
		}
		assertContentContains 'Password must have at least one letter, number, and special character: !@#$%^&'
		assertContentContains 'Passwords do not match'

		form('resetPasswordForm') {
			password = 'aaaaaaaa'
			password2 = 'aaaaaaaa'
			click 'reset_submit'
		}
		assertContentContains 'Password must have at least one letter, number, and special character: !@#$%^&'

		form('resetPasswordForm') {
			password = 'aaaaaa1#'
			password2 = 'aaaaaa1#'
			click 'reset_submit'
		}

		assertContentContains 'Your password was successfully changed'
		assertContentContains 'Logged in as ' + un
	}
}
