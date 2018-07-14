package test

import grails.test.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ProfileServiceSpec extends Specification {

    ProfileService profileService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        ///new Profile(...).save(flush: true, failOnError: true)
        //new Profile(...).save(flush: true, failOnError: true)
        //Profile profile = new Profile(...).save(flush: true, failOnError: true)
        //new Profile(...).save(flush: true, failOnError: true)
        //new Profile(...).save(flush: true, failOnError: true)

        //profile.id
    }

    void "test get"() {
        setupData()

        expect:
        profileService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Profile> profileList = profileService.list(max: 2, offset: 2)

        then:
        profileList.size() == 2

    }

    void "test count"() {
        setupData()

        expect:
        profileService.count() == 4
    }

    void "test delete"() {

        expect:
        profileService.count() == 4

        when:
        profileService.delete(Profile.first().id)
        sessionFactory.currentSession.flush()

        then:
        profileService.count() == 3
    }

    void "test save"() {
        when:

        Profile profile = new Profile(user:User.findByUsername("foon_2"),myAnswer1:'1234',myQuestion1: "Count to four",myAnswer2: '12345', myQuestion2: 'Count to Five')
        profileService.save(profile)

        then:
        profile.id != null
    }
}
