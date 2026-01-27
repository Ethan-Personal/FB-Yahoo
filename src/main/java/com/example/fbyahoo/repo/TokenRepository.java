package com.example.fbyahoo.repo;

import com.example.fbyahoo.model.OAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<OAuthToken, Long> {

    Optional<OAuthToken> findTopByOrderByIdAsc();

    void deleteAll();

}
