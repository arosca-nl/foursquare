# foursquare

If you do not have an existing Foursquare account, you will need to [create an account](https://developer.foursquare.com/docs/places-api/getting-started/) for your Client ID and Secret.

The authentication information is placed in foursquare.properties, a new  file at the same level as gradle.properties

    foursquare.properties
    
    client_id="CLIENT_ID"
    client_secret="CLIENT_SECRET"
    
The application uses Userless Auth for simplicity, overlooking the security issues of storing the authentication keys inside the code.