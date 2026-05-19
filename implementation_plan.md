# AS400 SOAP Web Service Connection Plan

This plan outlines the design and steps to create a Spring Boot Maven project under `D:\Tutorial\springboot\AS400-SOAP` that acts as a SOAP Web Service to connect to pub400.com and perform CRUD operations on the `WKOM2.CIFPF` table. We also provide a custom RPGLE program `CIFCRUD` that will be compiled on the AS/400 and called via Java's JT400 program call capability.

---

## User Review Required

> [!IMPORTANT]
> **AS/400 Server & Credentials:**
> We are using the AS/400 host `pub400.com` with user `WKOM`, password `remeh3143`, and library `WKOM2`. Please ensure your credentials are correct and that the `CIFPF` physical file is compiled in library `WKOM2` as defined in `D:\Tutorial\Antigrapity\AS400-CIF\EMPRPG\CIFPF.PF`.

> [!WARNING]
> **Port and Namespace Configuration:**
> The SOAP Web Service will run on port `8080` (or another port if preferred) and export the endpoint at `/ws`. The service will auto-generate WSDL at `/ws/customers.wsdl`.

---

## Proposed Technical Design

### 1. The RPGLE Program (`CIFCRUD.rpgle`)
We will create a standalone, bound RPGLE program (`*PGM`) source code locally. You can upload and compile it manually to your AS/400.
Using a program call with standard parameters is highly robust and performs much better than direct SQL connection because it abstracts business logic, validates constraints (e.g. valid date parsing) in a native AS/400 environment, and keeps the audit columns (`CIFCDT`, `CIFCTM`, `CIFCUS1`, `CIFUDT`, `CIFUTM`, `CIFUUS`) fully consistent.

**Parameters:**
- `P_ACTION` (1A) - `'C'` (Create), `'R'` (Read), `'U'` (Update), `'D'` (Delete)
- `P_CUSTID` (10A) - Customer ID (Primary Key)
- `P_NAME` (50A) - Customer Name
- `P_IDTYPE` (1A) - ID Type (K=KTP, P=Passport, D=Driver License)
- `P_IDNUM` (25A) - Identification Number
- `P_DOB` (10A) - Date of Birth (YYYY-MM-DD)
- `P_GENDER` (1A) - Gender (M/F)
- `P_NAT` (3A) - Nationality Code
- `P_RELIG` (1A) - Religion Code
- `P_MARITAL` (1A) - Marital Status (S=Single, M=Married, D=Divorced, W=Widowed)
- `P_OCCUP` (30A) - Occupation
- `P_ADDR1` (50A) - Address Line 1
- `P_ADDR2` (50A) - Address Line 2
- `P_CITY` (30A) - City Name
- `P_PROV` (30A) - Province/State
- `P_ZIP` (10A) - Postal Code
- `P_COUNTRY` (3A) - Country Code
- `P_PHONE` (15A) - Phone Number
- `P_MOBILE` (15A) - Mobile Number
- `P_EMAIL` (50A) - Email Address
- `P_CUSTYPE` (1A) - Customer Type (I=Individual, C=Corporate)
- `P_BRANCH` (4A) - Branch Code
- `P_OPNDATE` (10A) - Opening Date (YYYY-MM-DD)
- `P_LIMIT` (15P 2) - Credit Limit (Packed Decimal)
- `P_RISK` (1A) - Risk Rating (L=Low, M=Medium, H=High)
- `P_STATUS` (1A) - Status (A=Active, I=Inactive, C=Closed, B=Blocked)
- `P_USER` (10A) - Current Operator (for Audit fields)
- `P_SUCCESS` (1A) - Output: `'Y'` (Yes) or `'N'` (No)
- `P_ERRCODE` (7A) - Output: Error code if failed
- `P_ERRMSG` (100A) - Output: Detailed error message if failed

