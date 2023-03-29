package com.shop.admin;

import com.shop.adminnumber.AdminNumber;
import com.shop.adminnumber.AdminNumberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AdminNumberController.ADMIN_NUMBERS_URL)
public class AdminNumberController {
    public static final String ADMIN_NUMBERS_URL = "/api/admins-numbers";
    private final AdminNumberService adminNumberService;

    public AdminNumberController(AdminNumberService adminNumberService) {
        this.adminNumberService = adminNumberService;
    }

    @GetMapping
    public List<AdminNumber> findAll() {
        return adminNumberService.findAll();
    }

    @GetMapping("/{id}")
    public AdminNumber findById(@PathVariable long id) {
        return adminNumberService.findById(id);
    }

    @PostMapping
    public AdminNumber save(@RequestBody AdminNumber adminNumber) {
        return adminNumberService.save(adminNumber);
    }

    @PutMapping("/{id}")
    public AdminNumber update(
        @PathVariable long id,
        @RequestBody AdminNumber adminNumber
    ) {
        adminNumber.setId(id);
        return adminNumberService.update(adminNumber);
    }

    @DeleteMapping("/{number}")
    public String delete(@PathVariable String number) {
        adminNumberService.delete(AdminNumber.of(number));
        return "Admin Number Successfully Deleted";
    }
}
