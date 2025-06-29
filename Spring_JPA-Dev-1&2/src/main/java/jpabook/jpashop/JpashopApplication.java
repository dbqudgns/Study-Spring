package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean // /api/v1/simple-orders 연관
	Hibernate5JakartaModule hibernate5JakartaModule() {
		return new Hibernate5JakartaModule(); // 초기화된 프록시 객체만 노출하고 초기화 되지 않은 프록시 객체는 null 처리
		// 이때 초기화된 프록시 객체가 요청이 필요한 객체와 양방향 관계일 경우 무한 루프가 빠진다.
		// 따라서 한쪽에 @JsonIgnore 설정이 필요하다 !
	}

//	@Bean // /api/v1/simple-orders 연관
//	Hibernate5JakartaModule hibernate5Module() {
//		Hibernate5JakartaModule hibernate5Module= new Hibernate5JakartaModule();
//
//		// 강제 지연 로딩 설정 : 양방향 연관관계를 계속 로딩하여 무한 루프가 빠지므로 한쪽에 @JsonIgnore 설정이 필요하다.
//		hibernate5Module.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
//
//		return hibernate5Module;
//	}

}
