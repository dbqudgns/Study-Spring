package hello.order.v4;

import hello.order.OrderService;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV4 {

    @Bean
    OrderService orderService() {
        return new OrderServiceV4();
    }

    // TimedAspect를 적용해야 @Timed에 AOP가 적용된다.
    // TimedAspect를 등록하면 @Timed를 인지해서 Timer를 사용하는 메서드 또는 타입에 AOP를 적용한다.
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
