package hello;

import hello.boot.MySpringApplication;
import hello.boot.MySpringBootApplication;

@MySpringBootApplication
public class MySpringBootMain {

    public static void main(String[] args) {
        System.out.println("MySpringBootMain.main");
        MySpringApplication.run(MySpringBootMain.class, args); // 컴포넌트 스캔의 정보를 담는 MySpringBootMain.class을 설정 파일로 보낸다.
    }
}
