package com.wkom.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerData", namespace = "http://wkom.com/soap")
public class CustomerData {

    @XmlElement(name = "customerId", required = true)
    private String customerId;

    @XmlElement(name = "customerName")
    private String customerName;

    @XmlElement(name = "idType")
    private String idType;

    @XmlElement(name = "idNumber")
    private String idNumber;

    @XmlElement(name = "dateOfBirth")
    private String dateOfBirth; // Format: YYYY-MM-DD

    @XmlElement(name = "gender")
    private String gender;

    @XmlElement(name = "nationality")
    private String nationality;

    @XmlElement(name = "religion")
    private String religion;

    @XmlElement(name = "maritalStatus")
    private String maritalStatus;

    @XmlElement(name = "occupation")
    private String occupation;

    @XmlElement(name = "address1")
    private String address1;

    @XmlElement(name = "address2")
    private String address2;

    @XmlElement(name = "city")
    private String city;

    @XmlElement(name = "province")
    private String province;

    @XmlElement(name = "postalCode")
    private String postalCode;

    @XmlElement(name = "country")
    private String country;

    @XmlElement(name = "phone")
    private String phone;

    @XmlElement(name = "mobile")
    private String mobile;

    @XmlElement(name = "email")
    private String email;

    @XmlElement(name = "customerType")
    private String customerType;

    @XmlElement(name = "branchCode")
    private String branchCode;

    @XmlElement(name = "openingDate")
    private String openingDate; // Format: YYYY-MM-DD

    @XmlElement(name = "creditLimit")
    private double creditLimit;

    @XmlElement(name = "riskRating")
    private String riskRating;

    @XmlElement(name = "status")
    private String status;
}
