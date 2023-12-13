# Changelog Android

## Security Verify SDK 2.1.9
- Bug fix: Handle missing transaction attributes gracefully
- Replace native library for string obfuscation with internal class

## Security Verify SDK 2.1.8
- Bug fix: dlopen failed: library "libnative-lib.dylib" not found

## Security Verify SDK 2.1.7
- 3rd party libraries updated to resolve vulnerabilities

## Security Verify SDK 2.1.6
- Update `jailbroken` Attribute for IBM Security Verify

## Security Verify SDK 2.1.5 (27.04.2022)
- Add support to customize 'user-agent' HTTP request header
- Return more descriptive error message if token could not be refreshed
- Fix problem with parsing for 'tokenType' for OnPremiseAuthenticators
- Expose server error response when fetching pending transaction

## Security Verify SDK 2.1.4 (30.06.2021)
- Add support to register grant attributes during device registration
- Change error description to "digit value out of range" when TOTP digits are not 6 or 8
- Fix NPE when unknown algorithm is used in OTP Uri

## Security Verify SDK 2.1.3 (--)
- skipped to get version back in sync with iOS

## Security Verify SDK 2.1.2 (11.02.2021)
- Fix NPE when server response for 'enrollCloud' does not contain expected attributes
- Fix NPE when server response for enrollments contains an error 

## Security Verify SDK 2.1.0 (07.10.2020)
- Rebranding to "IBM Security Verify SDK"
- Add support for "setInvalidatedByBiometricEnrollment" to key generation

## Verify SDK 2.0.6 (20.05.2019)
- Keep order for attributes in PendingTransaction view
- Fix NPE in AuthenticatorContext.getSignatureAuthenticationMethod
- Fix NPE in enrollOnPremiseTotp if server returns an error
- Fix NPE if getAuthenticationInfo is successful, but empty body received


## Verify SDK 2.0.5 (24.12.2019)
- Make OAuth token refresh optional during network calls
- Submit correct keyname for on-premise enrollments
- Add custom headers to OAuthContext requests
- Add support for customized headers to AuthenticatorContext requests for on-prem
- Make OAuth token refresh optional during parsing
- Handle error message that is returned with 200 from QRcode login


## Verify SDK 2.0.4 (26.07.2019)
- Fix on-premises filtering of pending transactions by `authenticator_id`
- Fix NPE when OAuth token could not be decrypted
- Fix KeyStore exception `android.os.ServiceSpecificException` in Android P
- Update lower boundary for TOTP period to 1 (second)
- Update targeted Android API to 29
- Enhances TOTP enrolment parsing for on-premises authenticators

## Verify SDK 2.0.3 (04.04.2019)
- Update on-premise transaction signing data value for ISAM v9.0.6.
- Support QR code login for Cloud and On-Premise.
- Support `DEVICE_ROOTED` detection.
- Enhance transaction data parsing of additional data to support custom JSON attributes.
- Expose the transaction timestamp in `PendingTransactions`.
- Prevent QR scanning where the TOTP period is less than 10 or greater than 300.
- Enforce Android API version 23 or greater for using the SDK

## Verify SDK 2.0.2 (10.10.2018)
- support certificate pinning 
- support default implementations for `ignoreSsl == true`

## Verify SDK 2.0.1
- n/a

## Verify SDK 2.0.0
- replacing Mobile Access SDK v1
- supporting both on-premises and cloud based multi-factor authenticators
- enhanced security features that apply to both on-premise and cloud


## Mobile Access SDK v1.2.6
- Support for checking if key pair exists

## Mobile Access SDK v1.2.5 (05.09.2017)
- Support parenthesis in url query part
- Support for determining if keys may be invalidated.
- Support for using authenticated keys for signing.
- Exposing error code from 'MobileKitException' class.
- Exposing algorithm and keystore name from 'KeyStoreHelper' class.

## Mobile Access SDK v1.2.4 (18.07.2017)
- Support for custom headers in OAuthContext.
- Support Base64 encoding options for public key export and data signing.
- Support for logging output to logcat
- Support for customised exception class 'MobileKitException'
- Accepts both "SHAx" and "HmacSHAx" as input for HmacAlgorithm.

## Mobile Access SDK v1.2.0 (30.09.2016)
- Support for internationalisation.
    * Czech, German, Spanish, French, Hungarian, Italian, Japanese, Korean, Polish, Portuguese, Russian, Chinese (Simplified and Taiwan)

- rename library to "IBMMobileKit-<version_number>.aar"

- Support for multi-factor (MFA) and non-MFA authentication enrolment
    * Fingerprint
    * User presence enrolment
    * HOTP enrolment
    * TOTP enrolment

- Support for multi-factor (MFA) authentication unenrollment
    * Fingerprint
    * User presence enrolment

- Support for context based challenge and verification
    * Fingerprint
    * User presence enrolment
    * HOTP enrolment
    * TOTP enrolment
    * Username password
- Support for extending the context based challenge framework
- Support for querying pending challenges

## Mobile Access SDK v1.0.0 (26.05.2016)
- Support for OAuth ROPC and AZN code flow
- Support for HMAC generated one-time password (HOTP)
- Support for time generated one-time password (TOTP)
