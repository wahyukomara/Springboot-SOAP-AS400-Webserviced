# Walkthrough: AS400 SOAP Web Service Connection

We have successfully created the Spring Boot SOAP Web Service project under `D:\Tutorial\springboot\AS400-SOAP` and developed the modern free-form RPGLE CRUD program locally under `D:\Tutorial\Antigrapity\AS400-CIF\EMPRPG\cifcrud.rpgle` as requested. 

Here is the comprehensive guide on how the files are structured, how to deploy/compile the RPGLE program manually, and how to verify the SOAP service.

---

## 📂 Created & Modified Files

### AS/400 Component
- **[cifcrud.rpgle](file:///D:/Tutorial/Antigrapity/AS400-CIF/EMPRPG/cifcrud.rpgle)**: Standalone bound ILE RPGLE program that handles Create, Read, Update, and Delete operations on `CIFPF`. Includes robust monitoring for Date parsing (`P_DOB`, `P_OPNDATE`) and automatically populates/preserves audit columns.

### Spring Boot Component (under `D:\Tutorial\springboot\AS400-SOAP`)
- **[pom.xml](file:///D:/Tutorial/springboot/AS400-SOAP/pom.xml)**: Maven build file configured with `spring-boot-starter-web-services`, `wsdl4j` for dynamic WSDL generation, and `jt400` for IBM i connection.
- **[application.properties](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/resources/application.properties)**: Configuration holding server port (`8080`) and AS/400 connection details.
- **[customers.xsd](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/resources/customers.xsd)**: XML schema describing payloads for customer operations.
- **[CustomerData.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/CustomerData.java)**: JAXB model mapping database columns.
- **[GetCustomerRequest.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/GetCustomerRequest.java)** / **[GetCustomerResponse.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/GetCustomerResponse.java)**: SOAP models for Read operation.
- **[CreateCustomerRequest.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/CreateCustomerRequest.java)** / **[CreateCustomerResponse.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/CreateCustomerResponse.java)**: SOAP models for Create operation.
- **[UpdateCustomerRequest.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/UpdateCustomerRequest.java)** / **[UpdateCustomerResponse.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/UpdateCustomerResponse.java)**: SOAP models for Update operation.
- **[DeleteCustomerRequest.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/DeleteCustomerRequest.java)** / **[DeleteCustomerResponse.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/DeleteCustomerResponse.java)**: SOAP models for Delete operation.
- **[package-info.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/package-info.java)**: Declares namespace package binding.
- **[As400CrudService.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/service/As400CrudService.java)**: Connects to AS/400 and invokes `/QSYS.LIB/WKOM2.LIB/CIFCRUD.PGM` via JT400 program calling. Maps Java types to AS/400 native types (e.g. `AS400PackedDecimal` and `AS400Text`).
- **[CustomerEndpoint.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/endpoint/CustomerEndpoint.java)**: Receives and processes incoming SOAP requests.
- **[WebServiceConfig.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/config/WebServiceConfig.java)**: Sets up the message dispatching servlet and dynamic WSDL publishing.
- **[SoapApplication.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/SoapApplication.java)**: Main Spring Boot application entry point.

---

## 🛠️ Step 1: Upload and Compile the RPGLE Program (Manually)

To deploy the CRUD RPGLE program on your AS/400 (`pub400.com`):

1. **Upload the Source Member**:
   - Transfer `cifcrud.rpgle` to your AS/400 source physical file using FTP, VS Code IBM i extension, or your preferred client.
   - Target location: Library: `WKOM1` | File: `QRPGLESRC` | Member: `CIFCRUD` | Type: `RPGLE`.

2. **Compile the Bound Program**:
   - Run the following terminal command on the IBM i command line:
     ```cmd
     CRTBNDRPG PGM(WKOM2/CIFCRUD) SRCFILE(WKOM1/QRPGLESRC) SRCMBR(CIFCRUD) REPLACE(*YES)
     ```
   - This compiles the source into a standalone program (`*PGM`) in library `WKOM2` which can be executed immediately.

---

## 🚀 Step 2: Running the Spring Boot Application

1. Open a terminal in `D:\Tutorial\springboot\AS400-SOAP` and run:
   ```bash
   mvn spring-boot:run
   ```
2. The application will start on port `8080` (you can configure this in `application.properties`).
3. Open your browser or a tool like SoapUI/Postman and navigate to:
   `http://localhost:8080/ws/customers.wsdl`
   This dynamic URL will display the fully-qualified WSDL contract exposing all CRUD operations!

---

## 🧪 SOAP Payload Examples

All SOAP requests must be posted to: `http://localhost:8080/ws` with `Content-Type: text/xml` or `application/soap+xml`.

### 1. Create Customer
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:CreateCustomerRequest>
         <soap:customer>
            <soap:customerId>CIF0000001</soap:customerId>
            <soap:customerName>Robert Downey Jr.</soap:customerName>
            <soap:idType>K</soap:idType>
            <soap:idNumber>32730123456789</soap:idNumber>
            <soap:dateOfBirth>1965-04-04</soap:dateOfBirth>
            <soap:gender>M</soap:gender>
            <soap:nationality>USA</soap:nationality>
            <soap:religion>C</soap:religion>
            <soap:maritalStatus>M</soap:maritalStatus>
            <soap:occupation>Actor</soap:occupation>
            <soap:address1>Malibu Sands Rd</soap:address1>
            <soap:address2>Suite 400</soap:address2>
            <soap:city>Los Angeles</soap:city>
            <soap:province>California</soap:province>
            <soap:postalCode>90265</soap:postalCode>
            <soap:country>USA</soap:country>
            <soap:phone>3105551212</soap:phone>
            <soap:mobile>3109999999</soap:mobile>
            <soap:email>robert.downey@stark.com</soap:email>
            <soap:customerType>I</soap:customerType>
            <soap:branchCode>0001</soap:branchCode>
            <soap:openingDate>2026-05-19</soap:openingDate>
            <soap:creditLimit>50000.00</soap:creditLimit>
            <soap:riskRating>L</soap:riskRating>
            <soap:status>A</soap:status>
         </soap:customer>
         <soap:user>WKOM</soap:user>
      </soap:CreateCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### 2. Read Customer
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:GetCustomerRequest>
         <soap:customerId>CIF0000001</soap:customerId>
      </soap:GetCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### 3. Update Customer
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:UpdateCustomerRequest>
         <soap:customer>
            <soap:customerId>CIF0000001</soap:customerId>
            <soap:customerName>Robert Downey Jr. (Iron Man)</soap:customerName>
            <soap:idType>K</soap:idType>
            <soap:idNumber>32730123456789</soap:idNumber>
            <soap:dateOfBirth>1965-04-04</soap:dateOfBirth>
            <soap:gender>M</soap:gender>
            <soap:nationality>USA</soap:nationality>
            <soap:religion>C</soap:religion>
            <soap:maritalStatus>M</soap:maritalStatus>
            <soap:occupation>Hero</soap:occupation>
            <soap:address1>Malibu Sands Rd</soap:address1>
            <soap:address2>Suite 3000</soap:address2>
            <soap:city>Los Angeles</soap:city>
            <soap:province>California</soap:province>
            <soap:postalCode>90265</soap:postalCode>
            <soap:country>USA</soap:country>
            <soap:phone>3105551212</soap:phone>
            <soap:mobile>3109999999</soap:mobile>
            <soap:email>robert.downey@stark.com</soap:email>
            <soap:customerType>I</soap:customerType>
            <soap:branchCode>0001</soap:branchCode>
            <soap:openingDate>2026-05-19</soap:openingDate>
            <soap:creditLimit>100000.00</soap:creditLimit>
            <soap:riskRating>L</soap:riskRating>
            <soap:status>A</soap:status>
         </soap:customer>
         <soap:user>WKOM</soap:user>
      </soap:UpdateCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### 4. Delete Customer
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://wkom.com/soap">
   <soapenv:Header/>
   <soapenv:Body>
      <soap:DeleteCustomerRequest>
         <soap:customerId>CIF0000001</soap:customerId>
      </soap:DeleteCustomerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

---

## 📬 Detailed Step 5: Testing SOAP Requests in Postman

To successfully execute SOAP operations using Postman, follow these exact settings:

### 1. Create a New Request
1. Open **Postman**.
2. Click on the **`+`** (New Request) tab.
3. Change the HTTP method from `GET` to **`POST`**.
4. Set the Request URL to:
   ```text
   http://localhost:8080/ws
   ```

### 2. Configure Headers
Click on the **Headers** tab below the URL bar. Add or verify the following:
* **Key**: `Content-Type`
* **Value**: `text/xml`

### 3. Configure the Request Body
1. Go to the **Body** tab.
2. Select the **`raw`** radio button.
3. On the right-side format dropdown, change the format selection to **`XML`**.
4. Paste one of the SOAP XML payloads (from the **🧪 SOAP Payload Examples** section above) into the text area.

### 4. Send and Inspect Response
1. Click the **Send** button.
2. Check the response status in Postman (it should be `200 OK`).
3. View the XML response in the lower pane. For example, a successful **CreateCustomerRequest** will return a response with `<success>true</success>` and the corresponding customer ID!

