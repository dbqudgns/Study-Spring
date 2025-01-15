package jpabook.jpashop.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "CATEGORY_ITEM", //연결 테이블 이름 설정
            joinColumns = @JoinColumn(name = "CATEGORY_ID"), //연결 테이블에 조인해야 할 외래키명
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID")) //반대 테이블(Item) 기준에서 연결 테이블에 조인해야 할 외래키명
    private List<Item> items = new ArrayList<>();

}
