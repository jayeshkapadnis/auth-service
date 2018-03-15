package com.hashmapinc.haf.mappers;

import com.hashmapinc.haf.models.SecurityUser;
import com.hashmapinc.haf.models.UserInformation;
import com.hashmapinc.haf.models.UserPrincipal;

public class DefaultUserDetailsMapper implements UserDetailsMapper{
    @Override
    public SecurityUser map(UserInformation details, UserPrincipal userPrincipal) {
        return new SecurityUser(details, true, userPrincipal);
    }
}
