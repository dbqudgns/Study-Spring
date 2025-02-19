package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.*;

@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em); //JPAQueryFactory는 JPA 쿼리인 JPQL을 만들기 때문에 EntityManager가 필요하다.
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    public List<Item> findAllOld(ItemSearchCond cond) {

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        QItem item = QItem.item;
        BooleanBuilder builder = new BooleanBuilder(); //여러 조건을 구성하여 where 절을 작성하도록 동적 쿼리를 생성하는데 유용한 유틸리티 클래스
        if(StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like("%" + itemName + "%"));
        }

        if(maxPrice != null) {
            builder.and(item.price.loe(maxPrice)); //loe : 작거나 같다.
        }

        List<Item> result = query
                .select(item)
                .from(item)
                .where(builder)
                .fetch();

        return result;

    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        List<Item> result = query.select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice)) //where(A, B)는 A and B로 해석
                .fetch();

        return result;
    }

    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }

        return null; //무시된다.
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }

        return null; //무시된다.
    }
}
