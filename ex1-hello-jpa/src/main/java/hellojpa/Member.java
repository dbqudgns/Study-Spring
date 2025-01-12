package hellojpa;

import jakarta.persistence.*;

import java.util.Date;


// 2. SEQUENCE 전략 : 데이터베이스 시퀀스를 이용
/* @SequenceGenerator (
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름 (DB에 해당 이름으로 저장됨)
        initialValue = 1, allocationSize = 1
)*/

//3. TABLE 전략 : 키 생성 전용 테이븚을 하나 만들어서 데이터베이스 시퀀스를 흉내내는 전략
/*@TableGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "MEMBER_SEQ", allocationSize = 1)*/

@Entity //JPA가 관리하는 객체

public class Member {

    @Id //PK
    //@GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR" )
    //@GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name") //DB에는 "name"로 속성명이 지정된다.
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING) //enum 타입을 명시 : enum 이름(USER, ADMIN)을 DB에 저장
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) //날짜와 시간을 DB에 저장
    private Date createdDate;

    @Lob // 여기서는 DB에 CLOB 타입과 매핑된다.
    private String description;

    public Member() {} //JPA는 기본 생성자가 꼭 필요하다

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }
}
