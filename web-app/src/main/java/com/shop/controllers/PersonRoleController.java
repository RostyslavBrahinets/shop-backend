package com.shop.controllers;

import com.shop.dto.RoleDto;
import com.shop.models.Role;
import com.shop.services.PersonRoleService;
import com.shop.services.RoleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PersonRoleController.PERSON_ROLE_URL)
public class PersonRoleController {
    public static final String PERSON_ROLE_URL = "/web-api/person-role";
    private final PersonRoleService personRoleService;
    private final RoleService roleService;

    public PersonRoleController(
        PersonRoleService personRoleService,
        RoleService roleService
    ) {
        this.personRoleService = personRoleService;
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    public Role findRoleForPerson(
        @PathVariable long id
    ) {
        return personRoleService.findRoleForPerson(id);
    }

    @PostMapping("/{id}")
    public String updateRoleForPerson(
        @PathVariable long id,
        @RequestBody RoleDto roleDto
    ) {
        Role role = roleService.findByName(roleDto.getRole());
        personRoleService.updateRoleForPerson(id, role.getId());
        return "Role Successfully Changed";
    }
}
