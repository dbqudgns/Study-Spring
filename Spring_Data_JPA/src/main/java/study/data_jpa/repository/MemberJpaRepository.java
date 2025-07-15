package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.data_jpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext // Spring이 영속성 컨텍스트가 연결된 EntityManager를 자동 주입해주는 어노테이션
    private EntityManager em; // EntityManager : 영속성 컨텍스트를 직접 제어할 수 있는 객체

    // 저장
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    // 삭제
    public void delete(Member member) {
        em.remove(member);
    }

    // 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 단건 조회 : Optional 반환
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    // 카운트
    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    // 이름과 나이를 기준으로 회원 조회
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > : age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    // NamedQuery를 통한 조회
    public List<Member> findByUsername(String username) {

        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    // JPA 페이징 조회
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset) // 어느 인덱스 번호부터 가져올꺼야 ? (초기 인덱스는 0부터 시작)
                .setMaxResults(limit) // 몇 개를 가져올꺼야 ?
                .getResultList();
    }

    // 가져올 데이터의 개수 반환
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    // JPA를 사용한 벌크성 수정 쿼리
    public int bulkAgePlus(int age) {
        int resultCount = em.createQuery("update Member m set m.age = m.age + 1" + "where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();

        return resultCount;
    }
}
