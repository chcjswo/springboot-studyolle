package com.mocadev.studyolle.settings;

import com.mocadev.studyolle.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-09
 **/
@Data
@NoArgsConstructor
public class Notifications {

	private boolean studyCreatedByEmail;
	private boolean studyCreatedByWeb;
	private boolean studyEnrollmentResultByEmail;
	private boolean studyEnrollmentResultByWeb;
	private boolean studyUpdatedByEmail;
	private boolean studyUpdatedByWeb;

	public Notifications(Account account) {
		this.studyCreatedByEmail = account.isStudyCreatedByEmail();
		this.studyCreatedByWeb = account.isStudyCreatedByWeb();
		this.studyEnrollmentResultByEmail = account.isStudyEnrollmentResultByEmail();
		this.studyEnrollmentResultByWeb = account.isStudyEnrollmentResultByWeb();
		this.studyUpdatedByEmail = account.isStudyUpdatedByEmail();
		this.studyUpdatedByWeb = account.isStudyUpdatedByWeb();
	}
}
