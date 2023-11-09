package com.vam.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.MemberVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class MemberMapperTest {
	
	@Autowired
	private MemberMapper membermapper; // MemberMapper.java 인터페이스 의존성 주입
	
	/*
	 * // 회원가입 쿼리 테스트 메서드
	 * 
	 * @Test public void memberJoin() throws Exception{ MemberVO member = new
	 * MemberVO();
	 * 
	 * member.setMemberId("spring_test"); member.setMemberPw("spring_test");
	 * member.setMemberName("spring_test"); member.setMemberMail("spring_test");
	 * member.setMemberAddr1("spring_test"); member.setMemberAddr2("spring_test");
	 * member.setMemberAddr3("spring_test");
	 * 
	 * membermapper.memberJoin(member); }
	 */
	
	// 아이디 중복검사
	/*
	 * @Test public void memberIdChk() throws Exception{ String id = "admin"; String
	 * id2 = "test123"; membermapper.idCheck(id); membermapper.idCheck(id2); }
	 */
	
	/* 로그인 쿼리 mapper 메소드 테스트 */
	@Test
	public void memberLogin() throws Exception{
		MemberVO member = new MemberVO(); 	// MemberVO 변수 선언 및 초기화
		
		/* 올바른 아이디 비번 입력 경우 */
//		member.setMemberId("test1");
//		member.setMemberPw("test1");
		
		/* 올바르지 않은 아이디 비번 입력경우 */
		member.setMemberId("test1123");
        member.setMemberPw("test1321321");
		
		membermapper.memberLogin(member);
		System.out.println("결과 값 : " + membermapper.memberLogin(member));
	}
}
