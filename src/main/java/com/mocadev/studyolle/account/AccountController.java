package com.mocadev.studyolle.account;

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

@Controller
@RequiredArgsConstructor
public class AccountController {

	private final SingUpFormValidator singUpFormValidator;
	private final AccountService accountService;
	private final AccountRepository accountRepository;

	@InitBinder("signupForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(singUpFormValidator);
	}

	@GetMapping("/sign-up")
	public String signUpForm(Model model) {
		model.addAttribute(new SignUpForm());
		return "account/sign-up";
	}

	@PostMapping("/sign-up")
	public String signUp(@Valid SignUpForm signUpForm, Errors errors) {
		if (errors.hasErrors()) {
			return "account/sign-up";
		}
		final Account account = accountService.processNewAccount(signUpForm);
		accountService.login(account);
		return "redirect:/";
	}

	@GetMapping("/check-email-token")
	public String checkEmailToken(String token, String email, Model model) {
		final Account account = accountRepository.findByEmail(email);
		final String view = "account/checked-email";
		if (account == null) {
			model.addAttribute("error", "wrong email");
			return view;
		}
		if (!account.isValidToken(token)) {
			model.addAttribute("error", "wrong.token");
			return view;
		}

		account.completeSignUp();
		accountService.login(account);
		model.addAttribute("numberOfUser", accountRepository.count());
		model.addAttribute("nickname", account.getNickname());
		return view;
	}
}
