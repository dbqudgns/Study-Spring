package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity //JPA가 사용하는 객체(엔티티)
public class Item {

    @Id //Item 테이블의 기본 키와 id필드를 매핑한다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본 키 값을 데이터베이스에서 생성하는 IDENTITY 방식을 사용한다.
    private Long id;

    @Column(name = "item_name", length = 10) //객체의 필드(itemName)를 테이블의 속성(item_name)과 매핑한다. , 테이블의 속성(item_name) 값 길이를 10으로 지정한다.
    private String itemName; //스프링 부트에서는 객체 필드의 카멜 케이스(itemName)를 테이블 속성의 언더스코어(item_name)로 자동 변환해준다.
    private Integer price; //@Column을 생략할 경우 필드의 이름을 테이블 속성명으로 사용된다.
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
