package com.mocadev.studyolle.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String nickname;
	private String password;
	private boolean emailVerified;
	private String emailCheckToken;
	private LocalDateTime joinedAt;
	private String bio;
	private String url;
	private String occupation;
	private String liveAround;
	private String location;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	private String profileImage;
	private boolean studyCreatedByEmail;
	private boolean studyCreatedByWeb = true;
	private boolean studyEnrollmentResultByEmail;
	private boolean studyEnrollmentResultByWeb = true;
	private boolean studyUpdatedResultByEmail;
	private boolean studyUpdatedResultByWeb = true;
	private boolean studyUpdatedByEmail;
	private boolean studyUpdatedByWeb = true;
	private LocalDateTime emailCheckTokenGeneratedAt;

	public void generateEmailCheckToken() {
		this.emailCheckToken = UUID.randomUUID().toString();
		this.emailCheckTokenGeneratedAt = LocalDateTime.now();
	}

	public void completeSignUp() {
		this.emailVerified = true;
		this.joinedAt = LocalDateTime.now();
	}

	public boolean isValidToken(String token) {
		return this.emailCheckToken.equals(token);
	}

	public boolean canResendConfirmEmail() {
		return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
	}

	public boolean canSendConfirmEmail() {
		return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
	}
}
