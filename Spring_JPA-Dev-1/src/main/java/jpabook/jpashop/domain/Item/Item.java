package jpabook.jpashop.domain.Item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //== 핵심 비즈니스 로직 ==//
    // 캡슐화를 통해 해당 메서드들을 사용하는 코드에 영향을 주지 않고(또는 최소화하고) 내부를 변경할 수 있는 유연함을 준다.

    // 수량 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 수량 감소
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고 수량이 더 필요합니다.");
        }
        this.stockQuantity = restStock;
    }

}
