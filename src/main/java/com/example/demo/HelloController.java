package com.example.demo;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/status")
  public String checkStatus() {
    return "Hello from the world of Java";
  }

  @GetMapping("/health")
  public Map<String, String> getHealth() {
    return Map.of("status", "UP");
  }

  @GetMapping("/version")
  public Map<String, String> getVersion() {
    return Map.of("version", "1.0.0-SNAPSHOT");
  }
}
