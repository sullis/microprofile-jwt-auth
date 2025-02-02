/*
 * Copyright (c) 2016-2018 Contributors to the Eclipse Foundation
 *
 *  See the NOTICE file(s) distributed with this work for additional
 *  information regarding copyright ownership.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.eclipse.microprofile.jwt.tck.config;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.jwt.tck.TCKConstants.TEST_GROUP_CONFIG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.config.Names;
import org.eclipse.microprofile.jwt.tck.TCKConstants;
import org.eclipse.microprofile.jwt.tck.util.MpJwtTestVersion;
import org.eclipse.microprofile.jwt.tck.util.TokenUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 * Validate that mp.jwt.verify.publickey.location property values of type remote URL to a PEM work to validate the JWT
 * which is signed with privateKey4k.pem
 */
public class PublicKeyAsPEMLocationURLTest extends Arquillian {

    /**
     * The base URL for the container under test
     */
    @ArquillianResource
    private URL baseURL;

    /**
     * Create a CDI aware base web application archive that includes an embedded JWK public key that is referenced via
     * the mp.jwt.verify.publickey.location as a URL resource property. The root url is /pem
     * 
     * @return the base base web application archive
     * @throws IOException
     *             - on resource failure
     */
    @Deployment()
    public static WebArchive createLocationURLDeployment() throws IOException {
        URL publicKey = PublicKeyAsPEMLocationURLTest.class.getResource("/publicKey4k.pem");
        // Setup the microprofile-config.properties content
        Properties configProps = new Properties();
        // Read in the base URL of deployment since it cannot be injected for use by this method
        String jwksBaseURL = System.getProperty("mp.jwt.tck.jwks.baseURL", "http://localhost:8080/");
        // Location points to the PEM endpoint of the deployment
        System.out.printf("baseURL=%s\n", jwksBaseURL);
        URL pemURL = new URL(new URL(jwksBaseURL), "pem/endp/publicKey4k");
        System.out.printf("pemURL=%s\n", pemURL);
        configProps.setProperty(Names.VERIFIER_PUBLIC_KEY_LOCATION, pemURL.toExternalForm());
        configProps.setProperty(Names.ISSUER, TCKConstants.TEST_ISSUER);
        StringWriter configSW = new StringWriter();
        configProps.store(configSW, "PublicKeyAsPEMLocationURLTest microprofile-config.properties");
        StringAsset configAsset = new StringAsset(configSW.toString());

        WebArchive webArchive = ShrinkWrap
                .create(WebArchive.class, "PublicKeyAsPEMLocationURLTest.war")
                .addAsManifestResource(new StringAsset(MpJwtTestVersion.MPJWT_V_1_1.name()),
                        MpJwtTestVersion.MANIFEST_NAME)
                .addAsResource(publicKey, "/publicKey4k.pem")
                .addAsResource(publicKey, "/publicKey.pem")
                .addClass(PublicKeyEndpoint.class)
                .addClass(PEMApplication.class)
                .addClass(SimpleTokenUtils.class)
                .addAsWebInfResource("beans.xml", "beans.xml")
                .addAsManifestResource(configAsset, "microprofile-config.properties");
        System.out.printf("WebArchive: %s\n", webArchive.toString(true));
        return webArchive;
    }

    @RunAsClient()
    @Test(groups = TEST_GROUP_CONFIG, description = "Validate the http://localhost:8080/pem/endp/publicKey4k PEM endpoint")
    public void validateLocationUrlContents() throws Exception {
        URL locationURL = new URL(baseURL, "pem/endp/publicKey4k");
        Reporter.log("Begin validateLocationUrlContents");

        StringWriter content = new StringWriter();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(locationURL.openStream()))) {
            String line = reader.readLine();
            while (line != null) {
                content.write(line);
                content.write('\n');
                line = reader.readLine();
            }
        }
        Reporter.log("Received: " + content);
        String expected = TokenUtils.readResource("/publicKey4k.pem");
        Assert.assertEquals(content.toString(), expected);
    }

    @RunAsClient
    @Test(groups = TEST_GROUP_CONFIG, dependsOnMethods = {
            "validateLocationUrlContents"}, description = "Validate specifying the mp.jwt.verify.publickey.location as remote URL to a PEM key")
    public void testKeyAsLocationUrl() throws Exception {
        Reporter.log("testKeyAsLocationUrl, expect HTTP_OK");

        PrivateKey privateKey = TokenUtils.readPrivateKey("/privateKey4k.pem");
        String kid = "/privateKey4k.pem";
        HashMap<String, Long> timeClaims = new HashMap<>();
        String token = TokenUtils.generateTokenString(privateKey, kid, "/Token1.json", null, timeClaims);

        String uri = baseURL.toExternalForm() + "pem/endp/verifyKeyLocationAsPEMUrl";
        WebTarget echoEndpointTarget = ClientBuilder.newClient()
                .target(uri);
        Response response =
                echoEndpointTarget.request(APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).get();
        Assert.assertEquals(response.getStatus(), HttpURLConnection.HTTP_OK);
        String replyString = response.readEntity(String.class);
        JsonReader jsonReader = Json.createReader(new StringReader(replyString));
        JsonObject reply = jsonReader.readObject();
        Reporter.log(reply.toString());
        Assert.assertTrue(reply.getBoolean("pass"), reply.getString("msg"));
    }
}
