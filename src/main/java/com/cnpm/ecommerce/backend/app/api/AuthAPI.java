package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.CustomerDTO;
import com.cnpm.ecommerce.backend.app.dto.JwtResponse;
import com.cnpm.ecommerce.backend.app.dto.LoginDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.service.IUserService;
import com.cnpm.ecommerce.backend.app.utils.JwtUtils;
import com.cnpm.ecommerce.backend.app.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService customerService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateCustomer(@Validated @RequestBody LoginDTO loginDto){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl customerDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = customerDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if(roles.contains("ROLE_EMPLOYEE")){
            return new ResponseEntity<>(new MessageResponse("Account is denied to customer page", HttpStatus.UNAUTHORIZED,LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                customerDetails.getId(),
                customerDetails.getUsername(),
                customerDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerCustomer(@Validated @RequestBody CustomerDTO customerDto){

        if(customerService.existsByUserName(customerDto.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));

        }

        if(customerService.existsByEmail(customerDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));
        }

        customerService.createCustomer(customerDto);

        return ResponseEntity.ok(new MessageResponse("Customer registered successfully!", HttpStatus.OK, LocalDateTime.now() ));

    }

}
