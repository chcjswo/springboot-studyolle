package com.mocadev.studyolle.main;

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
 * @since 2022-03-04
 **/
@Controller
public class MainController {

	@GetMapping("/")
	public String home(@CurrentUser Account account, Model model) {
		if (account != null) {
			model.addAttribute(account);
		}

		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}
}
