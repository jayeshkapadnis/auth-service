package com.hashmapinc.haf.jwt.extractors;

import com.hashmapinc.haf.configs.HafSecurityConfig;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component(value="jwtQueryTokenExtractor")
public class JwtQueryTokenExtractor implements TokenExtractor {

    @Override
    public String extract(HttpServletRequest request) {
        String token = null;
        if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
            String[] tokenParamValue = request.getParameterMap().get(HafSecurityConfig.JWT_TOKEN_QUERY_PARAM);
            if (tokenParamValue != null && tokenParamValue.length == 1) {
                token = tokenParamValue[0];
            }
        }
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationServiceException("Authorization query parameter cannot be blank!");
        }

        return token;
    }
}
