package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.memory.MemoryItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//의존성 주입 : 객체가 직접 필요한 의존성(다른 객체)를 생성하지 않고 외부에서 주입받는 방식
@Configuration //스프링 컨테이너는 MemoryConfig에 정의된 메서드를 호출하여 스프링 빈을 생성하고 관리한다.
public class MemoryConfig {

    @Bean //메서드의 반환 값을 스프링 빈으로 등록한다.
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean //해당 빈들은 스프링 컨테이너에 의해 관리되며, 다른 빈에서 주입 받을 수 있다.
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

}

