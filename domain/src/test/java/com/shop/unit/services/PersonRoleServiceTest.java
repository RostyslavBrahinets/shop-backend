package com.shop.unit.services;

import com.shop.repositories.PersonRoleRepository;
import com.shop.services.PersonRoleService;
import com.shop.services.PersonService;
import com.shop.validators.PersonValidator;
import com.shop.validators.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class PersonRoleServiceTest {
    @Mock
    private PersonRoleRepository personRoleRepository;
    @Mock
    private PersonService personService;
    @Mock
    private PersonValidator personValidator;
    @Mock
    private RoleValidator roleValidator;

    private PersonRoleService personRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        personRoleService = new PersonRoleService(
            personRoleRepository,
            personService,
            personValidator,
            roleValidator
        );
    }

    @Test
    @DisplayName("Role for person was saved")
    void role_for_person_was_saved() {
        personRoleService.saveRoleForPerson(1, 1);
        verify(personRoleRepository).saveRoleForPerson(1, 1);
    }
}
