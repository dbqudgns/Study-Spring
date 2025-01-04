package hello.springtx.order;

import org.springframework.data.jpa.repository.JpaRepository;

//스프링이 프록시 기술을 사용하여 OrderRepository 구현체를 만들어 스프링 빈으로 등록 해준다.
public interface OrderRepository extends JpaRepository<Order, Long> {
}
