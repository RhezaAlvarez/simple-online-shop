package com.example.btpn.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.btpn.dto.Orders.CreateOrderReq;
import com.example.btpn.dto.Orders.GetAllOrderRes;
import com.example.btpn.dto.Orders.GetOrderByIdRes;
import com.example.btpn.models.Customers;
import com.example.btpn.models.Items;
import com.example.btpn.models.Orders;
import com.example.btpn.repositories.CustomersRepo;
import com.example.btpn.repositories.ItemsRepo;
import com.example.btpn.repositories.OrdersRepo;
import com.example.btpn.services.specifications.OrderSpec;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Slf4j
@Service
public class OrdersService {
    @Autowired
    OrdersRepo ordersRepo;

    @Autowired
    CustomersRepo customersRepo;

    @Autowired
    ItemsRepo itemsRepo;

    @Autowired
    OrderSpec orderSpec;

    public List<GetAllOrderRes> getAllOrders(Pageable pageable, String filterBy, String filterValue, String sortBy){
        List<Orders> orders = new ArrayList<>();

        if(filterValue.equals("")){
            orders = ordersRepo.findAll();
        }
        else{ // Filtering here
            Specification<Orders> filterOption = orderSpec.filterSpec(filterBy, filterValue);
            orders = ordersRepo.findAll(filterOption);
        }
        Integer totalItems = orders.size();

        // Sorting
        Comparator<Orders> sortComparator = orderSpec.sortComparatorSpec(sortBy);
        Collections.sort(orders, sortComparator);

        // Converting Orders from List to Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), orders.size());
        List<Orders> sublist = orders.subList(start, end);
        Page<Orders> orderPage = new PageImpl<>(sublist, pageable, orders.size());

        List<GetAllOrderRes> getAllOrderRes = orderPage.stream().map(order -> {
            GetAllOrderRes res = new GetAllOrderRes();
            res.setTotalRecord(totalItems);
            res.setOrderId(order.getOrderId());
            res.setOrderCode(order.getOrderCode());
            res.setOrderDate(order.getOrderDate());
            res.setTotalPrice(order.getTotalPrice());
            res.setCustomerName(order.getCustomers().getCustomerName());
            res.setItemName(order.getItems().getItemName());
            res.setQuantity(order.getQuantity());
            return res;
        })
        .collect(Collectors.toList());

        return getAllOrderRes;
    }

    public GetOrderByIdRes getOrdersById(Integer id){
        Optional<Orders> orders = ordersRepo.findById(id);
        Orders order = orders.get();
        GetOrderByIdRes res = new GetOrderByIdRes();
            res.setOrderId(order.getOrderId());
            res.setOrderCode(order.getOrderCode());
            res.setOrderDate(order.getOrderDate());
            res.setTotalPrice(order.getTotalPrice());
            res.setCustomerName(order.getCustomers().getCustomerName());
            res.setItemName(order.getItems().getItemName());
            res.setQuantity(order.getQuantity());
        return res;
    }

    public void createOrder(CreateOrderReq createOrderReq){
        Customers customers = customersRepo.findByCustomerName(createOrderReq.getCustomerName()).get();
        Items items = itemsRepo.findByItemName(createOrderReq.getItemName()).get();

        if(items.getStock()-createOrderReq.getQuantity() < 0){
            log.debug("Stok tidak mencukupi");
        }
        else{
            Orders orders = new Orders();
            orders.setOrderCode("");
            orders.setOrderDate(LocalDateTime.now());
            orders.setTotalPrice(createOrderReq.getQuantity() * items.getPrice());
            orders.setCustomers(customers);
            orders.setItems(items);
            orders.setQuantity(createOrderReq.getQuantity());
            ordersRepo.save(orders);

            orders.setOrderCode("ORDER-" + orders.getOrderId());
            ordersRepo.save(orders);

            items.setStock(items.getStock()-createOrderReq.getQuantity());
            itemsRepo.save(items);

            customers.setLastOrderDate(LocalDateTime.now());
            customersRepo.save(customers);
        }
    }

    public Orders updateOrders(Integer id, Orders requestBody){
        Optional<Orders> optionalOrders = ordersRepo.findById(id);
        Orders orders = optionalOrders.get();
        orders.setOrderDate(requestBody.getOrderDate());
        orders.setTotalPrice(requestBody.getTotalPrice());
        orders.setQuantity(requestBody.getQuantity());
        ordersRepo.save(orders);
        return orders;
    }

    public Orders deleteOrders(Integer id){
        Optional<Orders> optionalOrders = ordersRepo.findById(id);
        ordersRepo.deleteById(id);
        return optionalOrders.get();
    }

    public byte[] downloadPdfOrder(String filterBy, String filterValue, String sortBy) throws FileNotFoundException, JRException{
        List<Orders> orders = new ArrayList<>();

        if(filterValue.equals("")){
            orders = ordersRepo.findAll();
        }
        else{ // Filtering here
            Specification<Orders> filterOption = orderSpec.filterSpec(filterBy, filterValue);
            orders = ordersRepo.findAll(filterOption);
        }

        // Sorting
        Comparator<Orders> sortComparator = orderSpec.sortComparatorSpec(sortBy);
        Collections.sort(orders, sortComparator);
        List<GetOrderByIdRes> getOrderByIdRes = orders.stream().map(order -> {
            GetOrderByIdRes res = new GetOrderByIdRes();
            res.setOrderId(order.getOrderId());
            res.setOrderCode(order.getOrderCode());
            res.setOrderDate(order.getOrderDate());
            res.setTotalPrice(order.getTotalPrice());
            res.setCustomerName(order.getCustomers().getCustomerName());
            res.setItemName(order.getItems().getItemName());
            res.setQuantity(order.getQuantity());
            return res;
        })
        .collect(Collectors.toList());

        File file = ResourceUtils.getFile("classpath:orders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(getOrderByIdRes);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Rheza");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        return pdfBytes;
    }
}
