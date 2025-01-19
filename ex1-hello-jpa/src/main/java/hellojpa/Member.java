package hellojpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;


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
public class Member extends BaseEntity {

    @Id //PK
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR" )
    //@GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME") //DB에는 "name"로 속성명이 지정된다.
    private String username;

    @ManyToOne(fetch = FetchType.LAZY) //MEMBER 입장에서 TEAM과 다대일 관계, 지연 로딩(LAZY)
    @JoinColumn(name = "TEAM_ID") //Member 테이블의 외래키(TEAM_ID)를 사용 
    private Team team;

    @Embedded //생략 가능
    private Period workPeriod;

    @Embedded //생략 가능
    private Address homeaddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
        @JoinColumn(name = "MEMBER_ID") //FAVORITE_FOOD 테이블의 외래키(MEMBER_ID)를 사용
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    /*
    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns =
        @JoinColumn(name = "MEMBER_ID")
    )
    private List<Address> addressHistory = new ArrayList<>();
    */

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID") //ADDRESS(AddressEntity) 테이블의 외래키를 사용
    private List<AddressEntity> addressHistory = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column=@Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street", column=@Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode", column=@Column(name = "WORK_ZIPCODE"))
    })
    private Address workaddress;

    /*
    @OneToOne
    @JoinColumn(name = "LOCKER_ID") //join할 때 상대 클래스(Locker)의 참고할 기본키(LOCKER_ID) 칼럼
    private Locker locker;
    */

    /*
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();
    */


    /* private Integer age;

    @Enumerated(EnumType.STRING) //enum 타입을 명시 : enum 이름(USER, ADMIN)을 DB에 저장
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) //날짜와 시간을 DB에 저장
    private Date createdDate;

    @Lob // 여기서는 DB에 CLOB 타입과 매핑된다.
    private String description;
*/
    public Member() {} //JPA는 기본 생성자가 꼭 필요하다

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(Address homeaddress) {
        this.homeaddress = homeaddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public Address getWorkaddress() {
        return workaddress;
    }

    public void setWorkaddress(Address workaddress) {
        this.workaddress = workaddress;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }
}
