package com.shop.controllers.rest;

import com.shop.configs.AppConfig;
import com.shop.models.Person;
import com.shop.services.PersonService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = PersonController.PEOPLE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {
    public static final String PEOPLE_URL = "/people";
    public static final AnnotationConfigApplicationContext applicationContext =
            new AnnotationConfigApplicationContext(AppConfig.class);
    public static final PersonService personService =
            applicationContext.getBean(PersonService.class);

    @GetMapping
    public List<Person> findAllPeople() {
        return personService.getPeople();
    }

    @GetMapping("/{id}")
    public Person findByIdPerson(@PathVariable int id) {
        return personService.getPerson(id);
    }

    @PostMapping
    public Person savePerson(
            @RequestBody Person person
    ) {
        return personService.addPerson(person);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable int id) {
        personService.deletePerson(id);
    }
}
