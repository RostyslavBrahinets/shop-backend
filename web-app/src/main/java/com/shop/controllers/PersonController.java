package com.shop.controllers;

import com.shop.dto.PersonDto;
import com.shop.models.Person;
import com.shop.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PersonController.PEOPLE_URL)
public class PersonController {
    public static final String PEOPLE_URL = "/web-api/people";
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> findAllPeople() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public Person findByIdPerson(@PathVariable int id) {
        return personService.findById(id);
    }

    @PostMapping
    public Person savePerson(
        @RequestBody Person person
    ) {
        return personService.save(person.getFirstName(), person.getLastName());
    }

    @PostMapping("/{id}")
    public Person updatePerson(
        @PathVariable long id,
        @RequestBody PersonDto person
    ) {
        Person updatedPerson = new Person();
        updatedPerson.setFirstName(person.getFirstName());
        updatedPerson.setLastName(person.getLastName());
        return personService.update(id, updatedPerson.getFirstName(), updatedPerson.getLastName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable int id) {
        personService.delete(id);
    }
}
