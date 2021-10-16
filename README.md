## Low Budget Proof Key for Code Exchange (PKCE) Implementation using Java Spring-boot
#### Just for fun, low budget implementation of PKCE Auth Flow using a single Spring-boot application that exposes APIs to act as an authorization and resource server both. 
### Screen Recording showing Auth Flow (62 Seconds)
https://user-images.githubusercontent.com/69693621/137596996-2b5d48ce-9d5b-4e17-9472-c774eddd725f.mov

---
## Authorization Code Flow with Proof Key for Code Exchange (PKCE)
1.) **Configure Client-id and Redirect-URI in `application.properties` file as per [PkceConfigurationProperties.class](https://github.com/hardikSinghBehl/budget-pkce-spring-boot/blob/main/src/main/java/com/behl/ehrmantraut/security/configuration/properties/PkceConfigurationProperties.java)**

```
com.behl.ehrmantraut.security.client-id= <Client-id goes here>
com.behl.ehrmantraut.security.redirect-uri= <Redirect-URI to send code to, after successfull authentication>
com.behl.ehrmantraut.security.response-type=code
com.behl.ehrmantraut.security.code-challenge-method=S256
com.behl.ehrmantraut.security.grant-type=authorization_code
com.behl.ehrmantraut.security.code-expiration-minutes=2
```

2.) **Create the code verifier and challenge**

  Before each authentication request, the client app should generate a `code verifier` and a `code challenge`. 
  * The code verifier is a cryptographically random string between 43 and 128 characters in length. It can contain letters, digits, underscores, periods, hyphens, or tildes.
  * In order to generate the code challenge, the client should hash the code verifier using the SHA256 algorithm. Then, base64url encode the hash that is generated.
  * Sample Code verifier and code Challenge for demo (Not to be kept static and should be dynamically calculated before every request)
    * Code Verifier
    ```
    dcFKDCmdcYmcmW6DXu2BfSrkGB1cKwFAI5Jv7he9RDo
    ```
    * Code Challenge
    ```
    Ijcr0PLd8HvnhB9AZXlhmPPJjyLyaPkianM0ERzD860
    ```

3.) **Hit /authenticate POST API with the below data in the request body (Sample JSON given)**

  ```
{
    "emailId": "mike.ehrmantraut@gmail.com",
    "password": "noHalfMeasures",
    "clientId": "<Client-id as configured in .properties file>",
    "responseType": "code",
    "redirectUri": "<Redirect-URI as configured in .properties file>",
    "codeChallengeMethod": "S256",
    "codeChallenge": "<Set to the code challenge that was calculated in step 2>",
    "state": "<Optional: This can be used to mitigate cross-site request forgery attacks>"
}
  ```
 * [Reference](https://datatracker.ietf.org/doc/html/rfc6749#section-10.12) for using `state`
 
4.) Recieve `code` and `state` in the request parameter of the provided redirect-URI after successfull authentication
  * This code is a `one-time-use` commodity to `exchange token(s)` from the server
  * The redirection is done through `ResponseEntity.class`
  
  ```
  return ResponseEntity.status(HttpStatus.FOUND).location("attach query params (code and optional state to redirect-uri)").build();
  ```
  
  * **Hit /token POST API with the below data in the request body (Sample JSON given)**
  ```
{
    "clientId": "<Client-id as configured in .properties file>",
    "grantType": "authorization_code",
    "code": "<Code recieved in parameter of redirect-uri goes here>",
    "redirectUri": "<Redirect-URI as configured in .properties file>",
    "codeVerifier": "<The value of this key must match the value of the code_verifier that your app generated in step 2.>"
}
  ```
  
5.) On success, the response will have a 200 OK status and the following Sample JSON data in the response body

```
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbF9pZCI6Im1pa2UuZWhybWFudHJhdXRAZ21haWwuY29tIiwic3ViIjoibWlrZS5laHJtYW50cmF1dEBnbWFpbC5jb20iLCJhY2NvdW50X2NyZWF0aW9uX3RpbWVzdGFtcCI6IjIwMjEtMTAtMTZUMTQ6NDM6MTguMDQ1MDgwIiwidXNlcl9pZCI6ImZiNTRhNjdlLWI5NWItNDM2OS1iNjExLTdmYjRlYTA0NGQ4NiIsImV4cCI6MTYzNDM5OTAyMiwiaWF0IjoxNjM0Mzk1NDIyfQ._hUb127nUzI-GTkMIUbstTa21tuqRpsanektHnqzwCQ",
    "tokenType": "Bearer",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaWtlLmVocm1hbnRyYXV0QGdtYWlsLmNvbSIsImV4cCI6MTYzNTY5MTQyMiwiaWF0IjoxNjM0Mzk1NDIyfQ.Lf7dQNSDZ9NUp6W4a8HwtZb0dWrgy9wpsxH4Pjb2VOg",
    "expiresIn": 3600
}
```
* The recieved `accessToken` allows the client to make requests to the Server on behalf of the logged in-user using the `Authorization Bearer Mechanism`.

6.) A request to refresh an access token can be sent to the same `/token endpoint` with the below data in the request body (Sample JSON given)
```
{
    "clientId": "<Client-id as configured in .properties file>",
    "grantType": "refresh_token",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaWtlLmVocm1hbnRyYXV0QGdtYWlsLmNvbSIsImV4cCI6MTYzNTY5MTQyMiwiaWF0IjoxNjM0Mzk1NDIyfQ.Lf7dQNSDZ9NUp6W4a8HwtZb0dWrgy9wpsxH4Pjb2VOg"
}
```
--- 

### PRs welcome, Star repository to show support
### Tech stack used
* Java 17
* Spring-boot 2.5.5
* Spring-Security and [JJWT](https://github.com/jwtk/jjwt)
* Spring-JPA/Hibernate
* H2 in-memory DB
* Lombok
* LoadingCache (in [Guava](https://github.com/google/guava)) to store userAuthenticationDetails in memory

### Local Setup
* Install Java 17 (recommended to use [SdkMan](https://sdkman.io))

`sdk install java 17-open`
* Install Maven (recommended to use [SdkMan](https://sdkman.io))

`sdk install maven`

* Clone the repo and Go to application.properties under src/main/resources and configure properties as required

* Run the below command in core

`mvn clean install`

* To start the application, run any of the below 2 commands

`mvn spring-boot:run &`

`java -jar /target/budget-pkce-0.0.1-SNAPSHOT.jar`

* Access the swagger-ui

`http://localhost:8080/swagger-ui.html`
