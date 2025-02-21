package com.example.demo.controller;

import com.example.demo.model.Log;
import com.example.demo.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

  @Autowired
  LogService logger;

  @PostMapping("/log")
  public void makeLog(
      @RequestBody Log logEvent) {
    logger.log(logEvent);
  }
}
