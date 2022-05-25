package com.shop.security;

import com.shop.models.Contact;
import com.shop.models.Person;
import com.shop.models.Role;
import com.shop.services.ContactService;
import com.shop.services.PersonRoleService;
import com.shop.services.PersonService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PersonService personService;
    private final ContactService contactService;
    private final PersonRoleService personRoleService;

    public UserDetailsServiceImpl(
        PersonService personService,
        ContactService contactService,
        PersonRoleService personRoleService
    ) {
        this.personService = personService;
        this.contactService = contactService;
        this.personRoleService = personRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Person person = personService.findByEmail(email);
        long id = person.getId();
        Role role = personRoleService.findRoleByPerson(id);
        List<GrantedAuthority> grantedAuthorities = List.of(
            new SimpleGrantedAuthority(role.getName())
        );
        Contact contact = contactService.findById(id);

        return new User(
            Objects.requireNonNull(contact).getEmail(),
            contact.getPassword(),
            grantedAuthorities
        );
    }
}
