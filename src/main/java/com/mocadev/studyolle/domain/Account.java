package com.mocadev.studyolle.domain;

import java.time.LocalDateTime;
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

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String nickname;

	private String password;

	private String emailVerified;

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

	private boolean studyCreatedByWeb;

	private boolean studyEnrollmentResultByWeb;

	private boolean studyEnrollmentResultByEmail;

	private boolean studyUpdatedResultByWeb;

	private boolean studyUpdatedResultByEmail;
}
