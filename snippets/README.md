# Code snippets

## Requirements
Some Android API methods require a [Context](https://developer.android.com/reference/android/content/Context.html) object as a parameter. The SDK has a ```ContextHelper``` class, which holds an application context object. You only have to set it once (as early as possible within your application) and the SDK will retrieve it from there whenever it's needed.

Set the application context:
```java
// call this as early as possible in your application
ContextHelper.sharedInstance().setContext(getApplicationContext());
```


## <a name="oauthtoken"></a>Get an OAuth token
The SDK supports the ROPC grant flow.


```java
public void getOAuthToken() {

    final String username = "yourUsername";
    final String password = "yourPassword";
    final String hostname = "https://yourserver/mga/sps/oauth/oauth20/token"
    final boolean ignoreSsl = false;
    final String clientId = "yourClientId"

    OAuthContext.sharedInstance().authorize(hostname, clientId, username, password, ignoreSsl, new IResultCallback<OAuthToken>() {
        @Override
        public void handleResult(OAuthToken oAuthToken, VerifySdkException e) {

            if (e == null)  {
                // we got the token
                Log.i("Token", oAuthToken.getAccessToken())
            }
            else {
                Log.e("Error", e.toString());
            }
        }
    });
}

```


## <a name="keypairgen"></a>Key pair generation
Key pairs are used in the SDK to sign challenges. The private key remains on the device, whereas the public key gets uploaded to the server as part of the mechanisms enrollment. The Algorithm used is SHA256withRSA with a Keysize of 2048 and the key pairs are stored in the Android Keystore. 
```java
private void generateKeyPair(String keyName, boolean requiresAuthentication)  {

    KeyStoreHelper.createKeyPair(keyName, Algorithm.valueOf("SHA256withRSA"), requiresAuthentication, new IResultCallback<PublicKey>()  {
        @Override
        public void handleResult(PublicKey publicKey, VerifySdkException verifySdkException) {

            if (verifySdkException == null) {
                // keypair successfully generated
            }
        }
    })
}
```

## <a name="signdata"></a>Signing data
The public key would be stored on a server and provide the challenge text to the client. The client uses the private key to sign the data which is sent back to the server. The server validates the signed data against the public key to verify the keys have not been tampered with.

This example expects an existing key pair, generated with `authenticationRequired = false`. If the key was generated with `authenticationRequired = true`, the `AuthenticationResult` objects, that holds the unlocked key, needs to be passed into `mySignData`. See the SDK documentation for further details.

```java

private final String keyName = "myKeyName";
private final String algorithm = "SHA256withRSA";

private void mySignData()  {

    // key has been generated a described above
    Log.i("Signed data: ", KeyStoreHelper.signData(keyName, algorithm, "hello world");
}
```

## <a name="certpin"></a>Certificate pinning
Compares a certificate stored in the mobile app as being the same certificate presented by the web server that provides the HTTPS connection.

***Please note: CertificatePinner can not be used to pin self-signed certificate if such certificate is not accepted by TrustManager.***

This will pin the connections for a host to the public key of a certificate. 
```java
NetworkHandler.sharedInstance().setCertificatePinner(new CertificatePinner.Builder()
        .add("yourhost", "sha256/public_key_of_certificate")
        .build());
```
Asterisk * is only permitted in the left-most domain name label and must be the only character in that label, e.g. `*.ibm.com` is permitted, while `*verify.ibm.com` is not.

### Self-signed certificates
This section applies to SSL settings for connections only to IBM Security Access Manager appliances. It will not have any effect on connections to IBM Cloud Identity.

If you use self-signed certificates, you additionally need to customize [SSLContext](https://developer.android.com/reference/javax/net/ssl/SSLContext.html), [HostnameVerifier](https://developer.android.com/reference/javax/net/ssl/HostnameVerifier.html) and [TrustManager](https://developer.android.com/reference/javax/net/ssl/TrustManager).


### Downloading the certificate
Working with a development server, the following is the easiest way to download a certificate chain:
```sh
# for DER:
openssl s_client -connect <host>:<port> -showcerts 2>/dev/null </dev/null | openssl x509 -inform pem -outform der -out <certificate-name>.der

# for PEM:
openssl s_client -connect <host>:<port> -showcerts 2>/dev/null </dev/null | openssl x509 -inform pem -outform pem -out <certificate-name>.pem
```

#### Customize SSL settings
This sample overrides the SSL settings, so that the certificate provided by the server is accepted for this connection.
```java
private final String certificatePath = "my-server-certificate.crt";
private final String expectedHostname = "my-server.com";

TrustManager tm = new X509TrustManager() {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[]{};
    }
};


HostnameVerifier makeHostnameVerifier(String expectedHostname) {
    return new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return hostname.equalsIgnoreCase(expectedHostname);
        }
    };
}

SSLContext makeSslContext(String filename) {
    InputStream caInput = null;
    SSLContext context = null;
    try {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream in = getClass().getClassLoader().getResourceAsStream(filename);
        caInput = new BufferedInputStream(in);
        final Certificate ca;

        ca = cf.generateCertificate(caInput);
        Log.d("Certificate pinning", "makeSslContext: ca=" + ((X509Certificate) ca).getSubjectDN());

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("our trusted CA", ca);

        TrustManager customTrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                throw new CertificateException("This doesn't need to ever succeed");
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                byte[] found = chain[0].getEncoded();
                byte[] wanted = ca.getEncoded();
                if (!Arrays.equals(found, wanted)) {
                    throw new CertificateException("Presented certificate didn't match pinned certificate");
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[] {customTrustManager}, null);

    } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | IOException | KeyManagementException e) {
        e.printStackTrace();

    } finally {
        if (caInput != null) try { caInput.close(); }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    return context;
}

NetworkHandler.sharedInstance.setOnPremiseSslParameter(makeSslContext(certificatePath), tm, makeHostnameVerifier(expectedHostname));
```

#### Allow all connections
The SDK provides implementations that turn off SSL checks. ***Be careful if you use this!***
```java
NetworkHandler.sharedInstance().setOnPremiseSslParameter(
        NetworkHandler.sharedInstance().getSslContextTrustAll(),
        NetworkHandler.sharedInstance().getTrustManager(),
        NetworkHandler.sharedInstance().getHostnameVerifierAcceptAll());
```
