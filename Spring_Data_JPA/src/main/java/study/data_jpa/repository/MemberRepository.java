package study.data_jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

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

}