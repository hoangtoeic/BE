package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.EmployeeDTO;
import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.User;
import com.cnpm.ecommerce.backend.app.service.IUserService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import com.cnpm.ecommerce.backend.app.validationgroups.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeAPI {

    @Autowired
    private IUserService employeeService;

    @GetMapping("")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<Page<User>> findAll(@RequestParam(name = "q", required = false) String userName,
                                              @RequestParam(name = "enabled", required = false) Integer enabled,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int limit,
                                              @RequestParam(defaultValue = "id,ASC") String[] sort){

        try {

            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<User> employeePage;

            if(userName == null && enabled == null) {
                employeePage = employeeService.findAllPageAndSortEmployee(pagingSort);
            } else {
                if(enabled == null){
                    employeePage = employeeService.findByUserNameContainingEmployee(userName, pagingSort);
                } else if (userName == null) {
                    employeePage = employeeService.findByEnabledEmployee(enabled, pagingSort);
                } else {
                    employeePage = employeeService.findByUserNameContainingAndEnabledEmployee(userName, enabled, pagingSort);
                }

            }

            return new ResponseEntity<>(employeePage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable("id") Long theId){

        EmployeeDTO theEmployee = employeeService.findByIdEmployeeDto(theId);
        return new ResponseEntity<>(theEmployee, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> createEmployee(@Valid @RequestBody EmployeeDTO theEmployeeDto, BindingResult theBindingResult){

        if(theBindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for create employee", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = employeeService.createEmployee(theEmployeeDto);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateEmployee(@PathVariable("id") Long theId,
                                                          @Validated(OnUpdate.class)  @RequestBody EmployeeDTO theEmployeeDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Invalid value for update employee", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
        }

        MessageResponse messageResponse = employeeService.updateEmployee(theId, theEmployeeDto);
        return new ResponseEntity<MessageResponse>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long theId){

        employeeService.deleteEmployee(theId);
        return new ResponseEntity<>(new MessageResponse("Delete successfully!", HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }


    @GetMapping("/count")
    public ResponseEntity<?> count(){
        return new ResponseEntity<>(employeeService.countEmployee(), HttpStatus.OK);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> activeEmployee(@RequestParam(name = "username", required = true) String userName){

        MessageResponse messageResponse = employeeService.activeEmployee(userName);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
