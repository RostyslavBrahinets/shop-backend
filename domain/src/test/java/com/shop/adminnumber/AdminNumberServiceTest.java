package com.shop.adminnumber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.shop.adminnumber.AdminNumberParameter.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminNumberServiceTest {
    @Mock
    private AdminNumberRepository adminNumberRepository;
    @Mock
    private AdminNumberValidator adminNumberValidator;

    private AdminNumberService adminNumberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminNumberService = new AdminNumberService(
            adminNumberRepository,
            adminNumberValidator
        );
    }

    @Test
    @DisplayName("Empty list of number of admin is returned in case when no number of admin in storage")
    void empty_list_of_number_of_admin_is_returned_in_case_when_no_number_of_admin_in_storage() {
        when(adminNumberRepository.findAll()).thenReturn(emptyList());

        List<AdminNumber> adminNumbers = adminNumberService.findAll();

        assertThat(adminNumbers).isEmpty();
    }

    @Test
    @DisplayName("List of number of admin is returned in case when number of admin are exists in storage")
    void list_of_number_of_admin_is_returned_in_case_when_number_of_admin_are_exists_in_storage() {
        when(adminNumberRepository.findAll()).thenReturn(
            List.of(getAdminNumberWithId())
        );

        List<AdminNumber> adminNumbers = adminNumberService.findAll();

        assertThat(adminNumbers).isEqualTo(List.of(getAdminNumberWithId()));
    }

    @Test
    @DisplayName("Number of admin was found by id")
    void number_of_admin_was_found_by_id() {
        when(adminNumberRepository.findById(getAdminNumberId())).thenReturn(
            Optional.of(getAdminNumberWithId())
        );

        AdminNumber adminNumber = adminNumberService.findById(getAdminNumberId());

        assertThat(adminNumber).isEqualTo(getAdminNumberWithId());
    }

    @Test
    @DisplayName("Number of admin was saved for with correct input")
    void number_of_admin_was_saved_with_correct_input() {
        AdminNumber savedAdminNumber = adminNumberService.save(getAdminNumberWithoutId());

        verify(adminNumberRepository).save(getAdminNumberWithoutId());

        assertThat(savedAdminNumber).isEqualTo(getAdminNumberWithId());
    }

    @Test
    @DisplayName("Number of admin was deleted")
    void number_of_admin_was_deleted() {
        adminNumberService.delete(getAdminNumberWithoutId());

        verify(adminNumberRepository).delete(getAdminNumberWithoutId());
    }

    @Test
    @DisplayName("Number of admin was found by user")
    void number_of_admin_was_found_by_user() {
        when(adminNumberRepository.findByNumber(getNumber()))
            .thenReturn(
                Optional.of(getAdminNumberWithId())
            );

        AdminNumber adminNumber = adminNumberService.findByNumber(getNumber());

        assertThat(adminNumber).isEqualTo(getAdminNumberWithId());
    }
}
