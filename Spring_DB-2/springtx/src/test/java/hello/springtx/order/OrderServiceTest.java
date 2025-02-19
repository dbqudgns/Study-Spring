package hello.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void complete() throws NotEnoughMoneyException {

        //given
        Order order = new Order();
        order.setUsername("정상");

        //when
        orderService.order(order);

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtimeException() {

        //given
        Order order = new Order();
        order.setUsername("예외");

        //when, then : 시스템 예외 발생
        assertThatThrownBy(() -> orderService.order(order))
                .isInstanceOf(RuntimeException.class);

        //then : 롤백되었으므로 데이터가 없어야 한다.
        Optional<Order> orderOptional = orderRepository.findById(order.getId());
        assertThat(orderOptional.isEmpty()).isTrue();

    }

    @Test
    void bizException() throws NotEnoughMoneyException {

        //given
        Order order = new Order();
        order.setUsername("잔고부족");

        //when //비즈니스 예외 발생
        try {
            orderService.order(order);
            fail("잔고 부족 예외가 발생해야 합니다.");
        }catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내");
        }

        //then : 비즈니스 예외(체크 예외)라 커밋 됐다.
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("대기");
    }


}