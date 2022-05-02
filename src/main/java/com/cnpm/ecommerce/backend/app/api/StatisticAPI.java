package com.cnpm.ecommerce.backend.app.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticAPI {

    @GetMapping("")
    public ResponseEntity<?> getAllRevenue(@RequestParam(name = "day", required = false) String day,
                                           @RequestParam(name = "month", required = false) String month,
                                           @RequestParam(name = "quarter", required = false) String quarter,
                                           @RequestParam(name = "year", required = false) String year) {



        return null;
    }
}
