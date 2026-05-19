# End-to-End SOAP Testing Scenarios & Test Data

Use these 6 sequential scenarios in Postman to test and verify the entire Create, Read, Update, and Delete lifecycle on the AS/400 `CIFPF` physical file.

* **Endpoint URL**: `http://localhost:8080/ws`
* **Headers**:
  * `Content-Type`: `text/xml`

---

## 📋 Scenario 1: Create a Customer ( CreateCustomerRequest )

* **HTTP Method**: `POST`
* **Goal**: Insert a new, unique test customer into the AS/400 `CIFPF` database table.
* **Request Payload**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:CreateCustomerRequest>
         <soap:customer>
            <soap:customerId>CUST990001</soap:customerId>
            <soap:customerName>Tony Stark</soap:customerName>
            <soap:idType>K</soap:idType>
            <soap:idNumber>1234567890123456</soap:idNumber>
            <soap:dateOfBirth>1970-05-29</soap:dateOfBirth>
            <soap:gender>M</soap:gender>
            <soap:nationality>USA</soap:nationality>
            <soap:religion>C</soap:religion>
            <soap:maritalStatus>M</soap:maritalStatus>
            <soap:occupation>Engineer</soap:occupation>
            <soap:address1>10880 Malibu Point</soap:address1>
            <soap:address2>Suite 100</soap:address2>
            <soap:city>Malibu</soap:city>
            <soap:province>California</soap:province>
            <soap:postalCode>90265</soap:postalCode>
            <soap:country>USA</soap:country>
            <soap:phone>3105553000</soap:phone>
            <soap:mobile>3109998888</soap:mobile>
            <soap:email>tony@starkindustries.com</soap:email>
            <soap:customerType>I</soap:customerType>
            <soap:branchCode>0001</soap:branchCode>
            <soap:openingDate>2026-05-19</soap:openingDate>
            <soap:creditLimit>9999999.99</soap:creditLimit>
            <soap:riskRating>L</soap:riskRating>
            <soap:status>A</soap:status>
         </soap:customer>
         <soap:user>WKOM</soap:user>
      </soap:CreateCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
* **Expected Response**:
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:CreateCustomerResponse xmlns:ns2="http://wkom.com/soap">
         <ns2:success>true</ns2:success>
         <ns2:customerId>CUST990001</ns2:customerId>
      </ns2:CreateCustomerResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
* **Postman Test Script** (Paste in **Tests** tab):
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify CreateCustomerResponse is successful", function () {
    var responseXml = pm.response.text();
    var jsonObject = xml2Json(responseXml);
    var body = jsonObject['SOAP-ENV:Envelope']['SOAP-ENV:Body'];
    var response = body['ns2:CreateCustomerResponse'];
    
    pm.expect(response['ns2:success']).to.equal("true");
    pm.expect(response['ns2:customerId']).to.equal("CUST990001");
});
```

---

## 📋 Scenario 2: Retrieve the Created Customer ( GetCustomerRequest )

* **HTTP Method**: `POST`
* **Goal**: Query the AS/400 to verify the record was successfully saved and all details match exactly.
* **Request Payload**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:GetCustomerRequest>
         <soap:customerId>CUST990001</soap:customerId>
      </soap:GetCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
* **Expected Response**:
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:GetCustomerResponse xmlns:ns2="http://wkom.com/soap">
         <ns2:success>true</ns2:success>
         <ns2:customer>
            <ns2:customerId>CUST990001</ns2:customerId>
            <ns2:customerName>Tony Stark</ns2:customerName>
            <ns2:idType>K</ns2:idType>
            <ns2:idNumber>1234567890123456</ns2:idNumber>
            <ns2:dateOfBirth>1970-05-29</ns2:dateOfBirth>
            <ns2:gender>M</ns2:gender>
            <ns2:nationality>USA</ns2:nationality>
            <ns2:religion>C</ns2:religion>
            <ns2:maritalStatus>M</ns2:maritalStatus>
            <ns2:occupation>Engineer</ns2:occupation>
            <ns2:address1>10880 Malibu Point</ns2:address1>
            <ns2:address2>Suite 100</ns2:address2>
            <ns2:city>Malibu</ns2:city>
            <ns2:province>California</ns2:province>
            <ns2:postalCode>90265</soap:postalCode>
            <ns2:country>USA</ns2:country>
            <ns2:phone>3105553000</ns2:phone>
            <ns2:mobile>3109998888</ns2:mobile>
            <ns2:email>tony@starkindustries.com</ns2:email>
            <ns2:customerType>I</ns2:customerType>
            <ns2:branchCode>0001</ns2:branchCode>
            <ns2:openingDate>2026-05-19</ns2:openingDate>
            <ns2:creditLimit>9999999.99</ns2:creditLimit>
            <ns2:riskRating>L</ns2:riskRating>
            <ns2:status>A</ns2:status>
         </ns2:customer>
      </ns2:GetCustomerResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
* **Postman Test Script** (Paste in **Tests** tab):
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify GetCustomerResponse matches created data", function () {
    var responseXml = pm.response.text();
    var jsonObject = xml2Json(responseXml);
    var body = jsonObject['SOAP-ENV:Envelope']['SOAP-ENV:Body'];
    var customer = body['ns2:GetCustomerResponse']['ns2:customer'];
    
    pm.expect(body['ns2:GetCustomerResponse']['ns2:success']).to.equal("true");
    pm.expect(customer['ns2:customerId']).to.equal("CUST990001");
    pm.expect(customer['ns2:customerName']).to.equal("Tony Stark");
    pm.expect(customer['ns2:creditLimit']).to.equal("9999999.99");
});
```

