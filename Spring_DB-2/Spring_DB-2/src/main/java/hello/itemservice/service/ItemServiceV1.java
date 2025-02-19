package hello.itemservice.service;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //클래스의 final 필드(itemRepository)을 매개변수로 받는 생성자를 자동으로 생성합니다.
public class ItemServiceV1 implements ItemService {

    private final ItemRepository itemRepository;

    //@RequiredArgsConstructor가 아래와 같은 생성자를 자동으로 생성해준다.
   /* public ItemServiceV1(ItemRepository itemRepository) {
        this.itemRepository = itemRepository; => 스프링 컨테이너에 의해 빈으로 등록된 빈 타입이 ItemRepostiory를 찾아 MemoryItemRepository를 주입받는다.
    }*/

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemRepository.update(itemId, updateParam);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        return itemRepository.findAll(cond);
    }
}
