package com.example.btpn.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.btpn.dto.Customers.CreateReq;
import com.example.btpn.dto.Customers.GetAllRes;
import com.example.btpn.dto.Customers.GetByIdRes;
import com.example.btpn.dto.Customers.UpdateReq;
import com.example.btpn.models.Customers;
import com.example.btpn.repositories.CustomersRepo;
import com.example.btpn.services.specifications.CustomerSpec;

@Service
public class CustomersService {
    @Autowired
    CustomersRepo customersRepo;

    @Autowired
    MinioService minioService;

    @Autowired
    CustomerSpec customerSpec;

    public List<GetAllRes> getAllCustomer(Pageable pageable, String filterBy, String filterValue, String sortBy){
        List<Customers> customers = new ArrayList<>();
        
        if(filterValue.equals("")){
            customers = customersRepo.findAll();
        }
        else{ // Filtering here
            Specification<Customers> filterOption = customerSpec.filterSpec(filterBy, filterValue);
            customers = customersRepo.findAll(filterOption);
        }
        Integer totalCustomers = customers.size();

        // Sorting
        Comparator<Customers> sortComparator = customerSpec.sortComparatorSpec(sortBy);
        Collections.sort(customers, sortComparator);

        // Converting Customers from List to Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), customers.size());
        List<Customers> sublist = customers.subList(start, end);
        Page<Customers> customerPage = new PageImpl<>(sublist, pageable, customers.size());
        
        List<GetAllRes> getAllRes = customerPage.stream().map(cus -> {
            GetAllRes res = new GetAllRes();
            if(filterValue.equals("")){
                res.setTotalRecord(totalCustomers);
            }
            else{
                res.setTotalRecord(totalCustomers);
            }
            res.setCustomerId(cus.getCustomersId());
            res.setCustomerName(cus.getCustomerName());
            res.setCustomerAddress(cus.getCustomerAddress());
            res.setCustomerCode(cus.getCustomerCode());
            res.setIsActive(cus.getIsActive());
            res.setLastOrderDate(cus.getLastOrderDate());
            return res;
        })
        .collect(Collectors.toList());
        return getAllRes;
    }

    public GetByIdRes getCustomerById(Integer id){
        Optional<Customers> customer = customersRepo.findById(id);
        GetByIdRes getByIdRes = new GetByIdRes();
        getByIdRes.setCustomerId(customer.get().getCustomersId());
        getByIdRes.setCustomerName(customer.get().getCustomerName());
        getByIdRes.setCustomerAddress(customer.get().getCustomerAddress());
        getByIdRes.setCustomerCode(customer.get().getCustomerCode());
        getByIdRes.setCustomerPhone(customer.get().getCustomerPhone());
        getByIdRes.setIsActive(customer.get().getIsActive());
        getByIdRes.setLastOrderDate(customer.get().getLastOrderDate());
        getByIdRes.setPic(getLink(customer.get().getPic()));
        return getByIdRes;
    }

    public void createCustomer(CreateReq createReq, MultipartFile pic){
        Customers customers = new Customers();
        customers.setCustomerName(createReq.getCustomerName());
        customers.setCustomerAddress(createReq.getCustomerAddress());
        customers.setCustomerCode("");
        customers.setCustomerPhone(createReq.getCustomerPhone());
        customers.setIsActive(1);
        customers.setLastOrderDate(null);
        customers.setPic(pic.getOriginalFilename());
        upload(pic);
        customersRepo.save(customers);

        customers.setCustomerCode("CUST-" + customers.getCustomersId());
        customersRepo.save(customers);
    }

    public void updateCustomer(Integer id, UpdateReq updateReq, MultipartFile file){
        Optional<Customers> optionalCustomer = customersRepo.findById(id);
        Customers customers = optionalCustomer.get();
        customers.setCustomerName(updateReq.getCustomerName());
        customers.setCustomerAddress(updateReq.getCustomerAddress());
        customers.setCustomerPhone(updateReq.getCustomerPhone());
        if(file != null){
            String fileName = file.getOriginalFilename();
            customers.setPic(fileName);
        }
        customersRepo.save(customers);
        upload(file);
    }

    public void deleteCustomer(Integer id){
        Optional<Customers> optionalCustomer = customersRepo.findById(id);
        Customers customers = optionalCustomer.get();
        customers.setIsActive(0);
        customersRepo.save(customers);
    }

    public void upload(MultipartFile file){
        minioService.uploadFile("image-profile-pic", file);
    }

    public void download(String fileName){
        minioService.downloadFile("image-profile-pic", fileName);
    }

    public String getLink(String fileName){
        return minioService.getLink("image-profile-pic", fileName, 1);
    }
    
    public List<String> getCustomersName(){
        List<Customers> customers = customersRepo.findAll();
        List<String> customersName = customers.stream().map(customer -> {
            String name = customer.getCustomerName();
            return name;
        })
        .collect(Collectors.toList());
        return customersName;
    }
}