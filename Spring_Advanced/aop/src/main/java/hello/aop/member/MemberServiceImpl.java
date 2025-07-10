package hello.aop.member;

import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService {

    @Override
    @MethodAop("test value") // "test value" : MethodAop 어노테이션의 value()에 전달되는 인수
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param) { return "ok"; }
}
