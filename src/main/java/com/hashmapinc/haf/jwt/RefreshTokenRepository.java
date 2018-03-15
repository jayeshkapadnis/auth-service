package com.hashmapinc.haf.jwt;

import com.hashmapinc.haf.jwt.factory.JwtTokenFactory;
import com.hashmapinc.haf.jwt.models.JwtToken;
import com.hashmapinc.haf.models.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenRepository {

    private final JwtTokenFactory tokenFactory;

    @Autowired
    public RefreshTokenRepository(final JwtTokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    public JwtToken requestRefreshToken(SecurityUser user) {
        return tokenFactory.createRefreshToken(user);
    }

}
