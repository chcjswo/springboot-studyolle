package com.mocadev.studyolle.settings;

import com.mocadev.studyolle.account.CurrentUser;
import com.mocadev.studyolle.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-06
 **/
@Controller
public class SettingsController {

	@GetMapping("/settings/profile")
	public String profileUpdateForm(@CurrentUser Account account, Model model) {
		model.addAttribute(account);
		model.addAttribute(new Profile(account));
		return "settings/profile";
	}
}
