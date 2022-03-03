package com.youwent.modules.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// 이렇게 붙여줘야 내가 직접 만든 메서드를 호출했을 때에도 트랜잭션 처리가 된다.
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);

    Account findByEmail(String email);

    Optional<Account> findById(Long id);
}
