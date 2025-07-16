package study.data_jpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 도메인 클래스 컨버터 : URL 경로의 ID를 자동으로 엔티티 객체로 변환해주는 스프링 MVC 기능

    // 도메인 클래스 컨버터 사용 전
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 도메인 클래스 컨버터 사용 후
    // HTTP 요청은 회원 id를 받지만 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환
    // 도메인 클래스 컨버터도 repository를 사용해서 엔티티를 찾는다.
    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // 스프링 데이터 JPA가 제공하는 페이징과 정렬
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    // 페이징 개별 설정
    @GetMapping("/members_page")
    public Page<Member> list2(@PageableDefault(size = 12, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    // 페이징 내용을 DTO로 변환 : Page.map() 활용
    @GetMapping("/members_pageDto")
    public Page<MemberDto> list3(@PageableDefault Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> pageDto = page.map(MemberDto::new);
        return pageDto;
    }

    @PostConstruct // 스프링 빈이 생성되고 의존성 주입까지 완료된 후에 자동으로 실행되는 초기화 메서드에 붙이는 어노테이션
    public void init() {
        for (int i = 0 ; i < 100; i++)
            memberRepository.save(new Member("user" + i, i));
    }

}
