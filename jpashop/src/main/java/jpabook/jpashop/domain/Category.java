package jpabook.jpashop.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToOne //여러 개의 하위 카테고리(Many)가 하나의 상위 카테고리(One)를 참조한다.
    @JoinColumn(name = "PARENT_ID") //자기 자신을 호출하는 self join이다. ( self join 할 때 키는 @JoinColumn에서 지정한 "PARENT_ID(FK)"을 사용한다.)
    private Category parent;

    @OneToMany(mappedBy = "parent") //상위 카테고리(Many) 하나가 여러 하위 카테고리(One)를 참조한다.
    private List<Category> child = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CATEGORY_ITEM", //연결 테이블 이름 설정
            joinColumns = @JoinColumn(name = "CATEGORY_ID"), //연결 테이블에 조인해야 할 외래키명
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID")) //반대 테이블(Item) 기준에서 연결 테이블에 조인해야 할 외래키명
    private List<Item> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChild() {
        return child;
    }

    public void setChild(List<Category> child) {
        this.child = child;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
