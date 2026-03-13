package com.company.hrms.security;

import com.company.hrms.entity.Permission;
import com.company.hrms.entity.Role;

import java.util.Set;

public class RolePermissionMapper {

    public static Set<Permission> getPermissions(Role role) {

        if (role == Role.ROLE_ADMIN) {
            return Set.of(Permission.values());
        }

        if (role == Role.ROLE_MANAGER) {
            return Set.of(
                    Permission.EMPLOYEE_CREATE,
                    Permission.EMPLOYEE_VIEW,
                    Permission.PAYROLL_GENERATE,
                    Permission.LEAVE_APPROVE
            );
        }

        return Set.of(
                Permission.EMPLOYEE_VIEW,
                Permission.LEAVE_REQUEST
        );
    }
}
