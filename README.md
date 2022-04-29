# IBM Security Verify SDK for Android

[![SDK Version](https://img.shields.io/badge/IBM%20Security%20Verify%20SDK-2.1.5-blue.svg)](https://exchange.xforce.ibmcloud.com/hub/IdentityandAccess)
![Android Version](https://img.shields.io/badge/Android-12-green.svg)
![Android Version](https://img.shields.io/badge/Android%20API-31-green.svg)

This repository contains sample apps and code snippets to showcase and provide guidance when developing mobile applications with the IBM Security Verify SDK. The following steps will help you get started.

[Looking for the iOS version?](https://github.com/ibm-security/verify-sdk-ios)
<br/>

## ① Getting the SDK

To access the SDK you need to sign in with an IBMid account.  Create your free [IBMid](https://www.ibm.com/account/us-en/signup/register.html) and navigate to [IBM AppExchange](https://exchange.xforce.ibmcloud.com/hub/IdentityandAccess) to download the SDK.
<br/>

<table>
  <tr>
    <th>SDK&nbsp;Version</th>
    <th>API&nbsp;23</th>
    <th>API&nbsp;24</th>
    <th>API&nbsp;25</th>
    <th>API&nbsp;26</th>
    <th>API&nbsp;27</th>
    <th>API&nbsp;28</th>
    <th>API&nbsp;29</th>
    <th>API&nbsp;30</th>
    <th>API&nbsp;31</th>
    <th>Gradle Version</th>
  </tr>
  <tr>
    <td><a href="CHANGELOG.md#security-verify-sdk-215-27042022">v2.1.5</a></td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td>Yes</td>
    <td><b>Yes (Targeted)</b></td>
    <td>Yes</td>
    <td>7.2</td>
  </tr>
</table>

## ② Configuring your environment

The SDK can be used in Android Studio.

See our [instructions on configuring your project with the SDK](samples/getting-the-sdk.md).

### AndroidX support library ###
With version 2.0.6, the SDK requires the `AndroidX` support library. To use `AndroidX` is the recommended by [Google](https://developer.android.com/topic/libraries/support-library) and the [migration](https://developer.android.com/jetpack/androidx/migrate) of existing projects is supported by Android Studio.

## ③ Sample apps and code snippets

Please note: the IBM Security Verify SDK itself is not part of the samples and needs to be downloaded from [IBM AppExchange](https://exchange.xforce.ibmcloud.com/hub/IdentityandAccess) and added to the VerifySdk folder in the project.

Available [samples](samples/README.md) and [snippets](snippets/README.md) include:

<table>
    <tr>
        <th width="170px">Name</th>
        <th>Type</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>OAuth token using ROPC grant</td>
        <td><a href="samples/OAuth">Sample</a></td>
        <td>This example demonstrates acquiring and refreshing an OAuth token.</td>
    </tr>
    <tr>
        <td>QR code scanning</td>
        <td><a href="samples/QRCodeScan">Sample</a></td>
        <td>This example demonstrates scanning a QR code for one-time password (OTP) generation or multi-factor authentication (MMFA) with ISAM.</td>
    </tr>
        <tr>
        <td>Create Authenticator</td>
        <td><a href="samples/AuthenticatorDemo">Sample</a></td>
        <td>This example demonstrates to bootstrap a MFA authenticator with IBM Cloud Identity and IBM Security Access Manager,
        starting from scanning a QR code.</td>
    </tr>
    <tr>
        <td>Get OAuth token</td>
        <td><a href="snippets#oauthtoken">Snippet</a></td>
        <td> The SDK supports the ROPC grant flow.</td>
    </tr>
    <tr>
        <td>Key pair generation</td>
        <td><a href="snippets#keypairgen">Snippet</a></td>
        <td>Key pairs are used in the SDK to sign challenges, coming from IBM Security Access Manager. The private key remains on the device, whereas the public key gets uploaded to the server as part of the mechanisms enrollment.</td>
    </tr>
     <tr>
        <td>Signing data</td>
        <td><a href="snippets#signdata">Snippet</a></td>
        <td>The public key would be stored on a server and provide the challenge text to the client. The client uses the private key to sign the data which is sent back to the server. The server validates the signed data against the public key to verify the keys have not been tampered with.</td>
    </tr>
    <tr>
        <td>Certificate pinning</td>
        <td><a href="snippets#certpin">Snippet</a></td>
        <td>Compares a certificate stored in the mobile app as being the same certificate presented by the web server that provides the HTTPS connection.</td>
    </tr>
</table>
<br/>

## ④ IBM Security Verify for Android

IBM Security Verify is a mobile app for multi-factor authentication (MFA) with IBM Security Verify and IBM Security Verify Access. IBM Security Verify for Android features:
- One-time password (OTP) generation
- Device registration and enrolment
- Multi-tenant services for push notification
- Built on the IBM Security Verify SDK for Android

For more information about IBM Security Verify for Android, navigate to the [user guide](http://www-01.ibm.com/support/docview.wss?uid=swg27048979).

[![Download on the App Store](res/download-on-the-app-store.png)](https://itunes.apple.com/au/app/ibm-verify/id1162190392?mt=8)
[![Get it on Google Play](res/get-it-on-google-play-store.png)](https://play.google.com/store/apps/details?id=com.ibm.security.verifyapp)

<br/>

## Terms of Support
The Verify SDK for Android will support continuous delivery for features and security vulnerabilties and defects into the latest stream. Security vulnerabilties and critical defects will be backported into older SDK Versions. 
_Support_ is defined as fixing of critical security vulnerabilties and defects. _Support_ does not imply new feature enhancements.

| What's supported and what's not | Latest SDK Versions (API 31) | SDK Versions < API 31 |
|-------------------------------------------------------|-----------------|----------------|
| Android Studio updates                                | Yes             | No             |
| Java updates                                          | Yes             | No             |
| New features                                          | Yes             | No             |
| Security Vulnerabilties                               | Yes             | Yes            |
| Critical Defects                                      | Yes             | Yes            |
| Android API version updates                           | Yes             | No             |

<br/>

## Security Testing Process
IBM has an internal development and release process for ensuring code quality and to mitigate the risk of vulnerabilities.   As part of the development process, all products are scanned by security vulnerability scanning tools to mitigate the risks of at least the following:  

https://www.ibm.com/support/knowledgecenter/en/SSW2NF_9.0.3/com.ibm.ase.help.doc/topics/r_sans_cwe_top25_report.html

In addition, IBM Security products are developed and tested according to the best practices outlined in the IBM Secure Engineering Framework

http://www-03.ibm.com/security/secure-engineering/

We do not provide external security certifications for the Verify SDK. IBM recommends professional security scanning be performed on all mobile apps built with the IBM Security Verify SDK.

<br/>

# License

The contents of this repository are open-source under [this license](LICENSE). The IBM Security Verify SDK itself is closed-source.

```
Copyright 2018 International Business Machines

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

```
Google Play and the Google Play logo are trademarks of Google Inc.
```
