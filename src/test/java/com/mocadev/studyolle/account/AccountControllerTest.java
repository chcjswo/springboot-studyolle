package com.mocadev.studyolle.account;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.mocadev.studyolle.domain.Account;
import com.mocadev.studyolle.mail.ConsoleMailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("local")
@Transactional
class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	AccountRepository accountRepository;

	@MockBean
	ConsoleMailSender javaMailSender;

	@DisplayName("회원 가입 화면 보이는 테스트")
	@Test
	void signUpForm() throws Exception {
		mockMvc.perform(get("/sign-up"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andExpect(model().attributeExists("signUpForm"))
			.andExpect(unauthenticated());
	}

	@DisplayName("회원 가입 처리 - 입력값 오류")
	@Test
	void signUpSubmit_with_wrong_input() throws Exception {
		mockMvc.perform(post("/sign-up")
				.param("nickname", "chcjswo")
				.param("email", "email..")
				.param("password", "12345")
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andExpect(model().attributeExists("signUpForm"))
			.andExpect(unauthenticated());
	}

	@DisplayName("회원 가입 처리 - 입력값 정상")
	@Test
	void signUpSubmit_with_correct_input() throws Exception {
		mockMvc.perform(post("/sign-up")
				.param("nickname", "chcjswo")
				.param("email", "test@test.com")
				.param("password", "12345678")
				.with(csrf()))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"))
			.andExpect(authenticated().withUsername("chcjswo"));

		final Account account = accountRepository.findByEmail("test@test.com");
		assertNotNull(account);
		assertNotNull(account.getEmailCheckToken());
		assertNotEquals(account.getPassword(), "12345678");
		assertTrue(accountRepository.existsByEmail("test@test.com"));
		then(javaMailSender).should().send(any(SimpleMailMessage.class));
	}

	@DisplayName("인증 메일 확인 - 입력값 오류")
	@Test
	void checkEmail_with_wrong_input() throws Exception {
		mockMvc.perform(get("/check-email-token")
				.param("token", "test")
				.param("email", "test@test.com"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("account/checked-email"))
			.andExpect(model().attributeExists("error"))
			.andExpect(unauthenticated());
	}

	@DisplayName("인증 메일 확인 - 입력값 정상")
	@Test
	void checkEmail_with_correct_input() throws Exception {
		final Account account = Account.builder()
			.email("test@test.com")
			.password("12345678")
			.nickname("test")
			.build();
		final Account newAccount = accountRepository.save(account);
		newAccount.generateEmailCheckToken();

		mockMvc.perform(get("/check-email-token")
				.param("token", newAccount.getEmailCheckToken())
				.param("email", newAccount.getEmail()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("account/checked-email"))
			.andExpect(model().attributeDoesNotExist("error"))
			.andExpect(model().attributeExists("nickname"))
			.andExpect(model().attributeExists("numberOfUser"))
			.andExpect(authenticated().withUsername("test"));
	}

}
