package com.hq.ecmp.util;

import com.hq.api.system.domain.SysRole;
import com.hq.core.security.LoginUser;

import java.util.List;

public class HqAdmin {

    public static int isAdmin(LoginUser user){
        List<SysRole> roleList = user.getUser().getRoles();
        for(SysRole data : roleList ) {
            if("admin".equals(data.getRoleKey()) || "sub_admin".equals(data.getRoleKey())){
                return 1;
            }
        }
        return 0;
    }
}
