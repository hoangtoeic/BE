package com.cnpm.ecommerce.backend.app.api;

import com.cnpm.ecommerce.backend.app.dto.MessageResponse;
import com.cnpm.ecommerce.backend.app.entity.Cart;
import com.cnpm.ecommerce.backend.app.service.IStatisticService;
import com.cnpm.ecommerce.backend.app.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticAPI {

    @Autowired
    private IStatisticService statisticService;

    @GetMapping("/revenue")
    public ResponseEntity<?> getAllRevenue(@RequestParam(name = "day", required = false) String day,
                                           @RequestParam(name = "month", required = false) String month,
                                           @RequestParam(name = "quarter", required = false) String quarter,
                                           @RequestParam(name = "year", required = false) String year) throws ParseException { 
    if(day != null) {
            return new ResponseEntity<>(statisticService.getAllRevenueByDay(day), HttpStatus.OK); 
    }
    if(month != null) {
        return new ResponseEntity<>(statisticService.getAllRevenueByMonth(month), HttpStatus.OK);
    }
    if(quarter != null) {
        return new ResponseEntity<>(statisticService.getAllRevenueByQuarter(quarter), HttpStatus.OK);
    }
    if(year != null) {
        return new ResponseEntity<>(statisticService.getAllRevenueByYear(year), HttpStatus.OK);
    }

    return new ResponseEntity<>(new MessageResponse("Please provide time to get revenue", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/product")
    public ResponseEntity<?> countAllSoldProduct(@RequestParam(name = "day", required = false) String day,
                                           @RequestParam(name = "month", required = false) String month,
                                           @RequestParam(name = "quarter", required = false) String quarter,
                                           @RequestParam(name = "year", required = false) String year) throws ParseException {
        if(day != null) {
            return new ResponseEntity<>(statisticService.getAllSoldProductByDay(day), HttpStatus.OK);
        }
        if(month != null) {
            return new ResponseEntity<>(statisticService.getAllSoldProductByMonth(month), HttpStatus.OK);
        }
        if(quarter != null) {
            return new ResponseEntity<>(statisticService.getAllSoldProductByQuarter(quarter), HttpStatus.OK);
        }
        if(year != null) {
            return new ResponseEntity<>(statisticService.getAllSoldProductByYear(year), HttpStatus.OK);
        }

        return new ResponseEntity<>(new MessageResponse("Please provide time to get sold product", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/order")
    public ResponseEntity<?> getAllOrderBy(@RequestParam(name = "day", required = false) String day,
                                           @RequestParam(name = "month", required = false) String month,
                                           @RequestParam(name = "quarter", required = false) String quarter,
                                           @RequestParam(name = "year", required = false) String year,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int limit,
                                           @RequestParam(defaultValue = "id,ASC") String[] sort) throws ParseException {

        try {
            Pageable pagingSort = CommonUtils.sortItem(page, limit, sort);
            Page<Cart> cartPage = null;

            if(day != null) {
                cartPage = statisticService.getAllCartByDay(day, pagingSort);

                return new ResponseEntity<>(cartPage, HttpStatus.OK);
            }
            if(month != null) {
                cartPage = statisticService.getAllCartByMonth(month, pagingSort);

                return new ResponseEntity<>(cartPage, HttpStatus.OK);
            }
            if(quarter != null) {
                cartPage = statisticService.getAllCartByQuarter(quarter, pagingSort);

                return new ResponseEntity<>(cartPage, HttpStatus.OK);
            }
            if(year != null) {
                cartPage = statisticService.getAllCartByYear(year, pagingSort);

                return new ResponseEntity<>(cartPage, HttpStatus.OK);
            }

            return new ResponseEntity<>(new MessageResponse("Please provide time to get cart", HttpStatus.BAD_REQUEST, LocalDateTime.now()), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
        }
    }
}
