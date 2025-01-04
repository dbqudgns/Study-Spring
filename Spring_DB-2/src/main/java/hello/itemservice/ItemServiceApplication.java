package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
//@Import(JdbcTemplateV3Config.class)
//@Import(MyBatisConfig.class)
//@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
//@Import(QuerydslConfig.class)
@Import(V2Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	@Bean
	@Profile("local") //프로필 : 로컬(나의 PC), 운영 환경, 테스트 실행 등등 다양한 환경에 따라서 다른 설정을 할 때 사용하는 정보이다.
	/*
	스프링 애플리케이션이 실행될 때,
	현재 활성화된 프로필(ex : spring.profiles.active=local)에 따라 특정 빈만 초기화된다.
	 */
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

	@Bean
	@Profile("test") // 프로필이 "test"인 경우에만 데이터소스를 스프링 빈으로 등록한다.
	//임베디드 데이터베이스 사용을 위한 DataSource 설정
	public DataSource dataSource() {
		log.info("메모리 데이터베이스 초기화");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
		/**
		 * jdbc:h2:mem:db : 임베디드 모드로 동작하는 H2 데이터베이스를 사용할 수 있다.
		 * DB_CLOSE_DELAY=-1 : 임베디드 모드에서 데이터베이스 커넥션 연결이 모두 끊어질 때 데이터베이스가 종료되지 않도록 설정
		**/
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}

}
