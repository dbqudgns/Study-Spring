package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long>
{

    //select i from Item i where i.name like ? => | 이름 조건만 검색할 시 사용되는 쿼리 메서드
    List<Item> findByItemNameLike(String itemName);

    //select i from Item i where i.price <= ? => | 가격 조건만 검색할 시 사용되는 쿼리 메서드
    List<Item> findByPriceLessThanEqual(Integer price);

    //select i from Item i where i.itemName like ? and i.price <= ? | 이름과 가격 조건을 검색했을 때 사용되는 쿼리 메서드
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    //쿼리 직접 실행 | 이름과 가격 조건을 검색했을 때 사용되는 쿼리 메서드
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price); //쿼리 직접 실행 시 파라미터 바인딩은 @Param 애노테이션을 사용해야 한다.
}
