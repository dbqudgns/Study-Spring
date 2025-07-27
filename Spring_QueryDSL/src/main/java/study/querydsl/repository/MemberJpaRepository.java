package study.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

// 순수 JPA 리포지토리 - QueryDSL 추가
@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<Member> findAll_QueryDSL() {
        return queryFactory
                .selectFrom(member).fetch();
    }

    public List<Member> findByUsername_QueryDSL(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }

    // 동적쿼리 : Builder 사용
    // 회원명, 팀명, 나이(ageGoe, ageLoe)
    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {

        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUsername())) { // null 또는 " "이 들어올 수 있으니 StringUtils.hasText을 사용하여 텍스트가 있는지 검증
            builder.and(member.username.eq(condition.getUsername()));
        }
        if (hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }
        if (condition.getAgeLoe() != null) {
            builder.and(member.age.loe(condition.getAgeLoe()));
        }

        // 만약 where절에 들어갈 조건이 없으면 모든 Member를 조회하기 때문에 이런 동적 쿼리에서는 페이징을 적용해줘야 한다.
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id,
                        member.username,
                        member.age,
                        team.id,
                        team.name))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }
}
