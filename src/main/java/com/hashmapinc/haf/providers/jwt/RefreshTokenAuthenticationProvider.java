package com.hashmapinc.haf.providers.jwt;

import com.hashmapinc.haf.jwt.factory.JwtTokenFactory;
import com.hashmapinc.haf.jwt.models.RawAccessJwtToken;
import com.hashmapinc.haf.jwt.models.RefreshAuthenticationToken;
import com.hashmapinc.haf.models.SecurityUser;
import com.hashmapinc.haf.models.UserInformation;
import com.hashmapinc.haf.models.UserPrincipal;
import com.hashmapinc.haf.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider{

    private final JwtTokenFactory tokenFactory;
    private final UserDetailsService userService;

    @Autowired
    public RefreshTokenAuthenticationProvider(final UserDetailsService userService, final JwtTokenFactory tokenFactory) {
        this.userService = userService;
        this.tokenFactory = tokenFactory;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        SecurityUser unsafeUser = tokenFactory.parseRefreshToken(rawAccessToken);
        UserPrincipal principal = unsafeUser.getUserPrincipal();
        SecurityUser securityUser;
        securityUser = authenticateByUserId(unsafeUser.getUser().getId());
        /*if (principal.getType() == UserPrincipal.Type.USER_NAME) {
            securityUser = authenticateByUserId(unsafeUser.getId());
        } else {
            securityUser = authenticateByPublicId(principal.getValue());
        }*/
        return new RefreshAuthenticationToken(securityUser);
    }

    private SecurityUser authenticateByUserId(String userId) {
        UserInformation user = userService.loadUserByUsername(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by refresh token");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("User is not active");
        }

        if (user.getAuthorities() == null) throw new InsufficientAuthenticationException("User has no authority assigned");

        UserPrincipal userPrincipal = new UserPrincipal(UserPrincipal.Type.USER_NAME, user.getUserName());

        return new SecurityUser(user, user.isEnabled(), userPrincipal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (RefreshAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
