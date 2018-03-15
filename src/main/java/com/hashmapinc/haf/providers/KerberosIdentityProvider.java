package com.hashmapinc.haf.providers;

import com.hashmapinc.haf.mappers.UserDetailsMapper;
import com.hashmapinc.haf.models.SecurityUser;
import com.hashmapinc.haf.models.UserInformation;
import com.hashmapinc.haf.models.UserPrincipal;
import com.hashmapinc.haf.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.kerberos.authentication.KerberosClient;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
@ConditionalOnProperty(value = "security.provider", havingValue = "kerberos")
public class KerberosIdentityProvider extends CustomAuthenticationProvider{

    private KerberosClient kerberosClient;

    @Autowired private UserDetailsService userDetailsService;

    @Autowired
    UserDetailsMapper mapper;

    @PostConstruct
    public void init(){
        SunJaasKerberosClient client = new SunJaasKerberosClient();
        client.setDebug(true);
        this.kerberosClient = client;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserPrincipal)) {
            throw new BadCredentialsException("Authentication Failed. Bad user principal.");
        }
        UserPrincipal userPrincipal =  (UserPrincipal) principal;
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        String validatedUsername = kerberosClient.login(auth.getName(), auth.getCredentials().toString());
        UserInformation userDetails = this.userDetailsService.loadUserByUsername(validatedUsername);
        SecurityUser securityUser = mapper.map(userDetails, userPrincipal);
        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

}
