package com.hotel.SpringBootHotelbooking.repository;

import com.hotel.SpringBootHotelbooking.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {



    Optional<Token> findByToken(String Token);

    @Query ("""
         select t from Token t inner join User u  on t.user.id = u.id 
                  where  t.user.id=:userId and t.logOut=false
            """)
    List<Token> findAllTokenByUser(Long userId);
}
