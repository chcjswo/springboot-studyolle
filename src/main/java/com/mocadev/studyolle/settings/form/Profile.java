package com.mocadev.studyolle.settings.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-06
 **/
@Data
public class Profile {

	@Length(max = 20)
	private String bio;
	@Length(max = 50)
	private String url;
	@Length(max = 50)
	private String occupation;
	@Length(max = 50)
	private String location;
	private String profileImage;
}
