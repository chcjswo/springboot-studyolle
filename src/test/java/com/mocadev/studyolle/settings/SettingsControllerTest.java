package com.mocadev.studyolle.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.mocadev.studyolle.WithAccount;
import com.mocadev.studyolle.account.AccountRepository;
import com.mocadev.studyolle.account.AccountService;
import com.mocadev.studyolle.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-06
 **/
@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	AccountService accountService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@AfterEach
	void afterEach() {
		accountRepository.deleteAll();
	}

	@WithAccount("test")
	@DisplayName("프로필 수정하기 - 정상")
	@Test
	void updateProfile() throws Exception {
		String bio = "소개";
		mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
				.param("bio", bio)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
			.andExpect(flash().attributeExists("message"));

		final Account account = accountRepository.findByNickname("test");
		assertEquals(bio, account.getBio());
	}

	@WithAccount("test")
	@DisplayName("프로필 수정하기 - 입력값 에러")
	@Test
	void updateProfile_error() throws Exception {
		String bio = "소개~~소개~~소개~~소개~~소개~~소개~~소개~~소개~~소개~~소개~~소개~~소개~~";
		mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
				.param("bio", bio)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("profile"))
			.andExpect(model().hasErrors());

		final Account account = accountRepository.findByNickname("test");
		assertNull(account.getBio());
	}

	@Test
	@DisplayName("패스워드 수정 - 정상")
	@WithAccount("test")
	void updatePasswordTest() throws Exception {
		mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
				.param("newPassword", "11111111")
				.param("newPasswordConfirm", "11111111")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl(SettingsController.SETTINGS_PASSWORD_URL))
			.andExpect(flash().attributeExists("message"));

		final Account account = accountRepository.findByNickname("test");
		assertTrue(passwordEncoder.matches("11111111", account.getPassword()));
	}
}
