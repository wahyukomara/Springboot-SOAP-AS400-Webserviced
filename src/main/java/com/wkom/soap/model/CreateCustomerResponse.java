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
@XmlRootElement(name = "CreateCustomerResponse", namespace = "http://wkom.com/soap")
public class CreateCustomerResponse {

    @XmlElement(name = "success")
    private boolean success;

    @XmlElement(name = "errorCode")
    private String errorCode;

    @XmlElement(name = "errorMessage")
    private String errorMessage;

    @XmlElement(name = "customerId")
    private String customerId;
}
