package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.*;
import com.cnpm.ecommerce.backend.app.entity.PasswordResetToken;
import com.cnpm.ecommerce.backend.app.entity.Role;
import com.cnpm.ecommerce.backend.app.entity.User;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.mapper.UserMapper;
import com.cnpm.ecommerce.backend.app.repository.PasswordResetTokenRepository;
import com.cnpm.ecommerce.backend.app.repository.RoleRepository;
import com.cnpm.ecommerce.backend.app.repository.UserRepository;
import com.cnpm.ecommerce.backend.app.utils.UserDetailsImpl;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Qualifier("passwordEncoder")
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${bezkoder.app.jwtResetExpirationMs}")
    private Long resetTokenDurationMs;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<EmployeeDTO> findAllEmployee() {
        List<User> theEmployees = userRepository.findAllEmployee();

        List<EmployeeDTO> theEmployeesDto = new ArrayList<>();

        for(User theEmployee :theEmployees) {
            if(theEmployee.getEnabled() == 1) {
                theEmployeesDto.add(UserMapper.mapperToDTO(theEmployee));
            }
        }

        return theEmployeesDto;
    }

    @Override
    public EmployeeDTO findByIdEmployeeDto(Long theId) {
        Optional<User> theEmployee = userRepository.findByIdEmployee(theId);

        if(!theEmployee.isPresent()) {
            throw new ResourceNotFoundException("Not found user with ID=" + theId);
        } else {
            if(theEmployee.get().getEnabled() == 1) {
                return UserMapper.mapperToDTO(theEmployee.get());
            }
        }

        return null;
    }



    @Override
    public MessageResponse createEmployee(EmployeeDTO theEmployeeDto) {

        Boolean existEmployee = userRepository.existsByUserName(theEmployeeDto.getUserName());

        if(existEmployee == true){
            return new MessageResponse("Exist username in database", HttpStatus.CONFLICT, LocalDateTime.now());
        } else {
            User theEmployee = new User();

            theEmployee.setCreatedDate(new Date());
            theEmployee.setUserName(theEmployeeDto.getUserName());
            theEmployee.setName(theEmployeeDto.getName());
            theEmployee.setPassword(passwordEncoder.encode(theEmployeeDto.getPassword()));
            theEmployee.setEmail(theEmployeeDto.getEmail());
            theEmployee.setPhoneNumber(theEmployeeDto.getPhoneNumber());
            theEmployee.setAddress(theEmployeeDto.getAddress());
            theEmployee.setGender(theEmployeeDto.getGender());
            if(theEmployeeDto.getProfilePicture() != null ) {
                theEmployee.setProfilePictureArr(Base64Utils.decodeFromString(theEmployeeDto.getProfilePicture()));
            } else {
                theEmployee.setProfilePictureArr(new byte[0]);
            }
            theEmployee.setEnabled(1);
            theEmployee.setAccCustomer(false);

            if(theEmployeeDto.getRoleCode() == null) {
                return new MessageResponse("Error create Employee!", HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
            }

            Set<Role> roles;
            if(theEmployeeDto.getRoleCode().equals("ROLE_ADMIN")) {
                roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_EMPLOYEE"),
                        roleRepository.findByCode("ROLE_ADMIN")));
            } else {
                roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_EMPLOYEE")));
            }
            theEmployee.setRoles(roles);


            userRepository.save(theEmployee);

            return new MessageResponse("Create employee successfully!", HttpStatus.CREATED, LocalDateTime.now());
        }
    }

    @Override
    public MessageResponse updateEmployee(Long theId, EmployeeDTO theEmployeeDto) {
        Optional<User> theEmployee = userRepository.findById(theId);

        if(!theEmployee.isPresent()){
            throw new ResourceNotFoundException("Not found employee with ID=" + theId);
        } else {
            theEmployee.get().setModifiedDate(new Date());
            theEmployee.get().setModifiedBy(theEmployee.get().getUserName());
            theEmployee.get().setName(theEmployeeDto.getName());
            theEmployee.get().setEmail(theEmployeeDto.getEmail());
            theEmployee.get().setPhoneNumber(theEmployeeDto.getPhoneNumber());
            theEmployee.get().setAddress(theEmployeeDto.getAddress());
            theEmployee.get().setGender(theEmployeeDto.getGender());
            if(theEmployeeDto.getProfilePicture() != null ) {
                theEmployee.get().setProfilePictureArr(Base64Utils.decodeFromString(theEmployeeDto.getProfilePicture()));
            }else {
                theEmployee.get().setProfilePictureArr(new byte[0]);
            }
            theEmployee.get().setAccCustomer(false);

            Set<Role> roles;
            if(theEmployeeDto.getRoleCode().equals("ROLE_ADMIN")) {
                roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_EMPLOYEE"),
                        roleRepository.findByCode("ROLE_ADMIN")));
            } else {
                roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_EMPLOYEE")));
            }
            theEmployee.get().setRoles(roles);

            userRepository.save(theEmployee.get());
        }

        return new MessageResponse("Updated employee successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteEmployee(Long theId) {
        Optional<User> theEmployee = userRepository.findById(theId);

        if(!theEmployee.isPresent()) {
            throw new ResourceNotFoundException("Not found Employee with ID=" + theId);
        } else {
            theEmployee.get().setEnabled(0);
            userRepository.save(theEmployee.get());
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUserName(String username) {
        return userRepository.existsByUserName(username);
    }


    @Override
    public Page<User> findAllPageAndSortEmployee(Pageable pagingSort) {
        Page<User> employeePage =  userRepository.findByIsAccCustomer(false, pagingSort);

        for(User employee : employeePage.getContent()) {
                employee.setProfilePicture(Base64Utils.encodeToString(employee.getProfilePictureArr()));
        }
        return  employeePage;
    }

    @Override
    public Page<User> findByUserNameContainingEmployee(String userName, Pageable pagingSort) {
        Page<User> employeePage =  userRepository.findByUserNameContainingAndIsAccCustomer(userName,false, pagingSort);

        for(User employee : employeePage.getContent()) {
                employee.setProfilePicture(Base64Utils.encodeToString(employee.getProfilePictureArr()));
        }
        return  employeePage;
    }

    @Override
    public Long countEmployee() {
        return userRepository.countEmployee();
    }

    @Override
    public List<CustomerDTO> findAllCustomer() {
        List<User> theCustomers = userRepository.findAllCustomer();

        List<CustomerDTO> theCustomersDto = new ArrayList<>();

        for(User theCustomer :theCustomers) {
            if(theCustomer.getEnabled() == 1) {
                theCustomersDto.add(UserMapper.mapperToCustomerDTO(theCustomer));
            }
        }

        return theCustomersDto;
    }

    @Override
    public CustomerDTO findByIdCustomerDto(Long theId) {
        Optional<User> theCustomer = userRepository.findByIdCustomer(theId);

        if(!theCustomer.isPresent()) {
            throw new ResourceNotFoundException("Not found user with ID=" + theId);
        } else {
            if(theCustomer.get().getEnabled() == 1) {
                return UserMapper.mapperToCustomerDTO(theCustomer.get());
            }
        }

        return null;
    }

    @Override
    public MessageResponse createCustomer(CustomerDTO theCustomerDto) {
        Boolean existCustomer = userRepository.existsByUserName(theCustomerDto.getUserName());

        if(existCustomer == true){
            return new MessageResponse("Exist username in database", HttpStatus.CONFLICT, LocalDateTime.now());
        } else {
            User theCustomer = new User();

            theCustomer.setCreatedDate(new Date());
            theCustomer.setUserName(theCustomerDto.getUserName());
            theCustomer.setName(theCustomerDto.getName());
            theCustomer.setPassword(passwordEncoder.encode(theCustomerDto.getPassword()));
            theCustomer.setEmail(theCustomerDto.getEmail());
            theCustomer.setPhoneNumber(theCustomerDto.getPhoneNumber());
            theCustomer.setAddress(theCustomerDto.getAddress());
            theCustomer.setGender(theCustomerDto.getGender());
            if(theCustomerDto.getProfilePicture() != null ) {
                theCustomer.setProfilePictureArr(Base64Utils.decodeFromString(theCustomerDto.getProfilePicture()));
            } else {
                theCustomer.setProfilePictureArr(new byte[0]);
            }
            theCustomer.setEnabled(1);
            theCustomer.setAccCustomer(true);
            Set<Role> roles = new HashSet<>(Arrays.asList(roleRepository.findByCode("ROLE_CUSTOMER")));
            theCustomer.setRoles(roles);

            userRepository.save(theCustomer);

            return new MessageResponse("Create customer successfully!", HttpStatus.CREATED, LocalDateTime.now());
        }

    }

    @Override
    public MessageResponse updateCustomer(Long theId, CustomerDTO theCustomerDto) {
        Optional<User> theCustomer = userRepository.findByIdCustomer(theId);

        if(!theCustomer.isPresent()){
            throw new ResourceNotFoundException("Not found customer with ID=" + theId);
        } else {
            theCustomer.get().setModifiedDate(new Date());
            theCustomer.get().setModifiedBy(theCustomer.get().getUserName());
            theCustomer.get().setName(theCustomerDto.getName());
            theCustomer.get().setEmail(theCustomerDto.getEmail());
            theCustomer.get().setPhoneNumber(theCustomerDto.getPhoneNumber());
            theCustomer.get().setAddress(theCustomerDto.getAddress());
            theCustomer.get().setGender(theCustomerDto.getGender());
            if(theCustomerDto.getProfilePicture() != null) {
                theCustomer.get().setProfilePictureArr(Base64Utils.decodeFromString(theCustomerDto.getProfilePicture()));
            }
            else {
                theCustomer.get().setProfilePictureArr(new byte[0]);
            }
            theCustomer.get().setAccCustomer(true);

            userRepository.save(theCustomer.get());
        }

        return new MessageResponse("Updated customer successfully!", HttpStatus.OK, LocalDateTime.now());
    }

    @Override
    public void deleteCustomer(Long theId) {
        Optional<User> theCustomer = userRepository.findByIdCustomer(theId);

        if(!theCustomer.isPresent()){
            throw new ResourceNotFoundException("Not found customer with ID=" + theId);
        } else {
            theCustomer.get().setEnabled(0);
            userRepository.save(theCustomer.get());
        }
    }

    @Override
    public Page<User> findAllPageAndSortCustomer(Pageable pagingSort) {
        Page<User> employeePage =  userRepository.findByIsAccCustomer(true,pagingSort);

        for(User employee : employeePage.getContent()) {
                employee.setProfilePicture(Base64Utils.encodeToString(employee.getProfilePictureArr()));
        }
        return  employeePage;
    }

    @Override
    public Page<User> findByUserNameContainingCustomer(String userName, Pageable pagingSort) {
        Page<User> employeePage =  userRepository.findByUserNameContainingAndIsAccCustomer(userName, true, pagingSort);

        for(User employee : employeePage.getContent()) {
                employee.setProfilePicture(Base64Utils.encodeToString(employee.getProfilePictureArr()));
        }
        return  employeePage;
    }

    @Override
    public Long countCustomer() {
        return userRepository.countCustomer();
    }

    @Override
    public User findByIdCustomer(Long customerId) {
        Optional<User> theCustomer = userRepository.findByIdCustomer(customerId);

        if(!theCustomer.isPresent()) {
            throw new ResourceNotFoundException("Not found user with ID=" + customerId);
        } else {
            if(theCustomer.get().getEnabled() == 1) {
                return theCustomer.get();
            }
        }

        return null;
    }

    @Override
    public User findByEmailCustomer(String customerEmail) {

        Optional<User> theCustomer = userRepository.findByEmailCustomer(customerEmail);

        if(!theCustomer.isPresent()) {
            throw new ResourceNotFoundException("Not found user with email=" + customerEmail);
        } else {
            if(theCustomer.get().getEnabled() == 1) {
                return theCustomer.get();
            }
        }

        return null;
    }

    @Override
    public User findByUsernameCustomer(String username) {
        Optional<User> theCustomer = userRepository.findByUsernameCustomer(username);

        if(!theCustomer.isPresent()) {
            throw new ResourceNotFoundException("Not found user with username=" + username);
        } else {
            if(theCustomer.get().getEnabled() == 1) {
                return theCustomer.get();
            }
        }

        return null;
    }

    @Override
    public MessageResponse resetPassword(PasswordResetRequest request, String siteURL) throws MessagingException, UnsupportedEncodingException{
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()){
            String randomCode = RandomString.make(64);

            sendResetPasswordEmail(user.get(), siteURL, randomCode);

            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(randomCode);
            passwordResetToken.setUser(user.get());
            passwordResetToken.setExpiryDate(Instant.now().plusMillis(resetTokenDurationMs));

            passwordResetTokenRepository.save(passwordResetToken);

            return new MessageResponse("Please check mail to reset password.", HttpStatus.OK, LocalDateTime.now());
        } else {
            return new MessageResponse("Email not in database", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
    }

    @Override
    public MessageResponse changeResetPassword(PasswordResetChangeRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()){

            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.get().getId());

            if(passwordResetToken != null) {
                passwordResetTokenRepository.delete(passwordResetToken);

                String encodedPassword = passwordEncoder.encode(request.getPassword());
                user.get().setPassword(encodedPassword);
                user.get().setModifiedDate(new Date());

                userRepository.save(user.get());
                return new MessageResponse("Reset password successfully!", HttpStatus.OK, LocalDateTime.now());
            } else {
                return new MessageResponse("Password has been reset.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }


        } else {
            return new MessageResponse("Email not in database", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
    }

    @Override
    public MessageResponse changePassword(PasswordChangeRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()){

            if(passwordEncoder.matches(request.getOldPassword(), user.get().getPassword())){
                String encodedPassword = passwordEncoder.encode(request.getPassword());
                user.get().setPassword(encodedPassword);
                user.get().setModifiedDate(new Date());

                userRepository.save(user.get());

                return new MessageResponse("Update password successfully!", HttpStatus.OK, LocalDateTime.now());
            } else {
                return new MessageResponse("The old password is incorrect!", HttpStatus.BAD_REQUEST, LocalDateTime.now());
            }

        } else {
            return new MessageResponse("Email not in database", HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
    }

    private void sendResetPasswordEmail(User user, String siteURL, String randomCode) throws MessagingException, UnsupportedEncodingException{
        String toAddress = user.getEmail();
        String fromAddress = "cnpmt12022@gmail.com";
        String senderName = "ABC Store";
        String subject = "Please confirm to reset password";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to reset your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">RESET</a></h3>"
                + "Thank you,<br>"
                + "ABC Store.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName());
        String verifyURL = siteURL + "/api/password/confirm?token=" + randomCode;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    @Override
    public User findByIdEmployee(Long employeeId) {
        Optional<User> theEmployee = userRepository.findById(employeeId);

        if(!theEmployee.isPresent()) {
            throw new ResourceNotFoundException("Not found user with ID=" + employeeId);
        } else {
            if(theEmployee.get().getEnabled() == 1) {
                return theEmployee.get();
            }
        }

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);

        if ( (!user.isPresent()) || user.get().getEnabled() == 0) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }

        return UserDetailsImpl.build(user.get());
    }
}
