package com.example.btpn.controllers;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.btpn.dto.Orders.CreateOrderReq;
import com.example.btpn.dto.Orders.GetAllOrderRes;
import com.example.btpn.dto.Orders.GetOrderByIdRes;
import com.example.btpn.services.OrdersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/get-all")
    public ResponseEntity<List<GetAllOrderRes>> getAllItems(
        @RequestParam(name = "currentPage", defaultValue = "0") int currentPage, 
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
        @RequestParam(name = "filterBy", defaultValue = "customerName") String filterBy,
        @RequestParam(name = "filterValue", defaultValue = "") String filterValue,
        @RequestParam(name = "sortBy", defaultValue = "Order Date desc") String sortBy
    ) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        List<GetAllOrderRes> orders = ordersService.getAllOrders(pageable, filterBy, filterValue, sortBy);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GetOrderByIdRes> getItemsById(@PathVariable("id") Integer id) {
        GetOrderByIdRes order = ordersService.getOrdersById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createItem(@RequestParam("order") String orderJson) throws JsonMappingException, JsonProcessingException {
        CreateOrderReq createReq = objectMapper.readValue(orderJson, CreateOrderReq.class);
        ordersService.createOrder(createReq);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItems(@PathVariable("id") Integer id) {
        ordersService.deleteOrders(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/download-report")
    public ResponseEntity<byte[]> downloadReport(
        @RequestParam(name = "filterBy", defaultValue = "customerName") String filterBy,
        @RequestParam(name = "filterValue", defaultValue = "") String filterValue,
        @RequestParam(name = "sortBy", defaultValue = "Order Date desc") String sortBy
    ) throws FileNotFoundException, JRException{
        byte[] pdfBytes = ordersService.downloadPdfOrder(filterBy, filterValue, sortBy);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ordersReport.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
