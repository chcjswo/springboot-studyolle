package com.mocadev.studyolle.settings;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-09
 **/
@Data
public class PasswordForm {

	@Length(min = 8, max = 50)
	private String newPassword;

	@Length(min = 8, max = 50)
	private String newPasswordConfirm;

}
