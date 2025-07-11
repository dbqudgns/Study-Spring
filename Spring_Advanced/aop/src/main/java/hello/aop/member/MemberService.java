package hello.aop.member;

public interface MemberService {
    String hello(String param);

    String internal(MyDTO myDTO);
}
