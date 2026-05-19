     H DFTACTGRP(*NO) ACTGRP(*CALLER)
      *===============================================================
      * Program   : CIFCRUD - CIF Table Maintenance
      * Description: Program to perform Create, Read, Update, Delete
      *              operations on physical file CIFPF.
      *              Called by Java via JT400 ProgramCall.
      * System    : Banking CIF System
      *===============================================================

     FCIFPF     UF A E           K DISK    RENAME(CIFREC:PFREC)

      *---------------------------------------------------------------
      * Prototype Definition
      *---------------------------------------------------------------
     D CIFCRUD         PR                  EXTPGM('CIFCRUD')
     D  P_ACTION                      1A
     D  P_CUSTID                     10A
     D  P_NAME                       50A
     D  P_IDTYPE                      1A
     D  P_IDNUM                      25A
     D  P_DOB                        10A
     D  P_GENDER                      1A
     D  P_NAT                         3A
     D  P_RELIG                       1A
     D  P_MARITAL                     1A
     D  P_OCCUP                      30A
     D  P_ADDR1                      50A
     D  P_ADDR2                      50A
     D  P_CITY                       30A
     D  P_PROV                       30A
     D  P_ZIP                        10A
     D  P_COUNTRY                     3A
     D  P_PHONE                      15A
     D  P_MOBILE                     15A
     D  P_EMAIL                      50A
     D  P_CUSTYPE                     1A
     D  P_BRANCH                      4A
     D  P_OPNDATE                    10A
     D  P_LIMIT                      15P 2
     D  P_RISK                        1A
     D  P_STATUS                      1A
     D  P_USER                       10A
     D  P_SUCCESS                     1A
     D  P_ERRCODE                     7A
     D  P_ERRMSG                    100A

      *---------------------------------------------------------------
      * Procedure Interface
      *---------------------------------------------------------------
     D CIFCRUD         PI
     D  P_ACTION                      1A
     D  P_CUSTID                     10A
     D  P_NAME                       50A
     D  P_IDTYPE                      1A
     D  P_IDNUM                      25A
     D  P_DOB                        10A
     D  P_GENDER                      1A
     D  P_NAT                         3A
     D  P_RELIG                       1A
     D  P_MARITAL                     1A
     D  P_OCCUP                      30A
     D  P_ADDR1                      50A
     D  P_ADDR2                      50A
     D  P_CITY                       30A
     D  P_PROV                       30A
     D  P_ZIP                        10A
     D  P_COUNTRY                     3A
     D  P_PHONE                      15A
     D  P_MOBILE                     15A
     D  P_EMAIL                      50A
     D  P_CUSTYPE                     1A
     D  P_BRANCH                      4A
     D  P_OPNDATE                    10A
     D  P_LIMIT                      15P 2
     D  P_RISK                        1A
     D  P_STATUS                      1A
     D  P_USER                       10A
     D  P_SUCCESS                     1A
     D  P_ERRCODE                     7A
     D  P_ERRMSG                    100A

      *---------------------------------------------------------------
      * Work Variables
      *---------------------------------------------------------------
     D W_DOB           S               D
     D W_OPD           S               D

      *===============================================================
      * Main Logic Block
      *===============================================================
      /FREE

       P_SUCCESS = 'N';
       P_ERRCODE = *blanks;
       P_ERRMSG = *blanks;

       select;
         //-----------------------------------------------------------
         // CREATE Operation
         //-----------------------------------------------------------
         when P_ACTION = 'C';
           chain P_CUSTID CIFPF;
           if %found(CIFPF);
             P_SUCCESS = 'N';
             P_ERRCODE = 'CIF0001';
             P_ERRMSG = 'Customer ID already exists';
             return;
           endif;

           // Parse Date of Birth
           monitor;
             if P_DOB = *blanks;
               W_DOB = D'1900-01-01';
             else;
               W_DOB = %date(P_DOB:*ISO);
             endif;
           on-error;
             P_SUCCESS = 'N';
             P_ERRCODE = 'ERRDATE';
             P_ERRMSG = 'Invalid Date of Birth format. Expected YYYY-MM-DD.';
             return;
           endmon;

           // Parse Opening Date
           monitor;
             if P_OPNDATE = *blanks;
               W_OPD = %date();
             else;
               W_OPD = %date(P_OPNDATE:*ISO);
             endif;
           on-error;
             P_SUCCESS = 'N';
             P_ERRCODE = 'ERRDATE';
             P_ERRMSG = 'Invalid Opening Date format. Expected YYYY-MM-DD.';
             return;
           endmon;

           // Map parameters to file fields
           CIFCUS  = P_CUSTID;
           CIFNAM  = P_NAME;
           CIFIDT  = P_IDTYPE;
           CIFIDN  = P_IDNUM;
           CIFDOB  = W_DOB;
           CIFGEN  = P_GENDER;
           CIFNTN  = P_NAT;
           CIFRLN  = P_RELIG;
           CIFMRS  = P_MARITAL;
           CIFOCP  = P_OCCUP;
           CIFAD1  = P_ADDR1;
           CIFAD2  = P_ADDR2;
           CIFCTY  = P_CITY;
           CIFPRV  = P_PROV;
           CIFZIP  = P_ZIP;
           CIFCTR  = P_COUNTRY;
           CIFPHN  = P_PHONE;
           CIFMOB  = P_MOBILE;
           CIFEML  = P_EMAIL;
           CIFTYP  = P_CUSTYPE;
           CIFBRN  = P_BRANCH;
           CIFOPD  = W_OPD;
           CIFLMT  = P_LIMIT;
           CIFRSK  = P_RISK;
           CIFSTS  = P_STATUS;

           // Set audit fields
           CIFCDT  = %date();
           CIFCTM  = %time();
           CIFCUS1 = P_USER;
           CIFUDT  = CIFCDT;
           CIFUTM  = CIFCTM;
           CIFUUS  = P_USER;

           write PFREC;

           P_SUCCESS = 'Y';
           P_ERRMSG = 'Customer created successfully';

         //-----------------------------------------------------------
         // RETRIEVE/READ Operation
         //-----------------------------------------------------------
         when P_ACTION = 'R';
           chain P_CUSTID CIFPF;
           if not %found(CIFPF);
             P_SUCCESS = 'N';
             P_ERRCODE = 'CIF0002';
             P_ERRMSG = 'Customer not found';
             return;
           endif;

           // Map fields to output parameters
           P_NAME    = CIFNAM;
           P_IDTYPE  = CIFIDT;
           P_IDNUM   = CIFIDN;
           P_DOB     = %char(CIFDOB:*ISO);
           P_GENDER  = CIFGEN;
           P_NAT     = CIFNTN;
           P_RELIG   = CIFRLN;
           P_MARITAL = CIFMRS;
           P_OCCUP   = CIFOCP;
           P_ADDR1   = CIFAD1;
           P_ADDR2   = CIFAD2;
           P_CITY    = CIFCTY;
           P_PROV    = CIFPRV;
           P_ZIP     = CIFZIP;
           P_COUNTRY = CIFCTR;
           P_PHONE   = CIFPHN;
           P_MOBILE  = CIFMOB;
           P_EMAIL   = CIFEML;
           P_CUSTYPE = CIFTYP;
           P_BRANCH  = CIFBRN;
           P_OPNDATE = %char(CIFOPD:*ISO);
           P_LIMIT   = CIFLMT;
           P_RISK    = CIFRSK;
           P_STATUS  = CIFSTS;

           P_SUCCESS = 'Y';
           P_ERRMSG = 'Customer retrieved successfully';

         //-----------------------------------------------------------
         // UPDATE Operation
         //-----------------------------------------------------------
         when P_ACTION = 'U';
           chain P_CUSTID CIFPF;
           if not %found(CIFPF);
             P_SUCCESS = 'N';
             P_ERRCODE = 'CIF0002';
             P_ERRMSG = 'Customer not found';
             return;
           endif;

           // Parse Date of Birth
           monitor;
             if P_DOB = *blanks;
               W_DOB = D'1900-01-01';
             else;
               W_DOB = %date(P_DOB:*ISO);
             endif;
           on-error;
             P_SUCCESS = 'N';
             P_ERRCODE = 'ERRDATE';
             P_ERRMSG = 'Invalid Date of Birth format. Expected YYYY-MM-DD.';
             return;
           endmon;

           // Parse Opening Date
           monitor;
             if P_OPNDATE = *blanks;
               W_OPD = %date();
             else;
               W_OPD = %date(P_OPNDATE:*ISO);
             endif;
           on-error;
             P_SUCCESS = 'N';
             P_ERRCODE = 'ERRDATE';
             P_ERRMSG = 'Invalid Opening Date format. Expected YYYY-MM-DD.';
             return;
           endmon;

           // Map parameters to file fields
           CIFNAM  = P_NAME;
           CIFIDT  = P_IDTYPE;
           CIFIDN  = P_IDNUM;
           CIFDOB  = W_DOB;
           CIFGEN  = P_GENDER;
           CIFNTN  = P_NAT;
           CIFRLN  = P_RELIG;
           CIFMRS  = P_MARITAL;
           CIFOCP  = P_OCCUP;
           CIFAD1  = P_ADDR1;
           CIFAD2  = P_ADDR2;
           CIFCTY  = P_CITY;
           CIFPRV  = P_PROV;
           CIFZIP  = P_ZIP;
           CIFCTR  = P_COUNTRY;
           CIFPHN  = P_PHONE;
           CIFMOB  = P_MOBILE;
           CIFEML  = P_EMAIL;
           CIFTYP  = P_CUSTYPE;
           CIFBRN  = P_BRANCH;
           CIFOPD  = W_OPD;
           CIFLMT  = P_LIMIT;
           CIFRSK  = P_RISK;
           CIFSTS  = P_STATUS;

           // Update audit fields (creation fields preserved)
           CIFUDT  = %date();
           CIFUTM  = %time();
           CIFUUS  = P_USER;

           update PFREC;

           P_SUCCESS = 'Y';
           P_ERRMSG = 'Customer updated successfully';

         //-----------------------------------------------------------
         // DELETE Operation
         //-----------------------------------------------------------
         when P_ACTION = 'D';
           chain P_CUSTID CIFPF;
           if not %found(CIFPF);
             P_SUCCESS = 'N';
             P_ERRCODE = 'CIF0002';
             P_ERRMSG = 'Customer not found';
             return;
           endif;

           delete PFREC;

           P_SUCCESS = 'Y';
           P_ERRMSG = 'Customer deleted successfully';

         //-----------------------------------------------------------
         // INVALID Operation
         //-----------------------------------------------------------
         other;
           P_SUCCESS = 'N';
           P_ERRCODE = 'ERRACT';
           P_ERRMSG = 'Invalid action. Supported actions: C, R, U, D';
       endsl;

       *inlr = *on;
       return;

      /END-FREE
