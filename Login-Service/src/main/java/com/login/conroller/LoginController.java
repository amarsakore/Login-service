package com.login.conroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.login.model.CustomerDetails;
import com.login.model.LoginClass;
import com.login.model.MyUserDetails;
import com.login.model.User;
import com.login.service.ICustomerService;
import com.login.service.UserService;
import com.login.util.JwtUtil;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

	@Autowired
	public AuthenticationManager authenticationManager;
	@Autowired
	public JwtUtil jwtUtil;
	@Autowired
	private UserService service;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ICustomerService customerService;

	@CrossOrigin(origins = "*")
	@PostMapping("/login")
	public String login(@RequestBody LoginClass loginClass) {
		System.out.println("IN LOGIN");
		SimpleMailMessage message = new SimpleMailMessage();
		if (authenticationManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(loginClass.getUsername(), loginClass.getPassword()))
				.isAuthenticated()) {
			User user = service.findByMobileNumber(loginClass.getUsername());
			
			  message.setFrom("axisbank.dummy.info@gmail.com");
			  message.setTo(user.getEmail()); String
			  msg="Dear Customer,\nWe hope this email finds you well. We are reaching out to inform you about a recent login activity on your Axis Bank account.\n If this login was authorized by you, kindly disregard this message.\r\n"
			  + "\r\n" +
			  "However, if you did not initiate this login. We strongly recommend taking immediate action to protect your account. Please Contact our Customer Support: +1245 987357 or visit bank manager immediately.\n "
			  + "\r\n"
			  +"Please note that Axis Bank will never request your personal or account information via email. If you receive any emails asking for sensitive information or account details, please report it to our Customer Support immediately.\r\n"
			  + "\r\n" +
			  "Thank you for your attention to this matter. We are committed to providing you with a secure banking experience and appreciate your cooperation in keeping your Axis Bank account safe.\r\n"
			  + "\r\n" + "Sincerely,\r\n" + "Axis Bank Customer Support";
			  message.setText(msg);
			  message.setSubject("Alert: Recent Login Activity on Your Axis Bank Account."
			  ); //mailSender.send(message);
			 
			return jwtUtil.generateToken(loginClass.getUsername());
		} else {
			return "Invalid Credentials";
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody CustomerDetails customerDetails) {
		customerDetails.setAccountType("DIGITAL");
		return new ResponseEntity<CustomerDetails>(customerService.saveCustomer(customerDetails), HttpStatus.OK);
	}

}
