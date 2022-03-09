package com.mocadev.studyolle.account;

import com.mocadev.studyolle.domain.Account;
import com.mocadev.studyolle.mail.ConsoleMailSender;
import com.mocadev.studyolle.settings.Notifications;
import com.mocadev.studyolle.settings.Profile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

	public void updateProfile(Account account, Profile profile) {
		account.setUrl(profile.getUrl());
		account.setBio(profile.getBio());
		account.setOccupation(profile.getOccupation());
		account.setLocation(profile.getLocation());
		account.setProfileImage(profile.getProfileImage());
		accountRepository.save(account);
	}

	public void updatePassword(Account account, String newPassword) {
		account.setPassword(passwordEncoder.encode(newPassword));
		accountRepository.save(account);
	}

	public void updateNotifications(Account account, Notifications notifications) {
		account.setStudyCreatedByWeb(notifications.isStudyCreatedByWeb());
		account.setStudyCreatedByEmail(notifications.isStudyCreatedByEmail());
		account.setStudyUpdatedByWeb(notifications.isStudyUpdatedByWeb());
		account.setStudyUpdatedByEmail(notifications.isStudyUpdatedByEmail());
		account.setStudyEnrollmentResultByEmail(notifications.isStudyEnrollmentResultByEmail());
		account.setStudyEnrollmentResultByWeb(notifications.isStudyEnrollmentResultByWeb());
		accountRepository.save(account);
	}
}
