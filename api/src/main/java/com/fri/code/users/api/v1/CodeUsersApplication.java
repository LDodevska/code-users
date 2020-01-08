package com.fri.code.users.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;


import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "UsersApi", version = "v1.0.0",
        contact = @Contact()), servers = @Server(url = "http://35.223.203.195:8080/v1"), security
        = @SecurityRequirement(name = "openid-connect"))
@ApplicationPath("/v1")
@DeclareRoles({"user", "admin"})
@RegisterService
public class CodeUsersApplication extends Application {
}
