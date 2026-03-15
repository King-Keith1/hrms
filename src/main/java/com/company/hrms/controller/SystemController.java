package com.company.hrms.controller;

import com.company.hrms.service.SystemClockService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system")
public class SystemController {

    private final SystemClockService clockService;

    public SystemController(SystemClockService clockService) {
        this.clockService = clockService;
    }

    @PostMapping("/next-day")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MANAGER')")
    public String advanceDay() {
        clockService.advanceDay();
        return "System date advanced to next day";
    }
}