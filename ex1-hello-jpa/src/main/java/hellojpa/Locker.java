package hellojpa;

import jakarta.persistence.*;

@Entity
public class Locker {

    @Id @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;

    private String name;

    @OneToOne(mappedBy = "locker") //Member 엔티티의 locker 필드와 매핑이 되어 있다.
    private Member member;

}
