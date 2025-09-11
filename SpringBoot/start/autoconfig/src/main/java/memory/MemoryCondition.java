package memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Slf4j
public class MemoryCondition implements Condition {

    /**
     * matches() 메서드가 true를 반환하면 조건에 만족해서 동작하고, false를 반환하면 동작하지 않는다.
     * @param context : 스프링 컨테이너, 환경 정보 등을 담는다.
     * @param metadata : 애노테이션 메타 정보를 담는다.
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String memory = context.getEnvironment().getProperty("memory");
        log.info("memory={}", memory);
        return "on".equals(memory);
    }
}
