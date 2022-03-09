package com.mocadev.studyolle.settings.validator;

import com.mocadev.studyolle.account.AccountRepository;
import com.mocadev.studyolle.domain.Account;
import com.mocadev.studyolle.settings.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-09
 **/
@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

	private final AccountRepository accountRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return NicknameForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		NicknameForm nicknameForm = (NicknameForm) target;
		final Account byNickname = accountRepository.findByNickname(nicknameForm.getNickname());
		if (byNickname != null) {
			errors.rejectValue("nickname", "wrong.value", "입력하신 닉네임은 사횽할 수 없습니다.");
		}
	}

}
