package com.mocadev.studyolle.account;

import com.mocadev.studyolle.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chcjswo
 * @version 1.0.0
 * @blog https://mocadev.tistory.com
 * @github https://github.com/chcjswo
 * @since 2022-02-25
 **/
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByEmail(String email);

	boolean existsByNickname(String email);

	Account findByEmail(String email);

	Account findByNickname(String nickname);
}
