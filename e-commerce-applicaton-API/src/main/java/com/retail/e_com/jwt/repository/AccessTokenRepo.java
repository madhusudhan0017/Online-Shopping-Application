package com.retail.e_com.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.entity.AccessToken;
import com.retail.e_com.entity.User;


public interface AccessTokenRepo extends JpaRepository<AccessToken, Integer>{

	boolean existsByTokenAndIsBlocked(String at, boolean b);

//	boolean findByToken(String accessToken);

	Optional<AccessToken> findByToken(String refreshtoken);

}
