package tobyspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(MemberFinder finder, MemberRegister register, EntityManager entityManager) {
    @Test
    void find() {
        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        entityManager.flush();
        entityManager.clear();

        Member found = finder.find(member.getId());

        assertThat(found.getId()).isEqualTo(member.getId());
    }

    @Test
    void findFail() {
        assertThatThrownBy(() -> finder.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}