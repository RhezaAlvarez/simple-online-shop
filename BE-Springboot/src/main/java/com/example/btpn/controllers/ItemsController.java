package com.example.btpn.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.btpn.dto.Items.CreateItemReq;
import com.example.btpn.dto.Items.GetAllItemRes;
import com.example.btpn.dto.Items.GetItemByIdRes;
import com.example.btpn.dto.Items.UpdateItemReq;
import com.example.btpn.services.ItemsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/items")
public class ItemsController {
    @Autowired
    ItemsService itemsService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/get-all")
    public ResponseEntity<List<GetAllItemRes>> getAllItems(
        @RequestParam(name = "currentPage", defaultValue = "0") int currentPage, 
        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
        @RequestParam(name = "filterBy", defaultValue = "itemName") String filterBy,
        @RequestParam(name = "filterValue", defaultValue = "") String filterValue,
        @RequestParam(name = "sortBy", defaultValue = "Name desc") String sortBy
    ) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        List<GetAllItemRes> items = itemsService.getAllItems(pageable, filterBy, filterValue, sortBy);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GetItemByIdRes> getItemsById(@PathVariable("id") Integer id) {
        GetItemByIdRes item = itemsService.getItemsById(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createItem(@RequestParam("item") String itemJson) throws JsonMappingException, JsonProcessingException {
        CreateItemReq createReq = objectMapper.readValue(itemJson, CreateItemReq.class);
        itemsService.createItem(createReq);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> editItems(@PathVariable("id") Integer id, @RequestParam("item") String itemJson) throws JsonMappingException, JsonProcessingException {
        UpdateItemReq updateReq = objectMapper.readValue(itemJson, UpdateItemReq.class);
        itemsService.updateItems(id, updateReq);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItems(@PathVariable("id") Integer id) {
        itemsService.deleteItems(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/items-name")
    public List<String> getitemsName() {
        return itemsService.getItemsName();
    }
}
