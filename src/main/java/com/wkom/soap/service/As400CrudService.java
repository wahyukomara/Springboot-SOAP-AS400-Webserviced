package com.wkom.soap.service;

import com.ibm.as400.access.*;
import com.wkom.soap.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class As400CrudService {

    @Value("${as400.host}")
    private String host;

    @Value("${as400.user}")
    private String user;

    @Value("${as400.password}")
    private String password;

    @Value("${as400.library}")
    private String library;

    /**
     * Executes the CIFCRUD program on the AS/400.
     */
    private ProgramResult executeProgram(String action, CustomerData customer, String operator) throws Exception {
        log.info("Connecting to AS/400 at {} for action: {}", host, action);
        
        AS400 system = new AS400(host, this.user, password);
        system.setGuiAvailable(false);

        try {
            // Set up library list to include the target library
            log.info("Setting library list for job: ADDLIBLE LIB({})", library.toUpperCase());
            CommandCall cmd = new CommandCall(system);
            if (!cmd.run("ADDLIBLE LIB(" + library.toUpperCase() + ")")) {
                AS400Message[] msgs = cmd.getMessageList();
                for (AS400Message m : msgs) {
                    // CPF2103 means the library is already in the library list, which is safe to ignore
                    if (!"CPF2103".equals(m.getID())) {
                        log.warn("ADDLIBLE warning [{}]: {}", m.getID(), m.getText());
                    }
                }
            }

            // Program Path: /QSYS.LIB/LIBNAME.LIB/PROGNAME.PGM
            String programPath = String.format("/QSYS.LIB/%s.LIB/CIFCRUD.PGM", library.toUpperCase());
            
            // Define converters
            AS400Text text1 = new AS400Text(1, system);
            AS400Text text3 = new AS400Text(3, system);
            AS400Text text4 = new AS400Text(4, system);
            AS400Text text7 = new AS400Text(7, system);
            AS400Text text10 = new AS400Text(10, system);
            AS400Text text15 = new AS400Text(15, system);
            AS400Text text25 = new AS400Text(25, system);
            AS400Text text30 = new AS400Text(30, system);
            AS400Text text50 = new AS400Text(50, system);
            AS400Text text100 = new AS400Text(100, system);
            
            AS400PackedDecimal packedLimit = new AS400PackedDecimal(15, 2);

            ProgramParameter[] parameterList = new ProgramParameter[30];

            // Initialize local variables from customer object
            String custId = customer != null ? customer.getCustomerId() : "";
            String custName = customer != null ? customer.getCustomerName() : "";
            String idType = customer != null ? customer.getIdType() : "";
            String idNum = customer != null ? customer.getIdNumber() : "";
            String dob = customer != null ? customer.getDateOfBirth() : "";
            String gender = customer != null ? customer.getGender() : "";
            String nationality = customer != null ? customer.getNationality() : "";
            String religion = customer != null ? customer.getReligion() : "";
            String maritalSts = customer != null ? customer.getMaritalStatus() : "";
            String occupation = customer != null ? customer.getOccupation() : "";
            String addr1 = customer != null ? customer.getAddress1() : "";
            String addr2 = customer != null ? customer.getAddress2() : "";
            String city = customer != null ? customer.getCity() : "";
            String province = customer != null ? customer.getProvince() : "";
            String postal = customer != null ? customer.getPostalCode() : "";
            String country = customer != null ? customer.getCountry() : "";
            String phone = customer != null ? customer.getPhone() : "";
            String mobile = customer != null ? customer.getMobile() : "";
            String email = customer != null ? customer.getEmail() : "";
            String custType = customer != null ? customer.getCustomerType() : "";
            String branch = customer != null ? customer.getBranchCode() : "";
            String opnDate = customer != null ? customer.getOpeningDate() : "";
            double limit = customer != null ? customer.getCreditLimit() : 0.0;
            String risk = customer != null ? customer.getRiskRating() : "";
            String status = customer != null ? customer.getStatus() : "";

            // Populate Parameter List
            parameterList[0] = new ProgramParameter(text1.toBytes(action)); // P_ACTION
            parameterList[1] = new ProgramParameter(text10.toBytes(padOrTrim(custId, 10)), 10); // P_CUSTID
            parameterList[2] = new ProgramParameter(text50.toBytes(padOrTrim(custName, 50)), 50); // P_NAME
            parameterList[3] = new ProgramParameter(text1.toBytes(padOrTrim(idType, 1)), 1); // P_IDTYPE
            parameterList[4] = new ProgramParameter(text25.toBytes(padOrTrim(idNum, 25)), 25); // P_IDNUM
            parameterList[5] = new ProgramParameter(text10.toBytes(padOrTrim(dob, 10)), 10); // P_DOB
            parameterList[6] = new ProgramParameter(text1.toBytes(padOrTrim(gender, 1)), 1); // P_GENDER
            parameterList[7] = new ProgramParameter(text3.toBytes(padOrTrim(nationality, 3)), 3); // P_NAT
            parameterList[8] = new ProgramParameter(text1.toBytes(padOrTrim(religion, 1)), 1); // P_RELIG
            parameterList[9] = new ProgramParameter(text1.toBytes(padOrTrim(maritalSts, 1)), 1); // P_MARITAL
            parameterList[10] = new ProgramParameter(text30.toBytes(padOrTrim(occupation, 30)), 30); // P_OCCUP
            parameterList[11] = new ProgramParameter(text50.toBytes(padOrTrim(addr1, 50)), 50); // P_ADDR1
            parameterList[12] = new ProgramParameter(text50.toBytes(padOrTrim(addr2, 50)), 50); // P_ADDR2
            parameterList[13] = new ProgramParameter(text30.toBytes(padOrTrim(city, 30)), 30); // P_CITY
            parameterList[14] = new ProgramParameter(text30.toBytes(padOrTrim(province, 30)), 30); // P_PROV
            parameterList[15] = new ProgramParameter(text10.toBytes(padOrTrim(postal, 10)), 10); // P_ZIP
            parameterList[16] = new ProgramParameter(text3.toBytes(padOrTrim(country, 3)), 3); // P_COUNTRY
            parameterList[17] = new ProgramParameter(text15.toBytes(padOrTrim(phone, 15)), 15); // P_PHONE
            parameterList[18] = new ProgramParameter(text15.toBytes(padOrTrim(mobile, 15)), 15); // P_MOBILE
            parameterList[19] = new ProgramParameter(text50.toBytes(padOrTrim(email, 50)), 50); // P_EMAIL
            parameterList[20] = new ProgramParameter(text1.toBytes(padOrTrim(custType, 1)), 1); // P_CUSTYPE
            parameterList[21] = new ProgramParameter(text4.toBytes(padOrTrim(branch, 4)), 4); // P_BRANCH
            parameterList[22] = new ProgramParameter(text10.toBytes(padOrTrim(opnDate, 10)), 10); // P_OPNDATE
            parameterList[23] = new ProgramParameter(packedLimit.toBytes(limit), 8); // P_LIMIT (8 bytes)
            parameterList[24] = new ProgramParameter(text1.toBytes(padOrTrim(risk, 1)), 1); // P_RISK
            parameterList[25] = new ProgramParameter(text1.toBytes(padOrTrim(status, 1)), 1); // P_STATUS
            parameterList[26] = new ProgramParameter(text10.toBytes(padOrTrim(operator, 10))); // P_USER
            
            // Output Parameters
            parameterList[27] = new ProgramParameter(1); // P_SUCCESS
            parameterList[28] = new ProgramParameter(7); // P_ERRCODE
            parameterList[29] = new ProgramParameter(100); // P_ERRMSG

            ProgramCall program = new ProgramCall(system, programPath, parameterList);
            log.info("Running Program {}...", programPath);
            
            if (!program.run()) {
                AS400Message[] messageList = program.getMessageList();
                StringBuilder err = new StringBuilder("Program call failed: ");
                for (AS400Message message : messageList) {
                    err.append(message.getText()).append(" ");
                }
                log.error(err.toString());
                throw new RuntimeException(err.toString());
            }

            // Extract results
            String success = ((String) text1.toObject(parameterList[27].getOutputData())).trim();
            String errCode = ((String) text7.toObject(parameterList[28].getOutputData())).trim();
            String errMsg = ((String) text100.toObject(parameterList[29].getOutputData())).trim();

            CustomerData outCustomer = null;
            if ("Y".equalsIgnoreCase(success) && "R".equalsIgnoreCase(action)) {
                // If retrieve was successful, rebuild CustomerData object
                outCustomer = new CustomerData();
                outCustomer.setCustomerId(((String) text10.toObject(parameterList[1].getOutputData())).trim());
                outCustomer.setCustomerName(((String) text50.toObject(parameterList[2].getOutputData())).trim());
                outCustomer.setIdType(((String) text1.toObject(parameterList[3].getOutputData())).trim());
                outCustomer.setIdNumber(((String) text25.toObject(parameterList[4].getOutputData())).trim());
                outCustomer.setDateOfBirth(((String) text10.toObject(parameterList[5].getOutputData())).trim());
                outCustomer.setGender(((String) text1.toObject(parameterList[6].getOutputData())).trim());
                outCustomer.setNationality(((String) text3.toObject(parameterList[7].getOutputData())).trim());
                outCustomer.setReligion(((String) text1.toObject(parameterList[8].getOutputData())).trim());
                outCustomer.setMaritalStatus(((String) text1.toObject(parameterList[9].getOutputData())).trim());
                outCustomer.setOccupation(((String) text30.toObject(parameterList[10].getOutputData())).trim());
                outCustomer.setAddress1(((String) text50.toObject(parameterList[11].getOutputData())).trim());
                outCustomer.setAddress2(((String) text50.toObject(parameterList[12].getOutputData())).trim());
                outCustomer.setCity(((String) text30.toObject(parameterList[13].getOutputData())).trim());
                outCustomer.setProvince(((String) text30.toObject(parameterList[14].getOutputData())).trim());
                outCustomer.setPostalCode(((String) text10.toObject(parameterList[15].getOutputData())).trim());
                outCustomer.setCountry(((String) text3.toObject(parameterList[16].getOutputData())).trim());
                outCustomer.setPhone(((String) text15.toObject(parameterList[17].getOutputData())).trim());
                outCustomer.setMobile(((String) text15.toObject(parameterList[18].getOutputData())).trim());
                outCustomer.setEmail(((String) text50.toObject(parameterList[19].getOutputData())).trim());
                outCustomer.setCustomerType(((String) text1.toObject(parameterList[20].getOutputData())).trim());
                outCustomer.setBranchCode(((String) text4.toObject(parameterList[21].getOutputData())).trim());
                outCustomer.setOpeningDate(((String) text10.toObject(parameterList[22].getOutputData())).trim());
                
                BigDecimal outLimit = (BigDecimal) packedLimit.toObject(parameterList[23].getOutputData());
                outCustomer.setCreditLimit(outLimit.doubleValue());
                
                outCustomer.setRiskRating(((String) text1.toObject(parameterList[24].getOutputData())).trim());
                outCustomer.setStatus(((String) text1.toObject(parameterList[25].getOutputData())).trim());
            }

            log.info("Program execution finished. Success: {}, Error Code: {}", success, errCode);
            return new ProgramResult("Y".equalsIgnoreCase(success), errCode, errMsg, outCustomer);

        } finally {
            system.disconnectAllServices();
        }
    }

    private String padOrTrim(String value, int length) {
        if (value == null) {
            return "";
        }
        if (value.length() > length) {
            return value.substring(0, length);
        }
        return value;
    }

    //-----------------------------------------------------------------
    // SOAP CRUD Implementations
    //-----------------------------------------------------------------

    public CreateCustomerResponse createCustomer(CustomerData customer, String operator) {
        CreateCustomerResponse response = new CreateCustomerResponse();
        try {
            ProgramResult result = executeProgram("C", customer, operator);
            response.setSuccess(result.isSuccess());
            response.setErrorCode(result.getErrorCode());
            response.setErrorMessage(result.getErrorMessage());
            response.setCustomerId(customer.getCustomerId());
        } catch (Exception e) {
            log.error("Error creating customer", e);
            response.setSuccess(false);
            response.setErrorCode("SYS_ERR");
            response.setErrorMessage("System Error: " + e.getMessage());
        }
        return response;
    }

    public GetCustomerResponse getCustomer(String customerId) {
        GetCustomerResponse response = new GetCustomerResponse();
        try {
            CustomerData dummy = new CustomerData();
            dummy.setCustomerId(customerId);
            ProgramResult result = executeProgram("R", dummy, "");
            response.setSuccess(result.isSuccess());
            response.setErrorCode(result.getErrorCode());
            response.setErrorMessage(result.getErrorMessage());
            response.setCustomer(result.getCustomer());
        } catch (Exception e) {
            log.error("Error getting customer", e);
            response.setSuccess(false);
            response.setErrorCode("SYS_ERR");
            response.setErrorMessage("System Error: " + e.getMessage());
        }
        return response;
    }

    public UpdateCustomerResponse updateCustomer(CustomerData customer, String operator) {
        UpdateCustomerResponse response = new UpdateCustomerResponse();
        try {
            ProgramResult result = executeProgram("U", customer, operator);
            response.setSuccess(result.isSuccess());
            response.setErrorCode(result.getErrorCode());
            response.setErrorMessage(result.getErrorMessage());
        } catch (Exception e) {
            log.error("Error updating customer", e);
            response.setSuccess(false);
            response.setErrorCode("SYS_ERR");
            response.setErrorMessage("System Error: " + e.getMessage());
        }
        return response;
    }

    public DeleteCustomerResponse deleteCustomer(String customerId) {
        DeleteCustomerResponse response = new DeleteCustomerResponse();
        try {
            CustomerData dummy = new CustomerData();
            dummy.setCustomerId(customerId);
            ProgramResult result = executeProgram("D", dummy, "");
            response.setSuccess(result.isSuccess());
            response.setErrorCode(result.getErrorCode());
            response.setErrorMessage(result.getErrorMessage());
        } catch (Exception e) {
            log.error("Error deleting customer", e);
            response.setSuccess(false);
            response.setErrorCode("SYS_ERR");
            response.setErrorMessage("System Error: " + e.getMessage());
        }
        return response;
    }

    // Helper holder class for execution results
    private static class ProgramResult {
        private final boolean success;
        private final String errorCode;
        private final String errorMessage;
        private final CustomerData customer;

        public ProgramResult(boolean success, String errorCode, String errorMessage, CustomerData customer) {
            this.success = success;
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.customer = customer;
        }

        public boolean isSuccess() { return success; }
        public String getErrorCode() { return errorCode; }
        public String getErrorMessage() { return errorMessage; }
        public CustomerData getCustomer() { return customer; }
    }
}
