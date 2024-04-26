package com.retail.e_com.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.entity.RefreshToken;
import com.retail.e_com.entity.User;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {

	boolean existsByTokenAndIsBlocked(String rt, boolean b);

//	boolean findByToken(String refreshToken);

	Optional<RefreshToken> findByToken(String refreshtoken);

}
