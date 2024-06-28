package com.example.btpn.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.btpn.dto.Customers.CreateReq;
import com.example.btpn.dto.Customers.GetAllRes;
import com.example.btpn.dto.Customers.GetByIdRes;
import com.example.btpn.dto.Customers.UpdateReq;
import com.example.btpn.services.CustomersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {
    @Autowired
    CustomersService customersService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/get-all")
    public ResponseEntity<List<GetAllRes>> getAllCustomer(
        @RequestParam(name = "currentPage", defaultValue = "0") int currentPage, 
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
        @RequestParam(name = "filterBy", defaultValue = "customerName") String filterBy,
        @RequestParam(name = "filterValue", defaultValue = "") String filterValue,
        @RequestParam(name = "sortBy", defaultValue = "Name desc") String sortBy) 
    {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        List<GetAllRes> customers = customersService.getAllCustomer(pageable, filterBy, filterValue, sortBy);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GetByIdRes> getCustomerById(@PathVariable("id") Integer id) {
        GetByIdRes customers = customersService.getCustomerById(id);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createCustomer(@RequestParam("customer") String customerJson, @RequestParam(value = "pic", required = false) MultipartFile file) throws JsonMappingException, JsonProcessingException {
        CreateReq createReq = objectMapper.readValue(customerJson, CreateReq.class);
        customersService.createCustomer(createReq, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> editCustomer(@PathVariable("id") Integer id, @RequestParam("customer") String customerJson, @RequestParam(value = "pic", required = false) MultipartFile file) throws JsonMappingException, JsonProcessingException {
        UpdateReq updateReq = objectMapper.readValue(customerJson, UpdateReq.class);
        customersService.updateCustomer(id, updateReq, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Integer id) {
        customersService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/customers-name")
    public List<String> getCustomersName() {
        return customersService.getCustomersName();
    }
}