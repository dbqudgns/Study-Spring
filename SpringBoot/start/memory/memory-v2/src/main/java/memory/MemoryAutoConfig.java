package memory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration // 스프링 부트가 제공하는 자동 구성 기능을 적용할 때 사용하는 애노테이션
@ConditionalOnProperty(name = "memory", havingValue = "on") // memory=on 이라는 환경 정보가 있을 때 스프링 빈을 등록한다.
public class MemoryAutoConfig {

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }

}
