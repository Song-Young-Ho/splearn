package tobyspring.splearn.application.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tobyspring.splearn.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static tobyspring.splearn.domain.Member.register;
import static tobyspring.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.MemberFixture.createPasswordEncoder;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    void creteMember() {
        Member member = register(createMemberRegisterRequest(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        repository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();
    }
}