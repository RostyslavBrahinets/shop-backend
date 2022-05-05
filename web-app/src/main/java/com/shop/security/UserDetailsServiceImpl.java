package com.shop.security;

import com.shop.exceptions.NotFoundException;
import com.shop.models.Contact;
import com.shop.models.Person;
import com.shop.models.Role;
import com.shop.services.ContactService;
import com.shop.services.PersonService;
import com.shop.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final PersonService personService;
    private final ContactService contactService;
    private final RoleService roleService;

    public UserDetailsServiceImpl(
        PersonService personService,
        ContactService contactService,
        RoleService roleService
    ) {
        this.personService = personService;
        this.contactService = contactService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Contact contact = null;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        try {
            Person person = personService.getPersonByEmail(email);
            long id = person.getId();
            Role role = roleService.getRole(id);
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            contact = contactService.getContact(id);
        } catch (NotFoundException e) {
            logger.error(e.getMessage(), e);
        }

        return new User(
            Objects.requireNonNull(contact).getEmail(),
            contact.getPassword(),
            grantedAuthorities
        );
    }
}
