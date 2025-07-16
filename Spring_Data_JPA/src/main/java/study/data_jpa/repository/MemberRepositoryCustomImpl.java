package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.data_jpa.entity.Member;

import java.util.List;

@RequiredArgsConstructor
// 사용자 정의 구현 클래스 규칙 : 리포지토리 인터페이스 이름(MemberRepository) + Impl
// 스프링 2.x 부터는 사용자 정의 인터페이스(MemberRepositoryCustom) + Impl 방식도 지원한다.
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