---

## 📋 Scenario 3: Update Customer Details ( UpdateCustomerRequest )

* **HTTP Method**: `POST`
* **Goal**: Test updating field values (changing Occupation, increasing Credit Limit, and modifying Risk Rating).
* **Request Payload**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:UpdateCustomerRequest>
         <soap:customer>
            <soap:customerId>CUST990001</soap:customerId>
            <soap:customerName>Tony Stark (Iron Man)</soap:customerName>
            <soap:idType>K</soap:idType>
            <soap:idNumber>1234567890123456</soap:idNumber>
            <soap:dateOfBirth>1970-05-29</soap:dateOfBirth>
            <soap:gender>M</soap:gender>
            <soap:nationality>USA</soap:nationality>
            <soap:religion>C</soap:religion>
            <soap:maritalStatus>M</soap:maritalStatus>
            <soap:occupation>Avenger</soap:occupation>
            <soap:address1>10880 Malibu Point</soap:address1>
            <soap:address2>Suite 3000</soap:address2>
            <soap:city>Malibu</soap:city>
            <soap:province>California</soap:province>
            <soap:postalCode>90265</soap:postalCode>
            <soap:country>USA</soap:country>
            <soap:phone>3105553000</soap:phone>
            <soap:mobile>3109998888</soap:mobile>
            <soap:email>tony@starkindustries.com</soap:email>
            <soap:customerType>I</soap:customerType>
            <soap:branchCode>0001</soap:branchCode>
            <soap:openingDate>2026-05-19</soap:openingDate>
            <soap:creditLimit>15000000.00</soap:creditLimit>
            <soap:riskRating>M</soap:riskRating>
            <soap:status>A</soap:status>
         </soap:customer>
         <soap:user>WKOM</soap:user>
      </soap:UpdateCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
* **Expected Response**:
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:UpdateCustomerResponse xmlns:ns2="http://wkom.com/soap">
         <ns2:success>true</ns2:success>
      </ns2:UpdateCustomerResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
