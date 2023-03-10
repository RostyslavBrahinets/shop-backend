package com.shop.validators;

import com.shop.exceptions.NotFoundException;
import com.shop.exceptions.ValidationException;
import com.shop.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserValidator {
    public void validate(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        List<User> users
    ) {
        if (firstName == null || firstName.isBlank()) {
            throw new ValidationException("First name is invalid");
        } else if (lastName == null || lastName.isBlank()) {
            throw new ValidationException("Last name is invalid");
        } else if (isInValidEmail(email)) {
            throw new ValidationException("E-mail is invalid");
        } else if (isEmailAlreadyInUse(email, users)) {
            throw new ValidationException("E-mail is already in use");
        } else if (isInvalidPhone(phone)) {
            throw new ValidationException("Phone is invalid");
        } else if (isPhoneAlreadyInUse(phone, users)) {
            throw new ValidationException("Phone is already in use");
        } else if (password == null || password.isBlank()) {
            throw new ValidationException("Password is invalid");
        }
    }

    public void validate(long id, List<User> users) {
        List<Long> ids = new ArrayList<>();

        for (User user : users) {
            ids.add(user.getId());
        }

        if (!ids.contains(id)) {
            throw new NotFoundException("User not found");
        }
    }

    public void validateFullName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new ValidationException("First name is invalid");
        } else if (lastName == null || lastName.isBlank()) {
            throw new ValidationException("Last name is invalid");
        }
    }

    public void validateEmail(String email) {
        if (email == null || !email.equals("admin")) {
            if (isInValidEmail(email)) {
                throw new ValidationException("E-mail is invalid");
            }
        }
    }

    public void validatePhone(String phone, User user, List<User> users) {
        if (isInvalidPhone(phone)) {
            throw new ValidationException("Phone is invalid");
        } else if (isPhoneAlreadyInUse(phone, users) && !phone.equals(user.getPhone())) {
            throw new ValidationException("Phone is already in use");
        }
    }

    private boolean isInValidEmail(String email) {
        return email == null
            || email.isBlank()
            || email.startsWith("@")
            || !email.contains("@")
            || !email.endsWith(".com")
            || email.endsWith("@.com");
    }

    private boolean isEmailAlreadyInUse(String email, List<User> users) {
        List<String> emails = new ArrayList<>();

        for (User user : users) {
            emails.add(user.getEmail());
        }

        return emails.contains(email);
    }

    private boolean isInvalidPhone(String phone) {
        return phone == null
            || phone.isBlank()
            || !phone.startsWith("+")
            || phone.length() < 12;
    }

    private boolean isPhoneAlreadyInUse(String phone, List<User> users) {
        List<String> phones = new ArrayList<>();

        for (User user : users) {
            phones.add(user.getPhone());
        }

        return phones.contains(phone);
    }
}
