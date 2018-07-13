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
        //new Profile(...).save(flush: true, failOnError: true)
        //new Profile(...).save(flush: true, failOnError: true)
        //Profile profile = new Profile(...).save(flush: true, failOnError: true)
        //new Profile(...).save(flush: true, failOnError: true)
        //new Profile(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
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
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        profileService.count() == 5
    }

    void "test delete"() {
        Long profileId = setupData()

        expect:
        profileService.count() == 5

        when:
        profileService.delete(profileId)
        sessionFactory.currentSession.flush()

        then:
        profileService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Profile profile = new Profile()
        profileService.save(profile)

        then:
        profile.id != null
    }
}
