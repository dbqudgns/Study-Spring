package study.data_jpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.dto.MemberProjection;
import study.data_jpa.entity.Member;

import java.util.List;

// 스프링 데이터 JPA가 MemberRepositoryCustom을 상속받은 MemberRepositoryImpl을 인식해서 스프링 빈으로 자동 등록해준다.
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, t.name) " + "from Member m join m.team t")
    List<MemberDto> findMEmberDto();

    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    Page<Member> findPageByAge(int age, Pageable pageable); // countQuery가 나간다.

    /**
     * @Query(value = "select m from Member m left join m.team t")
     * 하이버네이트 6에서는 의미없는 left join을 최적화 한다. => why? : select m from Member m과 같다.
     * 하이버네이트 6에서는 countQuery = "select count(m) from Member m"이 없어도 countQuery에 join 쿼리가 자동으로 없어진다.
     * 하이버네이트 6이전에는 위 countQuery를 써줘야 성능 최적화를 진행할 수 있었다. => 불필요한 Join 쿼리를 날리지 않음

     * @Query(value = "select m from Member m left join fetch m.team t")
     * 하이버네이트 6에서는 join fetch를 사용해도 countQuery는 join fetch 쿼리가 없다. => why? : Member를 기준으로 조회하기 때문에 성능 최적화가 자동으로 이루어짐
     * 반면, member와 team을 모두 조회하는 join fetch는 join fetch가 이루어진다.
     */
    @Query(value = "select m from Member m left join m.team t")
    Page<Member> findPageByAgeJoinQuery(int age, Pageable pageable); // countQuery가 나간다.

    Slice<Member> findSliceByAge(int age, Pageable pageable); // countQuery가 나가지 않고 limit + 1개의 데이터가 조회된다.

    // select문을 제외한 벌크 연산(update, insert, delete)는 @Modifying 어노테션을 적어줘야 한다.
    // 벌크 연산은 영속성 컨텍스트를 무시하고 실행되기 때문에, 영속성 컨텍스트에 있는 엔티티의 상태와 DB에 엔티티 상태가 달라질 수 있다.
    @Modifying(clearAutomatically = true) // clearAutomatically = true : 영속성 컨텍스를 초기화(em.clear())
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // JPQL 페치 조인
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // 공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"}) // @EntityGraph : 사실상 페치 조인의 간편 버전
    List<Member> findAll();

    // JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메서드 이름에서의 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // NamedEntityGraph 사용 방법
    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph2();

    // 쿼리 힌트 적용 : 읽기 전용 객체인 Member
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // 비관적 락(PPESSIMISTIC_WRITE) : 데이터를 읽을 때부터 락을 걸어서 다른 트랜잭션이 위 데이터를 수정하지 못하게 막는다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String name);

    // JPA 네이티브 SQL 지원
    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    // 스프링 데이터 JPA 네이티브 쿼리 + 인터페이스 기반 Projections 활용
    @Query(value = "select m.member_id as id, m.username, t.name as teamName " +
    "from member m left join team t on m.team_id = t.team_id",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}