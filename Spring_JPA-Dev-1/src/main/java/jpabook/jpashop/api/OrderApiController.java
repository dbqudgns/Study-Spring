package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY = NULL 처리
     * - 양방향 관계 문제 발생하므로 @JsonIgnore 처리 필요
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {

        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems(); //getTotalPrice()를 설정해두었기에 orderItem select 절이 나가지 않음
            orderItems.stream().forEach(o -> o.getItem().getName()); // Lazy 강제 초기화
        }

        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용 X)
     * - 단점 : 지연로딩으로 인해 N번 호출
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {

        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {

        List<Order> orders = orderRepository.findAllWithItem();

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @Getter
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // 강제 LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // 강제 LAZY 초기화
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName(); // 강제 LAZY 초기화
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
