package com.mocadev.studyolle.account;

import com.mocadev.studyolle.ConsoleMailSender;
import com.mocadev.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-02-26
 **/
@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final ConsoleMailSender javaMailSender;

	private Account saveNewAccount(SignUpForm signUpForm) {
		final Account account = Account.builder()
			.email(signUpForm.getEmail())
			.nickname(signUpForm.getNickname())
			.password(signUpForm.getPassword())
			.studyCreatedByWeb(true)
			.studyEnrollmentResultByWeb(true)
			.studyUpdatedResultByWeb(true)
			.build();

		return accountRepository.save(account);
	}

	private void sendSignUpConfirmEmail(Account newAccount) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(newAccount.getEmail());
		mailMessage.setSubject("스터디할래, 회원 가입 인증");
		mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
		javaMailSender.send(mailMessage);
	}

	public void processNewAccount(SignUpForm signUpForm) {
		final Account newAccount = saveNewAccount(signUpForm);
		newAccount.generateEmailCheckToken();
		sendSignUpConfirmEmail(newAccount);
	}
}
