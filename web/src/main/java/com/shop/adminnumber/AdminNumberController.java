package com.shop.adminnumber;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(AdminNumberController.ADMIN_NUMBERS_URL)
public class AdminNumberController {
    public static final String ADMIN_NUMBERS_URL = "/api/v1/admins-numbers";
    private final AdminNumberService adminNumberService;

    public AdminNumberController(AdminNumberService adminNumberService) {
        this.adminNumberService = adminNumberService;
    }

    @GetMapping("")
    public List<AdminNumber> findAll() {
        return adminNumberService.findAll();
    }

    @GetMapping("/{id}")
    public AdminNumber findById(@PathVariable long id) {
        return adminNumberService.findById(id);
    }

    @PostMapping("")
    public AdminNumber save(@RequestBody AdminNumber adminNumber) {
        return adminNumberService.save(adminNumber);
    }

    @PutMapping("/{id}")
    public AdminNumber update(
        @PathVariable long id,
        @RequestBody AdminNumber adminNumber
    ) {
        return adminNumberService.update(id, adminNumber);
    }

    @DeleteMapping("/{number}")
    public void delete(@PathVariable String number) {
        adminNumberService.delete(AdminNumber.of(number));
    }
}
