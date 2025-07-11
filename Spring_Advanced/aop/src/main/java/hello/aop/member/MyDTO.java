package hello.aop.member;

import hello.aop.member.annotation.ClassAop;

@ClassAop
public class MyDTO {
    String name;

    public MyDTO(String name) {
        this.name = name;
    }
}
