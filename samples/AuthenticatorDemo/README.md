# Authenticator Demo

This example demonstrates to register an authenticator and enroll to the available SignatureMethods to IBM Security Access Mananger. When the app starts, the camera is invoked straight away and after scanning a QR code, the result will be written to the logcat file:

```
2019-04-05 11:10:19.542 26767-26767/com.ibm.security.demoapps.authenticatordemo I/Authenticator Demo: Authenticator:{
      "accountName": "FIRSTNAME LASTNAME",
      "clientId": "IBMVerify",
      "detailsUri": "https:\/\/mobile-verify.securitypoc.com:443\/mga\/sps\/mmfa\/user\/mgmt\/details?client_id=IBMVerify",
      "identifier": "b8171287-5c9d-423e-89ff-cbdc063c6954",
      "name": "Big Blue Bank",
      "qrLoginUri": "https:\/\/mobile-verify.securitypoc.com:443\/mga\/sps\/apiauthsvc?PolicyId=urn:ibm:security:authentication:asf:qrcode_response",
      "registrationUri": "https:\/\/mobile-verify.securitypoc.com:443\/mga\/sps\/oauth\/oauth20\/token",
      "theme": {},
      "transactionUri": "https:\/\/mobile-verify.securitypoc.com:443\/scim\/Me?...",
      "ignoreSSL": false,
      "token": {
        "accessToken": "...",
        "refreshToken": "...",
        ...
      },
      "availableMethods": [...],
      "enrolledMethods": [...]
    }
```

# License

    Copyright 2017 International Business Machines

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.