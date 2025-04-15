package sdb.userdata.controller;

import jaxb.userdata.AuthResponse;
import jaxb.userdata.LoginRequest;
import jaxb.userdata.RegisterRequest;
import jaxb.userdata.UserResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import sdb.userdata.config.AppConfig;

@Endpoint
public class AuthController {

  @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "LoginRequest")
  @ResponsePayload
  public AuthResponse login(@RequestPayload LoginRequest request) {
    AuthResponse response = new AuthResponse();
    response.setToken("token");

    return response;
  }

  @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "RegisterRequest")
  @ResponsePayload
  public UserResponse register(@RequestPayload RegisterRequest request) {
    UserResponse response = new UserResponse();
    response.setId(1);
    response.setEmail(request.getEmail());
    response.setPhone("79206313951");
    response.setFirstName("name");
    response.setMiddleName("name");
    response.setLastName("name");
    response.setBirthDate("date");

    return response;
  }

}
