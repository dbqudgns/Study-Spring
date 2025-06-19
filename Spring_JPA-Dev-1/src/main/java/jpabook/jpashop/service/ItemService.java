package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // 상품 저장
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 모든 상품 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // 단일 상품 조회
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
