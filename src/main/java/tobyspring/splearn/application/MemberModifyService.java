package tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.provided.MemberFinder;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.*;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class MemberModifyService implements MemberRegister {
    private final MemberFinder finder;
    private final MemberRepository repository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest request) {
        checkDuplicateEmail(request);

        Member member = Member.register(request, passwordEncoder);

        repository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = finder.find(memberId);

        member.activate();

        return repository.save(member);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");
    }

    private void checkDuplicateEmail(MemberRegisterRequest request) {
        if (repository.findByEmail(new Email(request.email())).isPresent())
            throw new DuplicateEmailException("이미 사용중인 이메일입니다: " + request.email());
    }
}
