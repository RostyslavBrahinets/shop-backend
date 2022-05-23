package com.shop.dto;

import org.springframework.stereotype.Component;

@Component
public class ContactDto {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
