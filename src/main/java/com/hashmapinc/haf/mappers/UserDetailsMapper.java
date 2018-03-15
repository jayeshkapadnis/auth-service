package com.hashmapinc.haf.mappers;

import com.hashmapinc.haf.models.SecurityUser;
import com.hashmapinc.haf.models.UserInformation;
import com.hashmapinc.haf.models.UserPrincipal;

public interface UserDetailsMapper {

    SecurityUser map(UserInformation details, UserPrincipal userPrincipal);
}
