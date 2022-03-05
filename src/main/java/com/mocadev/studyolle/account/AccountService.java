package com.mocadev.studyolle.account;

import com.mocadev.studyolle.domain.Account;
import com.mocadev.studyolle.mail.ConsoleMailSender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-02-26
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

	private final AccountRepository accountRepository;
	private final ConsoleMailSender javaMailSender;
	private final PasswordEncoder passwordEncoder;

	public Account processNewAccount(SignUpForm signUpForm) {
		final Account newAccount = saveNewAccount(signUpForm);
		newAccount.generateEmailCheckToken();
		sendSignUpConfirmEmail(newAccount);
		return newAccount;
	}

	private Account saveNewAccount(SignUpForm signUpForm) {
		final Account account = Account.builder()
			.email(signUpForm.getEmail())
			.nickname(signUpForm.getNickname())
			.password(passwordEncoder.encode(signUpForm.getPassword()))
			.studyCreatedByWeb(true)
			.studyEnrollmentResultByWeb(true)
			.studyUpdatedResultByWeb(true)
			.build();

		return accountRepository.save(account);
	}

	public void sendSignUpConfirmEmail(Account newAccount) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(newAccount.getEmail());
		mailMessage.setSubject("스터디올래, 회원 가입 인증");
		mailMessage.setText(
			"/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email="
				+ newAccount.getEmail());
		javaMailSender.send(mailMessage);
	}

	public void login(Account account) {
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
			new UserAccount(account),
			account.getPassword(),
			List.of(new SimpleGrantedAuthority("ROLE_USER")));
		SecurityContextHolder.getContext().setAuthentication(token);
	}


	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(emailOrNickname);
		if (account == null) {
			account = accountRepository.findByNickname(emailOrNickname);
		}
		if (account == null) {
			throw new UsernameNotFoundException(emailOrNickname);
		}
		return new UserAccount(account);
	}

	public void completeSignUp(Account account) {
		account.completeSignUp();
		login(account);
	}
}
