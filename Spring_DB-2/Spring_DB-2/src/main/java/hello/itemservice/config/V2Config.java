package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice.repository.v2.ItemQueryRepositoryV2;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
public class V2Config {

    private final EntityManager em;
    private final ItemRepositoryV2 itemRepositoryV2; //SpringDataJPA

    @Bean //ItemRepositoryTestV2을 위해 작성
    public ItemService itemService() {
        return new ItemServiceV2(itemRepositoryV2, itemQueryRepositoryV2());
    }

    @Bean //ItemRepositoryTestV2을 위해 작성
    public ItemQueryRepositoryV2 itemQueryRepositoryV2() {
        return new ItemQueryRepositoryV2(em);
    }

    @Bean //ItemRepositoryTest와 어플리케이션 작동을 위해 작성
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(em);
    }

}
