package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.*;
import com.cnpm.ecommerce.backend.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService extends UserDetailsService {

    List<EmployeeDTO> findAllEmployee();

    EmployeeDTO findByIdEmployeeDto(Long theId);


    MessageResponse createEmployee(EmployeeDTO theEmployee);

    MessageResponse updateEmployee(Long theId, EmployeeDTO theEmployee);

    void deleteEmployee(Long theId);

    Boolean existsByEmail(String email);

    Boolean existsByUserName(String username);

    User findByIdEmployee(Long employeeId);

    Page<User> findAllPageAndSortEmployee(Pageable pagingSort);

    Page<User> findByUserNameContainingEmployee(String userName, Pageable pagingSort);

    Long countEmployee();

    List<CustomerDTO> findAllCustomer();

    CustomerDTO findByIdCustomerDto(Long theId);


    MessageResponse createCustomer(CustomerDTO theCustomer);

    MessageResponse updateCustomer(Long theId, CustomerDTO theCustomer);

    void deleteCustomer(Long theId);

    Page<User> findAllPageAndSortCustomer(Pageable pagingSort);

    Page<User> findByUserNameContainingCustomer(String userName, Pageable pagingSort);

    Long countCustomer();

    User findByIdCustomer(Long customerId);

    MessageResponse resetPassword(PasswordResetRequest request, String getSiteURL) throws MessagingException, UnsupportedEncodingException;

    MessageResponse changeResetPassword(PasswordResetChangeRequest request);

    MessageResponse changePassword(PasswordChangeRequest request);
}
