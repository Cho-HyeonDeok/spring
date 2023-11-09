package com.daewoo.market;

import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vam.model.MemberVO;
import com.vam.service.MemberService;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private MemberService memberservice;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private BCryptPasswordEncoder pwEncoder;

	// �쉶�썝媛��엯 �럹�씠吏� �씠�룞
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public void joinGET() {

		logger.info("�쉶�썝媛��엯 �럹�씠吏� 吏꾩엯");

	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String joinPOST(MemberVO member) throws Exception {

		String rawPw = "";		// 인코딩 전 비번
		String encodePw = "";	// 인코딩 후 비번
		
		rawPw = member.getMemberPw();		// 비밀번호 데이터 얻기
		encodePw = pwEncoder.encode(rawPw);	// 비번 인코딩
		member.setMemberPw(encodePw);		// 인코딩 비번 다시 저장
		
		/* 회원가입 쿼리 실행 */
		memberservice.memberJoin(member);
		
		return "redirect:/main";
	}

	// 濡쒓렇�씤 �럹�씠吏� �씠�룞
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public void loginGET() {

		logger.info("濡쒓렇�씤 �럹�씠吏� 吏꾩엯");

	}

	// �븘�씠�뵒 以묐났 寃��궗
	@RequestMapping(value = "/memberIdChk", method = RequestMethod.POST)
	@ResponseBody
	public String memberIdChkPOST(String memberId) throws Exception {

		/* logger.info("memberIdChk() 吏꾩엯"); */

		int result = memberservice.idCheck(memberId);

		logger.info("寃곌낵媛� = " + result);

		if (result != 0) {

			return "fail"; // 以묐났 �븘�씠�뵒 o

		} else {

			return "success"; // 以묐났 �븘�씠�뵒 x

		}
	} // memberIdChkPOST() 醫낅즺

	/* 이메일 인증 */
	@RequestMapping(value = "/mailCheck", method = RequestMethod.GET)
	@ResponseBody
	public String mailCheckGET(String email) throws Exception {

		/* 뷰(veiw)로부터 넘어온 데이터 확인 */
		logger.info("이메일 데이터 전송 확인");
		logger.info("인증번호 : " + email);

		/* 인증번호(난수) 생성 */
		Random random = new Random();
		int checkNum = random.nextInt(888888) + 111111;
		logger.info("인증번호 : " + checkNum);

		/* 이메일 보내기 */
		String setFrom = "sante1458@naver.com";
		String toMail = email;
		String title = "회원가입 인증 이메일 입니다.";
		String content = "홈페이지를 방문해주셔서 감사합니다." + "<br><br>" + "인증 번호는 " + checkNum + "입니다." + "<br>"
				+ "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
		
		try {
			
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			helper.setText(content,true);
			mailSender.send(message);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		String num = Integer.toString(checkNum);
		
		return num;
	}

	/* 로그인 */
    @RequestMapping(value="login.do", method=RequestMethod.POST)
    public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception{
        
    	HttpSession session = request.getSession();
    	String rawPw = "";
    	String encodePw = "";
    	
    	MemberVO lvo = memberservice.memberLogin(member); // 아이디 일치 여부 확인
    	
    	if (lvo != null) { // 아이디 일치
    		
    		rawPw = member.getMemberPw(); // 사용자 제출 비번
    		encodePw = lvo.getMemberPw(); // 데이터베이스에 저장된 인코딩 비번
    		
    		if (true == pwEncoder.matches(rawPw, encodePw)) { // 비번 일치 판단
    			
    			lvo.setMemberPw("");					// 인코딩된 비번 정보 지움
    			session.setAttribute("member", lvo); 	// session에 사용자 정보 전달
    			return "redirect:/main";				// 메인 페이지 이동
    			
    		} else {
    			
    			rttr.addFlashAttribute("result", 0);
        		return "redirect:/member/login"; // 로그인 페이지 이동
    		}
    		
    	} else {			// 아이디 불일치 (로그인 실패)
    		
    		rttr.addFlashAttribute("result", 0);
    		return "redirect:/member/login"; // 로그인 페이지 이동
    	}
    	
    	
    }
    
    /* 메인 페이지 로그아웃 */
    @RequestMapping(value="logout.do", method=RequestMethod.GET)
    public String logoutMainGET(HttpServletRequest request) throws Exception {
    	
    	logger.info("logoutMainGET 메소드 진입");
    	
    	HttpSession session = request.getSession();
    	
    	session.invalidate();
    	
    	return "redirect:/main";
    	
    }
    
    /* 비동기방식 로그아웃 메소드 */
    @RequestMapping(value="logout.do", method=RequestMethod.POST)
    @ResponseBody // ajax 반환 시 필요
    public void logoutPOST(HttpServletRequest request) throws Exception{
    	
    	logger.info("비동기 로그아웃 메소드 진입");
    	
    	HttpSession session = request.getSession();
    	
    	session.invalidate();
    }


}