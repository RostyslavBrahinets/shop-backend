package com.shop.controllers;

import com.shop.models.Role;
import com.shop.services.PersonRoleService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = PersonRoleController.PERSON_ROLE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonRoleController {
    public static final String PERSON_ROLE_URL = "/web-api/person-role";
    private final PersonRoleService personRoleService;

    public PersonRoleController(PersonRoleService personRoleService) {
        this.personRoleService = personRoleService;
    }

    @GetMapping("/{id}")
    public Role findRoleForPerson(
        @PathVariable long id
    ) {
        return personRoleService.getRole(id);
    }


    @PostMapping("/{id}")
    public void updateRoleForPerson(
        @PathVariable long id,
        @RequestBody String role
    ) {
        personRoleService.updateRoleForPerson(id, role);
    }
}
