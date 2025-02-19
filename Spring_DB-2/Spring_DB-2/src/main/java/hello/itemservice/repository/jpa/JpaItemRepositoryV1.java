package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional //JPA의 모든 데이터 변경(등록, 삭제, 수정)은 트랜잭션 안에서 이루어져야 한다. 조회는 트랜잭션이 없이도 가능하다.
public class JpaItemRepositoryV1 implements ItemRepository {

    //JPA의 모든 동작은 엔티티 매니저(EntityManager)를 통해 이루어진다. 엔티티 매니저는 내부에 데이터소스를 가지고 있고 DB에 접근할 수 있다.
    private final EntityManager em;

    public JpaItemRepositoryV1(EntityManager em) {
        //스프링을 통해 의존주입 받는다.
        this.em = em;
    }

    @Override
    public Item save(Item item) {
        em.persist(item); //JPA에서 객체를 데이터베이스에 저장할 때 사용되는 메서드
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
        //수정된 객체를 다시 저장해야 하는 코드가 필요하지 않나 ? : 트랜잭션이 commit 하는 시점에 JPA가 UPDATE 쿼리문을 만들어 DB에 보내고 commit한다.
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String jpql = "select i from Item i";

        Integer maxPrice = cond.getMaxPrice();
        String itemName = cond.getItemName();

        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }

        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%', :itemName, '%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
        }

        log.info("jpql={}", jpql);

        //Item 타입에 맞는 쿼리문을 반환한다.
        TypedQuery<Item> query = em.createQuery(jpql, Item.class);
        if (StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        return query.getResultList(); //List<Item>으로 반환된다.
    }
}
