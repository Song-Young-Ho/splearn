package tobyspring.splearn.application.member.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static tobyspring.splearn.domain.member.Member.register;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.domain.member.MemberFixture.createPasswordEncoder;

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
        entityManager.clear();

        var found = repository.findById(member.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void duplicateEmailFail() {
        Member member = register(createMemberRegisterRequest(), createPasswordEncoder());
        repository.save(member);

        Member member2 = register(createMemberRegisterRequest(), createPasswordEncoder());
        assertThatThrownBy(() -> repository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}