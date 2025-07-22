package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

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

    /** 정렬
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    // 페이징 : 조회 건수 제한
    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    // 페이징 : 전체 조회 수가 필요할 시
    @Test
    public void paging2() {
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    // 집합 함수
    @Test
    public void aggregation() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.count(), // 회원 수
                        member.age.sum(), // 나이 합
                        member.age.avg(), // 평균 나이
                        member.age.max(), // 최대 나이
                        member.age.min()) // 최소 나이
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    /**
     * GroupBy 사용 : 팀의 이름과 각 팀의 평균 연령을 구해라
     */
    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    // 조인 : 기본 조인 => join(조인 대상, 별칭으로 사용할 Q타입)
    /**
     * 팀 A에 소속된 모든 회원
     */
    @Test
    public void join() throws Exception {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team) // innerJoin() 내부 조인
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username") // result 리스트 안의 각 객체에서 getUsername() 값을 꺼낸다.
                .containsExactly("member1", "member2"); // 꺼낸 값들이 "member1", "member2" 순서와 내용이 정확히 일치하는지 검증
    }

    /**
     * 세타 조인 (연관관계가 없는 필드로 조인)
     * 외부 조인이 불가능 -> 조인 on을 사용하면 외부 조인 가능
     * 회원의 이름이 팀 이름과 같은 회원 조회
     *
     */
    @Test
    public void theta_join() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team) // 실제 SQL에서는 Cross Join이 실행된다.
                .where(member.username.eq(team.name))
                .fetch();

        assertThat((result))
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /** 조인 - on : 조인 대상 필터링
     * ex. 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조회, 회원은 모두 조회
     */
    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }

        List<Tuple> result2 = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result2) {
            System.out.println("tuple = " + tuple);
        }
    }

    /** 조인 - on : 연관관계 없는 엔티티 외부 조인
     * ex. 회원의 이름과 팀의 이름이 같은 대상 외부 조인
     */
    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name)) // id 매핑식이 들어가지 않는다.
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("t=" + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf; // EntityManagerFactory는 EntityManager를 생성하는 팩토리(공장) 객체

    // 패치 조인 미적용
    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 특정 연관 객체가 초기화(로딩) 되었는지 확인
        assertThat(loaded).as("패치 조인 미적용").isFalse();
    }

    // 패치 조인 적용
    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 특정 연관 객체가 초기화(로딩) 되었는지 확인
        assertThat(loaded).as("패치 조인 적용").isTrue();
    }



}
