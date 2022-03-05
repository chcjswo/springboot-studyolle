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
import org.springframework.web.bind.annotation.PathVariable;
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

		accountService.completeSignUp(account);
		model.addAttribute("numberOfUser", accountRepository.count());
		model.addAttribute("nickname", account.getNickname());
		return view;
	}

	@GetMapping("/check-email")
	public String checkEmail(@CurrentUser Account account, Model model) {
		model.addAttribute("email", account.getEmail());
		return "account/check-email";
	}

	@GetMapping("/resend-confirm-email")
	public String resendConfirmEmail(@CurrentUser Account account, Model model) {
		if (!account.canResendConfirmEmail()) {
			model.addAttribute("error", "인증 이메일은 1시간에 한번만 전송할 수 있습니다.");
			model.addAttribute("email", account.getEmail());
			return "account/check-email";
		}
		accountService.sendSignUpConfirmEmail(account);
		return "account/check-email";
	}

	@GetMapping("/profile/{nickname}")
	public String viewProfile(@PathVariable String nickname,
							  Model model,
							  @CurrentUser Account account) {
		final Account byNickname = accountRepository.findByNickname(nickname);
		if (nickname == null) {
			throw  new IllegalArgumentException(nickname + "에 해당하는 유저가 없습니다.");
		}

		model.addAttribute(byNickname);
		model.addAttribute("isOwner", byNickname.equals(account));
		return "account/profile";
	}
}
