package com.fri.code.users.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
@DeclareRoles({"user", "admin"})
@RegisterService
public class CodeUsersApplication extends Application {
}
