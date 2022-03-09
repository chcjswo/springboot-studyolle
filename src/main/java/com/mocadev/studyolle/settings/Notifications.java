package com.mocadev.studyolle.settings;

import lombok.Data;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-09
 **/
@Data
public class Notifications {

	private boolean studyCreatedByEmail;
	private boolean studyCreatedByWeb;
	private boolean studyEnrollmentResultByEmail;
	private boolean studyEnrollmentResultByWeb;
	private boolean studyUpdatedByEmail;
	private boolean studyUpdatedByWeb;
}
