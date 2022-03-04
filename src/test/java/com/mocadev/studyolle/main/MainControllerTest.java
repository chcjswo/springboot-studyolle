package com.mocadev.studyolle.main;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mocadev.studyolle.account.AccountRepository;
import com.mocadev.studyolle.account.AccountService;
import com.mocadev.studyolle.account.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-05
 **/
@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	AccountService accountService;
	@Autowired
	AccountRepository accountRepository;

	@Test
	@DisplayName("닉네임으로 로그인 성공")
	@Order(1)
	void login_with_nickname() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "test")
				.param("password", "11111111")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(authenticated().withUsername("test"));
	}

	@Test
	@DisplayName("이메일로 로그인 성공")
	@Order(2)
	void login_with_email() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "test@test.com")
				.param("password", "11111111")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(authenticated().withUsername("test"));
	}

	@Test
	@DisplayName("로그인 실패")
	@Order(3)
	void login_fail() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "test@te.com")
				.param("password", "11111111")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?error"))
			.andExpect(unauthenticated());
	}

	@WithMockUser
	@Test
	@DisplayName("로그아웃")
	@Order(4)
	void logout() throws Exception {
		mockMvc.perform(post("/logout")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(unauthenticated());
	}

	@BeforeEach
	void beforeEach() {
		final SignUpForm signUpForm = new SignUpForm();
		signUpForm.setNickname("test");
		signUpForm.setEmail("test@test.com");
		signUpForm.setPassword("11111111");
		accountService.processNewAccount(signUpForm);
	}

	@AfterEach
	void afterEach() {
		accountRepository.deleteAll();
	}
}
