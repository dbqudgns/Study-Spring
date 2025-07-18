package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    // JPAQueryFactory를 필드로
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    // JPQL 조회
    @Test
    public void startJPQL() {
        // member1을 찾아라
        String qlString =
                "select m from Member m " +
                "where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // QueryDSL 조회 및 Q클래스 인스턴스 활용
    @Test
    public void startQuerydsl() {
        // member1을 찾아라
        //JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        /** Q클래스 인스턴스를 사용하는 2가지 방법
         * QMember qMember = new QMember("m"); :: 별칭 직접 지정
         * QMember qMember = QMember.member; :: 기본 인스턴스 사용
         * 같은 테이블을 조인하는 경우가 아니라면 기본 인스턴스를 사용하자
         */

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) // 파라미터 바인딩 처리
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // 기본 검색 쿼리
    @Test
    public void search() {

        Member findMember = queryFactory
                .selectFrom(member) // select, from의 대상이 같은 경우 합칠 수 있다.
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // AND 조건을 파라미터로 처리
    @Test
    public void searchAndParm() {
        List<Member> result1 = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"), // where에 파라미터로 검색조건을 추가하면 AND 조건이 추가된다.
                        member.age.eq(10))
                .fetch();

        assertThat(result1.size()).isEqualTo(1);
    }

    // 결과 조회
    @Test
    public void resultFetch() {

        // fetch() : 리스트 조회, 데이터 없으면 빈 리스트를 반환
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        /**
         * fetchOne() : 단건 조회
         * 결과가 없으면 null
         * 결과가 둘 이상이면 com.querydsl.core.NonUniqueResultException
         */
        Member findMember1 = queryFactory
                .selectFrom(member)
                .fetchOne();

        // fetchFirst() : limit(1).fetchOne()으로 처음 한 건 조회
        Member findMember2 = queryFactory
                .selectFrom(member)
                .fetchFirst();

        // fetchResults() : 페이징 정보 포함, total count 쿼리 추가 실행
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        // fetchCount() : count 쿼리로 변경해서 count 수 조회
        long count = queryFactory
                .selectFrom(member)
                .fetchCount();

    }


}
