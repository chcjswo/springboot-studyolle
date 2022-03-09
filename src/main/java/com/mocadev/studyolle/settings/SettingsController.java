package com.mocadev.studyolle.settings;

import com.mocadev.studyolle.account.AccountService;
import com.mocadev.studyolle.account.CurrentUser;
import com.mocadev.studyolle.domain.Account;
import com.mocadev.studyolle.settings.form.NicknameForm;
import com.mocadev.studyolle.settings.form.Notifications;
import com.mocadev.studyolle.settings.form.PasswordForm;
import com.mocadev.studyolle.settings.form.Profile;
import com.mocadev.studyolle.settings.validator.NicknameValidator;
import com.mocadev.studyolle.settings.validator.PasswordFormValidator;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

	public static final String SETTINGS_NOTIFICATIONS_VIEW_NAME = "settings/notifications";
	public static final String SETTINGS_NOTIFICATIONS_URL = "/settings/notifications";

	public static final String SETTINGS_ACCOUNT_VIEW_NAME = "settings/account";
	public static final String SETTINGS_ACCOUNT_URL = "/settings/account";

	private final AccountService accountService;
	private final ModelMapper modelMapper;
	private final NicknameValidator nicknameValidator;

	@InitBinder("passwordForm")
	public void passwordFormInitBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(new PasswordFormValidator());
	}

	@InitBinder("nicknameForm")
	public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(nicknameValidator);
	}

	@GetMapping(SETTINGS_PROFILE_URL)
	public String profileUpdateForm(@CurrentUser Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(modelMapper.map(account, Profile.class));
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

	@GetMapping(SETTINGS_NOTIFICATIONS_URL)
	public String notificationsUpdateForm(@CurrentUser Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(modelMapper.map(account, Notifications.class));
		return SETTINGS_NOTIFICATIONS_VIEW_NAME;
	}

	@PostMapping(SETTINGS_NOTIFICATIONS_URL)
	public String notificationsUpdate(@CurrentUser Account account,
								 @Valid Notifications notifications,
								 Errors errors,
								 Model model,
								 RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return SETTINGS_NOTIFICATIONS_VIEW_NAME;
		}
		accountService.updateNotifications(account, notifications);
		attributes.addFlashAttribute("message", "알림 정보를 수정했습니다.");
		return "redirect:" + SETTINGS_NOTIFICATIONS_URL;
	}

	@GetMapping(SETTINGS_ACCOUNT_URL)
	public String accountUpdateForm(@CurrentUser Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(modelMapper.map(account, NicknameForm.class));
		return SETTINGS_ACCOUNT_VIEW_NAME;
	}

	@PostMapping(SETTINGS_ACCOUNT_URL)
	public String accountUpdate(@CurrentUser Account account,
								 @Valid NicknameForm nicknameForm,
								 Errors errors,
								 Model model,
								 RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(account);
			return SETTINGS_ACCOUNT_VIEW_NAME;
		}
		accountService.updateNickname(account, nicknameForm.getNickname());
		attributes.addFlashAttribute("message", "닉네임을 수정했습니다.");
		return "redirect:" + SETTINGS_ACCOUNT_URL;
	}
}
