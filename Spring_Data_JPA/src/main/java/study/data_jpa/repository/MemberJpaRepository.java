package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.data_jpa.entity.Member;

@Repository
public class MemberJpaRepository {

    @PersistenceContext // Spring이 영속성 컨텍스트가 연결된 EntityManager를 자동 주입해주는 어노테이션
    private EntityManager em; // EntityManager : 영속성 컨텍스트를 직접 제어할 수 있는 객체

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
