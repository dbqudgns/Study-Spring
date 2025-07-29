package study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

// 2. 사용자 정의 인터페이스 구현
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberTeamDto> searchByWhereParameter(MemberSearchCondition condition) {
        // 동적 쿼리 : Where절에 파라미터를 사용한 예제 -> 다른 메서드에서도 조건 재사용이 가능하다 !!
        // 회원명, 팀명, 나이(ageGoe, ageLoe)
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name
                ))
                .from(member)
                .leftJoin(member.team, team)
                // 만약 where절에 들어갈 조건이 없으면 모든 Member를 조회하기 때문에 이런 동적 쿼리에서는 페이징을 적용해줘야 한다.
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .fetch();

    }

    /**
     * 단순한 페이징, fetchResults() 사용
     * @param condition
     * @param pageable
     * @return
     */
    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MemberTeamDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 복잡한 페이징, 데이터 조회 쿼리와, 전체 카운트 쿼리를 분리
     * @param condition
     * @param pageable
     * @return
     */
    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> content = queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

      //  long total = queryFactory.select(member). ~~
      //  return new PageImpl<>(content, pageable, total);

    /** 아래 Count 쿼리는 단순한 쿼리에서는 잘 동작하는데 복잡한 쿼리에서는 잘 동작하지 않아
    * QueryDSL 5.0은 fetchCount(), fetchResult()을 지원하지 않기로 했디.
    * 따라서 아래 Count 쿼리를 주석처리 하였다.
    */

        // JPAQuery<Member> countQuery = queryFactory
           //     .select(member)
           //     .from(member)
           //     .leftJoin(member.team, team)
           //     .where(usernameEq(condition.getUsername()),
           //             teamNameEq(condition.getTeamName()),
           //             ageGoe(condition.getAgeGoe()),
           //             ageLoe(condition.getAgeLoe()));

       /**
       *  return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
       *  countQuery::fetchCount == () -> countQuery.fetchCount()
       *  1. 페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때 count 쿼리 생략
       *  2. 마지막 페이지일 때 count 쿼리 생략
       */

        JPAQuery<Long> countQuery = queryFactory
                .select(member.count()) // count(member.id)로 처리된다.
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression usernameEq(String username) {
                return hasText(username) ? member.username.eq(username) : null;
            }

    private BooleanExpression teamNameEq(String teamName) {
                return hasText(teamName) ? team.name.eq(teamName) : null;
            }

    private BooleanExpression ageGoe(Integer ageGoe) {
                return ageGoe == null ? null : member.age.goe(ageGoe);
            }

    private BooleanExpression ageLoe(Integer ageLoe) {
                return ageLoe == null ? null : member.age.loe(ageLoe);
            }

}

