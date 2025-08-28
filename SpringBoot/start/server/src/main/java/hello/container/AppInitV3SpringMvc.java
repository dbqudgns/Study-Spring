package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

// WebApplicationInitializer : 스프링이 이미 만들어둔 애플리케이션 초기화 인터페이스이다.
// AppInitV2Spring과 거의 유사한 코드이다.

/**
 * 스프링 MVC도 서블릿 컨테이너 초기화 파일에 초기화 클래스(SpringServletContainerInitializer)를 등록해두었다.
 * 그리고 WebApplicationInitializer 인터페이스를 애플리케이션 초기화 인터페이스로 지정해두고, 이것을 생성해서 실행한다.
 */
public class AppInitV3SpringMvc implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("AppInitV3SpringMvc.onStartup");

        // 스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(HelloConfig.class); // 컨테이너에 스프링 설정을 추가한다.

        // 스프링 MVC 디스패처 서블릿 생성 및 스프링 컨테이너 연결
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);

        // 디스패처 서블릿을 서블릿 컨테이너에 등록
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV3", dispatcher);

        // 모든 요청이 디스패처 서블릿을 통하도록 설정
        servlet.addMapping("/");

    }
}
