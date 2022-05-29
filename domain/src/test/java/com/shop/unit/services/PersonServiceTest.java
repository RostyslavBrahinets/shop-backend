package com.shop.unit.services;

import com.shop.models.Person;
import com.shop.repositories.PersonRepository;
import com.shop.services.PersonService;
import com.shop.validators.ContactValidator;
import com.shop.validators.PersonValidator;
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

class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonValidator personValidator;
    @Mock
    private ContactValidator contactValidator;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        personService = new PersonService(
            personRepository,
            personValidator,
            contactValidator
        );
    }

    @Test
    @DisplayName("Person was saved for with correct input")
    void person_was_saved_with_correct_input() {
        when(personRepository.save("John", "Smith"))
            .thenReturn(Person.of("John", "Smith").withId(1));

        Person savedPerson = personService.save("John", "Smith");

        assertThat(savedPerson).isEqualTo(new Person(1, "John", "Smith"));
    }

    @Test
    @DisplayName("Empty list of people is returned in case when no people in storage")
    void empty_list_of_people_is_returned_in_case_when_no_people_in_storage() {
        when(personRepository.findAll()).thenReturn(emptyList());

        List<Person> people = personService.findAll();

        assertThat(people).isEmpty();
    }

    @Test
    @DisplayName("List of people is returned in case when people are exists in storage")
    void list_of_people_is_returned_in_case_when_people_are_exists_in_storage() {
        when(personRepository.findAll()).thenReturn(
            List.of(
                Person.of("John", "Smith").withId(1)
            )
        );

        List<Person> people = personService.findAll();

        assertThat(people).isEqualTo(List.of(new Person(1, "John", "Smith")));
    }

    @Test
    @DisplayName("Person was found by id")
    void person_was_found_by_id() {
        when(personRepository.findById(1)).thenReturn(
            Optional.of(Person.of("John", "Smith").withId(1))
        );

        Person person = personService.findById(1);

        assertThat(person).isEqualTo(new Person(1, "John", "Smith"));
    }

    @Test
    @DisplayName("Person was found by email")
    void person_was_found_by_email() {
        when(personRepository.findByEmail("test@gmail.com")).thenReturn(
            Optional.of(Person.of("John", "Smith").withId(1))
        );

        Person person = personService.findByEmail("test@gmail.com");

        assertThat(person).isEqualTo(new Person(1, "John", "Smith"));
    }

    @Test
    @DisplayName("Person was deleted")
    void person_was_deleted() {
        personService.delete(1);
        verify(personRepository).delete(1);
    }
}
