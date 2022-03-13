package com.cnpm.ecommerce.backend.app.service;

import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.dto.RoleDTO;
import com.cnpm.ecommerce.backend.app.entity.Role;
import com.cnpm.ecommerce.backend.app.exception.ResourceNotFoundException;
import com.cnpm.ecommerce.backend.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService implements IRoleService{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long theId) throws ResourceNotFoundException {
        return roleRepository.findById(theId).orElseThrow(() -> new ResourceNotFoundException("Not found role with ID=" + theId));
    }

    @Override
    public Role findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public MessageResponse createRole(RoleDTO theRoleDto) {
        Role role = new Role();
        role.setCode(theRoleDto.getCode());
        role.setName(theRoleDto.getName());
        role.setCreatedDate(new Date());
        role.setCreatedBy("Hoa");

        roleRepository.save(role);
        return new MessageResponse("Create role successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateRole(Long theId, RoleDTO theRoleDto) {

        Optional<Role> theRole = roleRepository.findById(theId);
        if(!theRole.isPresent()) {
            throw new ResourceNotFoundException("Not found role with ID=" + theId);
        } else {
            theRole.get().setName(theRoleDto.getName());
            theRole.get().setCode(theRoleDto.getCode());
            theRole.get().setModifiedBy("");
            theRole.get().setModifiedDate(new Date());

            roleRepository.save(theRole.get());
        }

        return new MessageResponse("Update role successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public void deleteProduct(Long theId) {

    }
}
