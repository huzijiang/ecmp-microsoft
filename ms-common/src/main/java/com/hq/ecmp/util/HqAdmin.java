package com.hq.ecmp.util;

import com.hq.api.system.domain.SysRole;
import com.hq.core.security.LoginUser;

import java.util.List;

public class HqAdmin {

    public static int isAdmin(LoginUser user) {
        List<SysRole> roleList = user.getUser().getRoles();
        for (SysRole data : roleList) {
            if ("admin".equals(data.getRoleKey()) || "sub_admin".equals(data.getRoleKey())) {
                return 1;
            }
        }
        return 0;
    }

    public static int isAdminOrCustomerService(LoginUser user) {
        List<SysRole> roleList = user.getUser().getRoles();
        for (SysRole data : roleList) {
            if ("admin".equals(data.getRoleKey()) || "sub_admin".equals(data.getRoleKey())) {
                return 1;
            }
            if ("customer_service".equals(data.getRoleKey()) ||
                    (!data.getRoleKey().isEmpty() && data.getRoleKey().contains("customer_service"))) {
                return 1;
            }
        }
        return 0;
    }
}
