package com.shop.adminnumber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

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
    @DisplayName("Number of admin was saved for with correct input")
    void number_of_admin_was_saved_with_correct_input() {
        AdminNumber savedAdminNumber = adminNumberService.save(AdminNumber.of("12345678"));

        verify(adminNumberRepository).save("12345678");

        assertThat(savedAdminNumber).isEqualTo(new AdminNumber(1, "12345678"));
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
            List.of(
                AdminNumber.of("12345678").withId(1)
            )
        );

        List<AdminNumber> adminNumbers = adminNumberService.findAll();

        assertThat(adminNumbers).isEqualTo(List.of(new AdminNumber(1, "12345678")));
    }

    @Test
    @DisplayName("Number of admin was found by id")
    void number_of_admin_was_found_by_id() {
        when(adminNumberRepository.findById(1)).thenReturn(
            Optional.of(AdminNumber.of("12345678").withId(1))
        );

        AdminNumber adminNumber = adminNumberService.findById(1);

        assertThat(adminNumber).isEqualTo(new AdminNumber(1, "12345678"));
    }

    @Test
    @DisplayName("Number of admin was found by user")
    void number_of_admin_was_found_by_user() {
        when(adminNumberRepository.findByNumber("12345678")).thenReturn(
            Optional.of(AdminNumber.of("12345678").withId(1))
        );

        AdminNumber adminNumber = adminNumberService.findByNumber(AdminNumber.of("12345678"));

        assertThat(adminNumber).isEqualTo(new AdminNumber(1, "12345678"));
    }

    @Test
    @DisplayName("Number of admin was deleted")
    void number_of_admin_was_deleted() {
        adminNumberService.delete(AdminNumber.of("12345678"));
        verify(adminNumberRepository).delete("12345678");
    }
}
