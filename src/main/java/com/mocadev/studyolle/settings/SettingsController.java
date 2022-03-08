package com.mocadev.studyolle.settings;

import com.mocadev.studyolle.account.AccountService;
import com.mocadev.studyolle.account.CurrentUser;
import com.mocadev.studyolle.domain.Account;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-06
 **/
@Controller
@RequiredArgsConstructor
public class SettingsController {

	public static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";
	public static final String SETTINGS_PROFILE_URL = "/settings/profile";

	public static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";
	public static final String SETTINGS_PASSWORD_URL = "/settings/password";

	private final AccountService accountService;

	@InitBinder("passwordForm")
	public void passwordFormInitBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(new PasswordFormValidator());
	}

	@GetMapping(SETTINGS_PROFILE_URL)
	public String profileUpdateForm(@CurrentUser Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(new Profile(account));
		return SETTINGS_PROFILE_VIEW_NAME;
	}

	@PostMapping(SETTINGS_PROFILE_URL)
	public String updateProfile(@CurrentUser Account account,
								@Valid Profile profile,
								Errors errors,
								Model model,
								RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return SETTINGS_PROFILE_VIEW_NAME;
		}
		accountService.updateProfile(account, profile);
		attributes.addFlashAttribute("message", "프로필을 수정했습니다.");
		return "redirect:" + SETTINGS_PROFILE_URL;
	}

	@GetMapping(SETTINGS_PASSWORD_URL)
	public String passwordUpdateForm(@CurrentUser Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(new PasswordForm());
		return SETTINGS_PASSWORD_VIEW_NAME;
	}

	@PostMapping(SETTINGS_PASSWORD_URL)
	public String updatePassword(@CurrentUser Account account,
								 @Valid PasswordForm passwordForm,
								 Errors errors,
								 Model model,
								 RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return SETTINGS_PASSWORD_VIEW_NAME;
		}
		accountService.updatePassword(account, passwordForm.getNewPassword());
		attributes.addFlashAttribute("message", "패스워드를 수정했습니다.");
		return "redirect:" + SETTINGS_PASSWORD_URL;
	}
}
