package study.querydsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;

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

