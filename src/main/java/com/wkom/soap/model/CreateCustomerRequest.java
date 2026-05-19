package com.wkom.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CreateCustomerRequest", namespace = "http://wkom.com/soap")
public class CreateCustomerRequest {

    @XmlElement(name = "customer", required = true)
    private CustomerData customer;

    @XmlElement(name = "user", required = true)
    private String user;
}
