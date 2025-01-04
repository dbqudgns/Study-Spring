# 스프링 DB 2편 - 데이터 접근 활용 기술

### 2024/12/23 : 데이터 접근 기술-시작 
- 진행할 프로젝트 설정 및 구조 파악
- 스프링 @Profile 개념 학습
- 의존관계 자동 주입 복습

### 2024/12/24 : 데이터 접근 기술 - 스프링 JdbcTemplate

- 스프링 JdbcTemplate을 H2에 적용 및 구조 파악 
- 이름 지정 파라미터 바인딩 기법 : Map, SqlParameterSource(BeanPropertySqlParameterSource, MapSqlParameterSource)
- SQL를 바탕으로 객체를 반환해주는 기법 : BeanPropertyRowMapper
- INSERT SQL를 자동으로 생성해주는 기법 : SimpleJdbcInsert

### 2024/12/25 : 데이터 접근 기술 - 테스트

- 테스트를 위한 데이터베이스 분리 
- 테스트의 격리성을 위한 트랜잭션 도입 
- 테스트 트랜잭션 자동화 : @Transactional 
- 스프링 부트에서 임베디드 모드 DB(H2) 실행 


### 2024/12/26 : 데이터 접근 기술 - MyBatis 

- 스프링 부트에 MyBatis 환경 설정
- Mybatis 기능 : 매핑 SQL XML 작성 및 실행
- Mybatis 스프링 연동 모듈에서의 Mapper 구현체 자동 처리 과정

### 2024/12/27 : 데이터 접근 기술 - JPA

- ORM 기술 표준인 JPA 사용 목적 
- JPA 적용 및 실행
- @Repository : 스프링에서의 JPA 예외 변환

### 2024/12/28 : 데이터 접근 기술 - 스프링 데이터 JPA

- 스프링 데이터 JPA 정의 및 장점
- 스프링 데이터 JPA 적용 및 실행

### 2024/12/29 : 데이터 접근 기술 - Querydsl

- Querydsl-JPA 정의 및 활용 사례 
- Querydsl 환경 설정 및 적용 

### 2024/12/30 : 데이터 접근 기술 - 활용 방안

- 스프링 데이터 JPA에서의 트레이드 오프(구조적인 문제)
- 스프링 데이터 JPA와 Querdsl을 적용한 Service 구현 및 적용 
- JpaTransactionManager의 다양한 지원 (이론)

### 2025/01/02 ~ 2025/01/04 : 스프링 트랜잭션 이해

- @Transactional의 적용 여부 확인 및 적용 위치에 따른 우선순위 확인
- 스프링 트랜잭션 AOP에서 프록시 내부 호출 한계 및 해결방법
- 초기화 시점(@PostConstruct)에서 트랜잭션 AOP 적용되지 않음과 해결방법 
- 스프링 트랜잭션 AOP에서의 예외 처리(커밋, 롤백)와 활용 코드 작성
