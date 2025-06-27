package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository_Old;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
/**
 @Transactional(readOnly = true)를 명시하지 않아도
 → 프록시 객체는 생성되고,
 → 엔티티의 Lazy 로딩 설정도 유지된다.
 그러나 중요한 건 "트랜잭션의 범위 안에서만 Lazy 로딩이 안전하게 작동한다"는 점 => 예외가 발생함 => OSIV를 켜놓은 상태이기 때문에 예외는 발생하지 않음
 */
public class OrderService {

    private final MemberRepository_Old memberRepositoryOld;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    /** 주문 */
    @Transactional
    public Long order (Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepositoryOld.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        /** Order 엔티티에서 Delivery와 OrderItem에 Cascade.ALL로 설정을 하였기에
         /* Order 저장 시 Delivery와 OrderItem도 persist 된다 ! 즉, Repository가 필요없다.
         */

        return order.getId();
    }

    /** 주문 취소 */
    @Transactional
    public void cancelOrder(Long orderId) {

        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();
    }

    /** 주문 검색 */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }



}
