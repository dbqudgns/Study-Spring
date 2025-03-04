# 자바 ORM 표준 JPA 프로그래밍 - 기본편

### 2025/01/09 : JPA 시작하기 

- 실습에 앞선 프로젝트 환경설정
- JPA 맛보기 

### 2025/01/11 : [ 영속성 관리 - 내부 동작 방식 ] , [ 엔티티 매핑 ]

- JPA 동작 과정인 영속성 컨텍스트(1차 캐시) 흐름 및 이점
- 엔티티의 생명주기 ( 비영속, 영속, 준영속, 삭제 )
- 트랜잭션을 지원하는 쓰기 지연 저장소 및 flush()의 정의 및 동작 과정 
- 객체와 테이블 매핑 어노테이션 ( @Entity, @Table )
- 데이터베이스 스키마(DDL) 자동 생성 옵션 : hibernate.hbm2ddl.auto

### 2025/01/12 : 엔티티 매핑

- 엔티티 필드와 DB 칼럼 매핑을 위한 다양한 어노테이션
- 기본 키 매핑을 위한 옵션 3가지
- 엔티티 매핑 지식 기반으로 엔티티 설계 실습 및 문제점

### 2025/01/13 : 연관관계 매핑 기초 

- 연관관계 매핑(객체의 참조(주소)와 테이블의 외래키를 매핑하는 JPA계의 포인터) 학습
- JPA에서의 단방향 연관관계 매핑
- JPA에서의 양방향 연관관계의 매핑과 연관관계의 주인(외래키가 있는 테이블) 
- 연관관계 매핑 지식 기반으로 작성해둔 엔티티들 간의 연관관계 매핑 실습 

### 2025/01/14 : 다양한 연관관계 매핑

- 다대일, 일대다, 일대일, 다대다 관계에서의 연관관계 매핑 학습 
- 다양한 연관관계 매핑 지식 기반으로 작성해둔 엔티티들 간의 연관관계 매핑 실습 

### 2025/01/15 : 고급 매핑

- 상속관계 매핑(객체의 상속, 구조와 DB의 슈퍼타입, 서브타입 관계를 매핑) 전략 3가지 학습
- @MappedSuperclass : 테이블과 관계 없고, 엔티티들이 공통으로 사용하는 매핑 필드를 모으는 역할 
- 고급 매핑 지식 기반으로 작성해둔 엔티티에 상속 관계 매핑, 필드 상속(@MappedSuperclass) 실습

### 2025/01/16 ~ 2025/01/17 : 프록시와 연관관계 정리

- 프록시 객체 호출(em.getReference()) 및 초기화 동작과정, 프록시 특징 학습
- 즉시 로딩(EAGER)과 지연 로딩(LAZY) 학습
- 영속성 전이(CASCADE)와 고아 객체 학습 
- 프록시와 연관관계 정리 지식 기반으로 작성해둔 엔티티에 지연 로딩, 영속성 전이 실습

### 2025/01/17 ~ 2025/01/19 : 값 타입 

- JPA의 데이터 타입 : 엔티티 타입, 값 타입 
- 값 타입 중 기본 값 타입 학습
- 값 타입 중 임베디드 타입 및 관련 애노테이션 학습 => DB의 복합 속성 느낌 
- 값 타입 한계 및 불변 객체 학습 
- 값 타입의 비교 방법( equals ) 학습
- 값 타입 컬렉션 실습 및 그에 대한 한계점, 해결방법 학습 
- 값 타입 지식 기반으로 작성해둔 엔티티에 값 타입 적용 실습

### 2025/01/19 ~ 2025/01/20 : 객체지향 쿼리 언어(JPQL)

- JPA가 지원하는 다양한 쿼리 방법 맛보기
- JQPL 기본 문법(프로젝션, 조인) 학습
- JPQL 기능(페이징, 서브 쿼리, 타입 표현, CASE 식, 기본 함수, 사용자 정의 함수) 학습

### 2025/01/21 ~ 2025/01/25 : 객체 지향 쿼리 언어 - 중급 문법

- JPQL 경로 표현식(상태 필드, 연관 필드) 및 명시적 조인, 묵시적 조인 학습
- JPA의 페치 조인 기본 개념, 특징 및 한계점 학습
- JPQL의 다형성 쿼리(TYPE, TREAT), 엔티티 직접 사용(기본키, 외래키), Named 쿼리, 벌크 연산 학습
