package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.EmployeeDTO;
import com.cnpm.ecommerce.backend.app.dto.JwtResponse;
import com.cnpm.ecommerce.backend.app.dto.LoginDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.service.IUserService;
import com.cnpm.ecommerce.backend.app.utils.JwtUtils;
import com.cnpm.ecommerce.backend.app.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/auth")
@CrossOrigin
public class AuthAdminAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService employeeService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateEmployee(@Validated @RequestBody LoginDTO loginDto){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForEmployee(authentication);

        UserDetailsImpl employeeDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = employeeDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if(roles.contains("ROLE_CUSTOMER")){
            return new ResponseEntity<>(new MessageResponse("Account is denied to admin", HttpStatus.UNAUTHORIZED,LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new JwtResponse(jwt,
                employeeDetails.getId(),
                employeeDetails.getUsername(),
                employeeDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerEmployee(@Validated @RequestBody EmployeeDTO employeeDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new MessageResponse("Invalid value for create employee", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        if(employeeService.existsByUserName(employeeDto.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));

        }

        if(employeeService.existsByEmail(employeeDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already use.", HttpStatus.BAD_REQUEST, LocalDateTime.now()));

        }


        MessageResponse messageResponse =  employeeService.createEmployee(employeeDto);

        return  new ResponseEntity<>(messageResponse, messageResponse.getStatus());

    }

}
