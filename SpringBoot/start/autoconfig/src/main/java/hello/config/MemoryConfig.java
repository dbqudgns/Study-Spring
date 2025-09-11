package hello.config;

import memory.MemoryCondition;
import memory.MemoryController;
import memory.MemoryFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
// @Conditional(MemoryCondition.class) // MemoryConfig의 실행 여부는 MemoryCondition의 matches에 따라 좌우된다.
@ConditionalOnProperty(name = "memory", havingValue = "on") // 환경 정보가 memory=on 이라는 조건에 맞으면 동작, 아니면 동작X
public class MemoryConfig {

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }

}
