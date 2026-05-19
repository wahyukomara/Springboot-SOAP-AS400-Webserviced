package com.wkom.soap.endpoint;

import com.wkom.soap.model.*;
import com.wkom.soap.service.As400CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Slf4j
@Endpoint
public class CustomerEndpoint {

    private static final String NAMESPACE_URI = "http://wkom.com/soap";

    private final As400CrudService as400CrudService;

    @Autowired
    public CustomerEndpoint(As400CrudService as400CrudService) {
        this.as400CrudService = as400CrudService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetCustomerRequest")
    @ResponsePayload
    public GetCustomerResponse getCustomer(@RequestPayload GetCustomerRequest request) {
        log.info("SOAP: GetCustomerRequest received for Customer ID: {}", request.getCustomerId());
        return as400CrudService.getCustomer(request.getCustomerId());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateCustomerRequest")
    @ResponsePayload
    public CreateCustomerResponse createCustomer(@RequestPayload CreateCustomerRequest request) {
        log.info("SOAP: CreateCustomerRequest received for Customer ID: {} by User: {}", 
                 request.getCustomer().getCustomerId(), request.getUser());
        return as400CrudService.createCustomer(request.getCustomer(), request.getUser());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateCustomerRequest")
    @ResponsePayload
    public UpdateCustomerResponse updateCustomer(@RequestPayload UpdateCustomerRequest request) {
        log.info("SOAP: UpdateCustomerRequest received for Customer ID: {} by User: {}", 
                 request.getCustomer().getCustomerId(), request.getUser());
        return as400CrudService.updateCustomer(request.getCustomer(), request.getUser());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteCustomerRequest")
    @ResponsePayload
    public DeleteCustomerResponse deleteCustomer(@RequestPayload DeleteCustomerRequest request) {
        log.info("SOAP: DeleteCustomerRequest received for Customer ID: {}", request.getCustomerId());
        return as400CrudService.deleteCustomer(request.getCustomerId());
    }
}
