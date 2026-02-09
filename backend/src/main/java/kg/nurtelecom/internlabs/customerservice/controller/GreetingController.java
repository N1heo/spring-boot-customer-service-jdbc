package kg.nurtelecom.internlabs.customerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GreetingController {

    @GetMapping
    public ResponseEntity<String> greeting() {
        return ResponseEntity.ok("Intern Labs Customer Service");
    }

    @GetMapping("/api/test/all")
    public ResponseEntity<String> allAccess() {
        return ResponseEntity.ok("Connected to customer service");
    }


}
