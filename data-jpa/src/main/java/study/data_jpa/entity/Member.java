package study.data_jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    protected Member() { // JPA가 프록시 객체를 만들 때 기본 생성자를 통해 만드는데 private로 접근 지정자를 선언할 시 생성이 안된다.
    }

    public Member(String username) {
        this.username = username;
    }
}
