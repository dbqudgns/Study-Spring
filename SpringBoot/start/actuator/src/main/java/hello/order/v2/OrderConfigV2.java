package hello.order.v2;

import hello.order.OrderService;
import hello.order.v1.OrderServiceV2;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV2 {

    @Bean
    public OrderService orderService() {
        return new OrderServiceV2();
    }

    // CountedAspect를 등록하면 @Counted를 인지해서 Counter를 사용하는 AOP를 적용한다.
    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