* **Postman Test Script** (Paste in **Tests** tab):
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify UpdateCustomerResponse is successful", function () {
    var responseXml = pm.response.text();
    var jsonObject = xml2Json(responseXml);
    var body = jsonObject['SOAP-ENV:Envelope']['SOAP-ENV:Body'];
    var response = body['ns2:UpdateCustomerResponse'];
    
    pm.expect(response['ns2:success']).to.equal("true");
});
```

---

## 📋 Scenario 4: Verify the Updates ( GetCustomerRequest )

* **HTTP Method**: `POST`
* **Goal**: Re-query the customer to ensure the modified values are properly saved on the database.
* **Request Payload**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:GetCustomerRequest>
         <soap:customerId>CUST990001</soap:customerId>
      </soap:GetCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
* **Expected Response**:
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:GetCustomerResponse xmlns:ns2="http://wkom.com/soap">
         <ns2:success>true</ns2:success>
         <ns2:customer>
            <ns2:customerId>CUST990001</ns2:customerId>
            <ns2:customerName>Tony Stark (Iron Man)</ns2:customerName>
            <ns2:idType>K</ns2:idType>
            <ns2:idNumber>1234567890123456</ns2:idNumber>
            <ns2:dateOfBirth>1970-05-29</ns2:dateOfBirth>
            <ns2:gender>M</ns2:gender>
            <ns2:nationality>USA</ns2:nationality>
            <ns2:religion>C</ns2:religion>
            <ns2:maritalStatus>M</ns2:maritalStatus>
            <ns2:occupation>Avenger</ns2:occupation>
            <ns2:address1>10880 Malibu Point</ns2:address1>
            <ns2:address2>Suite 3000</ns2:address2>
            <ns2:city>Malibu</ns2:city>
            <ns2:province>California</ns2:province>
            <ns2:postalCode>90265</soap:postalCode>
            <ns2:country>USA</ns2:country>
            <ns2:phone>3105553000</ns2:phone>
            <ns2:mobile>3109998888</ns2:mobile>
            <ns2:email>tony@starkindustries.com</ns2:email>
            <ns2:customerType>I</ns2:customerType>
            <ns2:branchCode>0001</ns2:branchCode>
            <ns2:openingDate>2026-05-19</ns2:openingDate>
            <ns2:creditLimit>15000000.0</ns2:creditLimit>
            <ns2:riskRating>M</ns2:riskRating>
            <ns2:status>A</ns2:status>
         </ns2:customer>
      </ns2:GetCustomerResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
* **Postman Test Script** (Paste in **Tests** tab):
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify GetCustomerResponse matches updated details", function () {
    var responseXml = pm.response.text();
    var jsonObject = xml2Json(responseXml);
    var body = jsonObject['SOAP-ENV:Envelope']['SOAP-ENV:Body'];
    var customer = body['ns2:GetCustomerResponse']['ns2:customer'];
    
    pm.expect(body['ns2:GetCustomerResponse']['ns2:success']).to.equal("true");
    pm.expect(customer['ns2:customerName']).to.equal("Tony Stark (Iron Man)");
    pm.expect(customer['ns2:creditLimit']).to.equal("15000000.0");
    pm.expect(customer['ns2:occupation']).to.equal("Avenger");
});
```

---

## 📋 Scenario 5: Delete the Customer ( DeleteCustomerRequest )

* **HTTP Method**: `POST`
* **Goal**: Clean up the test customer from the `CIFPF` database table.
* **Request Payload**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:DeleteCustomerRequest>
         <soap:customerId>CUST990001</soap:customerId>
      </soap:DeleteCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
* **Expected Response**:
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:DeleteCustomerResponse xmlns:ns2="http://wkom.com/soap">
         <ns2:success>true</ns2:success>
      </ns2:DeleteCustomerResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
* **Postman Test Script** (Paste in **Tests** tab):
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify DeleteCustomerResponse is successful", function () {
    var responseXml = pm.response.text();
    var jsonObject = xml2Json(responseXml);
    var body = jsonObject['SOAP-ENV:Envelope']['SOAP-ENV:Body'];
    var response = body['ns2:DeleteCustomerResponse'];
    
    pm.expect(response['ns2:success']).to.equal("true");
});
```

---

## 📋 Scenario 6: Verify Deletion ( GetCustomerRequest )

* **HTTP Method**: `POST`
* **Goal**: Attempt to retrieve the deleted customer to confirm they no longer exist in the system.
* **Request Payload**:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:GetCustomerRequest>
         <soap:customerId>CUST990001</soap:customerId>
      </soap:GetCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
* **Expected Response**:
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ns2:GetCustomerResponse xmlns:ns2="http://wkom.com/soap">
         <ns2:success>false</ns2:success>
         <ns2:errorCode>CIF0002</ns2:errorCode>
         <ns2:errorMessage>Customer not found</ns2:errorMessage>
      </ns2:GetCustomerResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```
* **Postman Test Script** (Paste in **Tests** tab):
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify customer is not found", function () {
    var responseXml = pm.response.text();
    var jsonObject = xml2Json(responseXml);
    var body = jsonObject['SOAP-ENV:Envelope']['SOAP-ENV:Body'];
    var response = body['ns2:GetCustomerResponse'];
    
    pm.expect(response['ns2:success']).to.equal("false");
    pm.expect(response['ns2:errorCode']).to.equal("CIF0002");
    pm.expect(response['ns2:errorMessage']).to.equal("Customer not found");
});
```
