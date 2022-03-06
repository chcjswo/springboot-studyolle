package com.mocadev.studyolle;


import com.mocadev.studyolle.account.AccountService;
import com.mocadev.studyolle.account.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-06
 **/
@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

	private final AccountService accountService;

	@Override
	public SecurityContext createSecurityContext(WithAccount withAccount) {
		String nickname = withAccount.value();

		final SignUpForm signUpForm = new SignUpForm();
		signUpForm.setNickname(nickname);
		signUpForm.setEmail(nickname + "@test.com");
		signUpForm.setPassword("11111111");
		accountService.processNewAccount(signUpForm);

		final UserDetails userDetails = accountService.loadUserByUsername(nickname);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		final SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	}
}
