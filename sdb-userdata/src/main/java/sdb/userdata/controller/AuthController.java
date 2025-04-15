package sdb.userdata.controller;

import jaxb.userdata.LoginRequest;
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
  public UserResponse login(@RequestPayload LoginRequest request) {
    System.out.println(1);
    UserResponse response = new UserResponse();
    response.setId(1);
    response.setEmail("email");
    response.setPhone("79206313951");
    response.setFirstName("name");
    response.setMiddleName("name");
    response.setLastName("name");
    response.setBirthDate("date");

    return response;
  }

}
