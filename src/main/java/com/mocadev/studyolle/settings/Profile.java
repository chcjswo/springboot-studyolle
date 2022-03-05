package com.mocadev.studyolle.settings;

import com.mocadev.studyolle.domain.Account;
import lombok.Data;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-06
 **/
@Data
public class Profile {

	private String bio;
	private String url;
	private String occupation;
	private String location;

	public Profile(Account account) {
		this.bio = account.getBio();
		this.url = account.getUrl();
		this.occupation = account.getOccupation();
		this.location = account.getLocation();
	}
}
