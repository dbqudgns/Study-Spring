package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.member;

@SpringBootTest
@Transactional
public class QuerydslMiddleTest {

    @PersistenceContext
    EntityManager em;

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

    // 프로젝션 : select을 통해 무엇을 가져올건지 즉, select 대상 지정

    // 프로젝션 대상이 하나일 때 : 타입을 명확하게 지정 가능
    @Test
    public void simpleProjection() {

        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String r : result) {
            System.out.println(r);
        }
    }

    // 프로젝션 대상이 2개 이상일 때 : 튜플이나 DTO로 조회
    @Test
    public void tupleProjection() {

        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple r : result) {
            System.out.println(r);
        }
    }

    /// ===============================================================


    // QueryDSL 결과 DTO 반환 3가지

    // 1. 프로퍼티 접근 - Setter
    @Test
    public void findDtoBySetter() {

        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto r : result) {
            System.out.println(r.getUsername() + " " + r.getAge());
        }
    }

    @Test
    // 2. 필드 직접 접근
    public void findDtoByField() {

        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto r : result) {
            System.out.println(r.getUsername() + " " + r.getAge());
        }
    }

    // 2.1 : 필드 직접 접근 시 별칭이 다를 때
    @Test
    public void findDtoByUser() {

        QMember memberSub = new QMember("memberSub");

        List<UserDto> fetch = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"), // 필드에 별칭("name") 적용 단, UserDto의 프로퍼티명과 같아야 한다.
                        ExpressionUtils.as( // 서브 쿼리에 별칭("age") 적용 단, UserDto의 프로퍼티명과 같아야 한다.
                                JPAExpressions
                                        .select(memberSub.age.max())
                                        .from(memberSub), "age")
                        )
                ).from(member)
                .fetch();

        for (UserDto f : fetch) {
            System.out.println(f.getName() + " " + f.getAge());
        }
    }

    // 3. 생성자 사용 : username, age 생성자가 있어야 한다!
    @Test
    public void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();

        for (MemberDto r : result) {
            System.out.println(r.getUsername() + " " + r.getAge());
        }
    }

    // 3.1 생성자 사용
    @Test
    public void findDtoByQueryProjection() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

    }

    /// ===============================================================

    // 동적 쿼리

    // 1. BooleanBuilder 사용
    @Test
    public void dynamicQuery_BooleanBuilder() throws Exception {

        // 파라미터 값이 둘다 null일 경우 where 절은 없어짐
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameParam, Integer ageParam) {
        BooleanBuilder builder = new BooleanBuilder(); // 초기값도 설정 가능하다. 단, 설정 값이 null이면 안된다!
        // ex. BooleanBuilder builder = new BoolenBuilder(member.username.eq(usernameParam));

        if (usernameParam != null) {
            builder.and(member.username.eq(usernameParam)); // or도 가능
        }

        if (ageParam != null) {
            builder.and(member.age.eq(ageParam));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    // 2. Where 다중 파라미터 사용
    @Test
    public void dynamicQuery_WhereParam() throws Exception {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameParam, Integer ageParam) {
        return queryFactory
                .selectFrom(member)
                // .where(usernameEq(usernameParam), ageEq(ageParam))
                .where(allEq(usernameParam, ageParam))
                .fetch();
    }

    // BooleanExpression : 표현식(member.username.eq(usernameParam)의 결과로 반환해준다.
    private BooleanExpression usernameEq(String usernameParam) {
        return usernameParam != null ? member.username.eq(usernameParam) : null;
    }

    private BooleanExpression ageEq(Integer ageParam) {
        return ageParam != null ? member.age.eq(ageParam) : null;
    }

    private BooleanExpression allEq(String usernameParam, Integer ageParam) {
        return usernameEq(usernameParam).and(ageEq(ageParam));
    }

}
