package com.mocadev.studyolle.account;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-03-05
 **/
@Table(name = "persistent_logins")
@Entity
@Getter
@Setter
public class PersistentLogins {

	@Id
	@Column(length = 64)
	private String series;

	@Column(nullable = false, length = 64)
	private String username;

	@Column(nullable = false, length = 64)
	private String token;

	@Column(name = "last_used", nullable = false, length = 64)
	private LocalDateTime lastUsed;

}