### 2. Spring Boot Web SOAP Service Setup
We will write a standard Spring Boot 3 project using the following components:
- **`pom.xml`**: Setup dependencies for `spring-boot-starter-web-services`, `wsdl4j`, `jt400`, and `lombok`.
- **SOAP XSD (`customers.xsd`)**: Contract-first definition of types.
- **JAXB POJOs**: Explicitly defined JAXB Request/Response classes (manually created under `com.wkom.soap.model` package using standard Jakarta annotations to guarantee zero compilation or classloader errors during the build).
- **JT400 Service (`As400CrudService.java`)**: Manages `AS400` connection pools, constructs `ProgramCall` objects, populates parameters (mapping characters and packed decimal fields using `AS400Text` and `AS400PackedDecimal`), and interprets program execution responses.
- **SOAP Endpoint (`CustomerEndpoint.java`)**: Receives SOAP XML payloads, routes requests to `As400CrudService`, and maps the results to SOAP responses.
- **Web Service Configuration (`WebServiceConfig.java`)**: Configures Message Dispatchers, sets up the schema, and exposes the auto-generated WSDL at `/ws/customers.wsdl`.

---

## Proposed Changes

### AS/400 Component

#### [NEW] [cifcrud.rpgle](file:///D:/Tutorial/Antigrapity/AS400-CIF/EMPRPG/cifcrud.rpgle)
- Create a complete RPGLE program implementing the CRUD parameters and processing them using standard file I/O operations on the `CIFPF` physical file.

### Spring Boot Component

#### [NEW] [pom.xml](file:///D:/Tutorial/springboot/AS400-SOAP/pom.xml)
- Defines dependencies for Spring Boot 3, Spring Web Services, JT400, Lombok, and WSDL4J.

#### [NEW] [application.properties](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/resources/application.properties)
- Connection credentials for AS/400 (`pub400.com`, user, password, library) and server port (`8080`).

#### [NEW] [CustomerData.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/CustomerData.java)
- JAXB POJO holding customer details mapping directly to `CIFPF` columns.

#### [NEW] [SOAP Request/Response POJOs](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/model/)
- Complete set of manually created JAXB request/response classes including:
  - `GetCustomerRequest.java` / `GetCustomerResponse.java`
  - `CreateCustomerRequest.java` / `CreateCustomerResponse.java`
  - `UpdateCustomerRequest.java` / `UpdateCustomerResponse.java`
  - `DeleteCustomerRequest.java` / `DeleteCustomerResponse.java`

#### [NEW] [As400CrudService.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/service/As400CrudService.java)
- Service utilizing `jt400` to execute `AS400` ProgramCall on program `WKOM2/CIFCRUD`.

#### [NEW] [CustomerEndpoint.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/endpoint/CustomerEndpoint.java)
- Endpoint exposing SOAP operations at payload namespace root.

#### [NEW] [WebServiceConfig.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/config/WebServiceConfig.java)
- Configures Spring SOAP Web Service endpoints and dynamic WSDL generation.

#### [NEW] [SoapApplication.java](file:///D:/Tutorial/springboot/AS400-SOAP/src/main/java/com/wkom/soap/SoapApplication.java)
- Main class to run the Spring Boot service.

---

## Verification Plan

### Automated/Programmatic Verification
- **Compilation Check**: We will compile the Spring Boot application using `mvn clean compile` to ensure there are no compilation issues.

### Manual Verification
- **WSDL Check**: Run the Spring Boot application locally, navigate to `http://localhost:8080/ws/customers.wsdl` using a web request or browser to verify successful WSDL generation.
- **CRUD Operations**: Send sample SOAP XML payloads to `http://localhost:8080/ws` for each operation:
  - **Create**: Inserts a test customer.
  - **Read**: Retrieves the created customer to verify fields are fully matched.
  - **Update**: Modifies fields (e.g. Customer Name and Credit Limit) and verifies they change.
  - **Delete**: Deletes the test customer and verifies subsequent reads return NOT FOUND.
