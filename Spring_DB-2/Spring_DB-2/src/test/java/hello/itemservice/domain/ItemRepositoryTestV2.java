package hello.itemservice.domain;

import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.repository.memory.MemoryItemRepository;
import hello.itemservice.repository.v2.ItemQueryRepositoryV2;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional //클래스에 존재하면 전체 테스트에 트랜잭션이 적용된다.
@SpringBootTest //ItemServiceApplication에 있는 @SpringBootApplication을 찾아 해당 클래스의 설정을 사용한다.
//강의에 나와있지 않은 코드 (본인 작성) => ItemQueryRepositoryV2와 ItemRepositoryV2를 테스트하기 위해 작성
class ItemRepositoryTestV2 {

    @Autowired
    ItemRepositoryV2 itemRepositoryV2;

    @Autowired
    ItemQueryRepositoryV2 itemQueryRepositoryV2;

    //트랜잭션 관련 코드
    /*@Autowired
    PlatformTransactionManager transactionManager; //스프링 부트에서 트랜잭션 매니저를 스프링 빈으로 등록해준다.

    TransactionStatus status;

    @BeforeEach
    void beforeEach() {
        //트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }
    */

    @AfterEach
    void afterEach() {
        //MemoryItemRepository 의 경우 제한적으로 사용
        if (itemRepositoryV2 instanceof MemoryItemRepository) {
            ((MemoryItemRepository) itemRepositoryV2).clearStore();
        }

        //트랜잭션 롤백
       // transactionManager.rollback(status);
    }

    //@Commit //테스트 종료 후 롤백 대신 커밋이 호출된다. == @Rollback(value = false)
    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item savedItem = itemRepositoryV2.save(item);

        //then
        Item findItem = itemRepositoryV2.findById(item.getId()).orElseThrow();
        assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("item1", 10000, 10);
        Item savedItem = itemRepositoryV2.save(item);
        Long itemId = savedItem.getId();

        //when : ItemRepositoryTest와 다른 부분(코드)
        ItemUpdateDto updateParam = new ItemUpdateDto("item2", 20000, 30);
        Item findItem = itemRepositoryV2.findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());


        //then
        Item findItem2 = itemRepositoryV2.findById(itemId).orElseThrow();
        assertThat(findItem2.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem2.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem2.getQuantity()).isEqualTo(updateParam.getQuantity());
    }

    @Test
    void findItems() {
        //given
        Item item1 = new Item("itemA-1", 10000, 10);
        Item item2 = new Item("itemA-2", 20000, 20);
        Item item3 = new Item("itemB-1", 30000, 30);

        itemRepositoryV2.save(item1);
        itemRepositoryV2.save(item2);
        itemRepositoryV2.save(item3);

        //둘 다 없음 검증
        test(null, null, item1, item2, item3);
        test("", null, item1, item2, item3);

        //itemName 검증
        test("itemA", null, item1, item2);
        test("temA", null, item1, item2);
        test("itemB", null, item3);

        //maxPrice 검증
        test(null, 10000, item1);

        //둘 다 있음 검증
        test("itemA", 10000, item1);
    }

    void test(String itemName, Integer maxPrice, Item ... items) {
        List<Item> result = itemQueryRepositoryV2.findAll(new ItemSearchCond(itemName, maxPrice));
        assertThat(result).containsExactly(items);
    }
}
