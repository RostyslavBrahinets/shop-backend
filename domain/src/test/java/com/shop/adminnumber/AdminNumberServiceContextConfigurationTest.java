package com.shop.adminnumber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        AdminNumberService.class,
        AdminNumberServiceContextConfigurationTest.TestContextConfig.class
    }
)
public class AdminNumberServiceContextConfigurationTest {
    @Autowired
    private AdminNumberRepository adminNumberRepository;
    @Autowired
    private AdminNumberValidator adminNumberValidator;
    @Autowired
    private AdminNumberService adminNumberService;

    private List<AdminNumber> adminNumbers;

    @BeforeEach
    void setUp() {
        adminNumbers = List.of();
    }

    @Test
    @DisplayName("Get all numbers of admins")
    void get_all_numbers_of_admins() {
        adminNumberService.findAll();

        verify(adminNumberRepository, atLeast(1)).findAll();
    }

    @Test
    @DisplayName("Get number of admin by id")
    void get_number_of_admin_by_id() {
        long id = 1;

        adminNumberService.findById(id);

        verify(adminNumberValidator, atLeast(1)).validate(id, adminNumbers);
        verify(adminNumberRepository).findById(id);
    }

    @Test
    @DisplayName("Get number of admin by number")
    void get_number_of_admin_by_number() {
        String number = "12345678";

        adminNumberService.findByNumber(AdminNumber.of(number));

        verify(adminNumberValidator, atLeast(1)).validate(number, adminNumbers);
        verify(adminNumberRepository).findByNumber(number);
    }

    @Test
    @DisplayName("Save number of admin")
    void save_number_of_admin() {
        String number = "12345678";

        adminNumberService.save(AdminNumber.of(number));

        verify(adminNumberValidator, atLeast(1)).validateAdminNumber(number);
        verify(adminNumberRepository).save(AdminNumber.of(number));
    }

    @Test
    @DisplayName("Delete number of admin by number")
    void delete_number_of_admin_by_number() {
        String number = "12345678";

        adminNumberService.delete(AdminNumber.of(number));

        verify(adminNumberValidator, atLeast(1)).validate(number, adminNumbers);
        verify(adminNumberRepository).delete(AdminNumber.of(number));
    }

    @TestConfiguration
    static class TestContextConfig {
        @Bean
        public AdminNumber adminNumber() {
            return mock(AdminNumber.class);
        }

        @Bean
        public AdminNumberRepository adminNumberRepository() {
            return mock(AdminNumberRepository.class);
        }

        @Bean
        public AdminNumberValidator adminNumberValidator() {
            return mock(AdminNumberValidator.class);
        }
    }
}
