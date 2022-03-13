package com.cnpm.ecommerce.backend.app.mapper;

import com.cnpm.ecommerce.backend.app.dto.CustomerDTO;
import com.cnpm.ecommerce.backend.app.dto.EmployeeDTO;
import com.cnpm.ecommerce.backend.app.entity.User;
import org.springframework.util.Base64Utils;

public class UserMapper {
    public static EmployeeDTO mapperToDTO(User theEmployee) {

        EmployeeDTO theEmployeeDto = new EmployeeDTO();

        theEmployeeDto.setId(theEmployee.getId());
        theEmployeeDto.setUserName(theEmployee.getUserName());
        theEmployeeDto.setName(theEmployee.getName());
        theEmployeeDto.setEmail(theEmployee.getEmail());
        theEmployeeDto.setPhoneNumber(theEmployee.getPhoneNumber());
        theEmployeeDto.setAddress(theEmployee.getAddress());
        theEmployeeDto.setGender(theEmployee.getGender());
        theEmployeeDto.setProfilePicture(Base64Utils.encodeToString(theEmployee.getProfilePictureArr()));
        theEmployeeDto.setEnabled(theEmployee.getEnabled());

        if(theEmployee.getRoles().size() == 2) {
            theEmployeeDto.setRoleCode("ROLE_ADMIN");
        } else {
            theEmployeeDto.setRoleCode("ROLE_EMPLOYEE");
        }

        return theEmployeeDto;
    }

    public static CustomerDTO mapperToCustomerDTO(User theCustomer){
        CustomerDTO theCustomerDto = new CustomerDTO();

        theCustomerDto.setId(theCustomer.getId());
        theCustomerDto.setUserName(theCustomer.getUserName());
        theCustomerDto.setName(theCustomer.getName());
        theCustomerDto.setEmail(theCustomer.getEmail());
        theCustomerDto.setPhoneNumber(theCustomer.getPhoneNumber());
        theCustomerDto.setAddress(theCustomer.getAddress());
        theCustomerDto.setGender(theCustomer.getGender());
        theCustomerDto.setProfilePicture(Base64Utils.encodeToString(theCustomer.getProfilePictureArr()));
        theCustomerDto.setEnabled(theCustomer.getEnabled());
        theCustomerDto.setRoleCode("ROLE_CUSTOMER");

        return theCustomerDto;
    }

}
