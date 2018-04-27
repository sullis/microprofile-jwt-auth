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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivateKey;
import java.util.HashMap;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.tck.container.jaxrs.TCKApplication;
import org.eclipse.microprofile.jwt.tck.util.TokenUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.eclipse.microprofile.jwt.tck.TCKConstants.TEST_GROUP_CONFIG;

/**
 * Validate that the bundled mp.jwt.verify.publickey config property as a literal PEM
 * is used to validate the JWT which is signed with privateKey4k.pem
 */
public class PublicKeyAsPEMTest extends Arquillian {

    /**
     * The base URL for the container under test
     */
    @ArquillianResource
    private URL baseURL;

    /**
     * Create a CDI aware base web application archive
     * @return the base base web application archive
     * @throws IOException - on resource failure
     */
    @Deployment()
    public static WebArchive createDeployment() throws IOException {
        URL publicKey = PublicKeyAsPEMTest.class.getResource("/publicKey.pem");
        URL config = PublicKeyAsPEMTest.class.getResource("/META-INF/microprofile-config.properties");
        WebArchive webArchive = ShrinkWrap
            .create(WebArchive.class, "PublicKeyAsPEMTest.war")
            .addAsResource(publicKey, "/publicKey.pem")
            .addClass(PublicKeyAsPEMEndpoint.class)
            .addClass(TCKApplication.class)
            .addAsWebInfResource("beans.xml", "beans.xml")
            .addAsManifestResource(config, "microprofile-config.properties")
            ;
        System.out.printf("WebArchive: %s\n", webArchive.toString(true));
        return webArchive;
    }
    @Deployment()
    public static WebArchive createLocationDeployment() throws IOException {
        URL publicKey = PublicKeyAsPEMTest.class.getResource("/publicKey.pem");
        URL config = PublicKeyAsPEMTest.class.getResource("/META-INF/microprofile-config.properties");
        WebArchive webArchive = ShrinkWrap
            .create(WebArchive.class, "PublicKeyAsPEMTest.war")
            .addAsResource(publicKey, "/publicKey.pem")
            .addClass(PublicKeyAsPEMEndpoint.class)
            .addClass(TCKApplication.class)
            .addAsWebInfResource("beans.xml", "beans.xml")
            .addAsManifestResource(config, "microprofile-config.properties")
            ;
        System.out.printf("WebArchive: %s\n", webArchive.toString(true));
        return webArchive;
    }

    @RunAsClient
    @Test(groups = TEST_GROUP_CONFIG,
        description = "Validate a request with MP-JWT succeeds with HTTP_OK, and replies with hello, user={token upn claim}")
    public void testKeyAsEmbeded() throws Exception {
        Reporter.log("callEcho, expect HTTP_OK");

        PrivateKey privateKey = TokenUtils.readPrivateKey("/privateKey4k.pem");
        String kid = "/privateKey4k.pem";
        HashMap<String, Long> timeClaims = new HashMap<>();
        String token = TokenUtils.generateTokenString(privateKey, kid, "/Token1.json", null, timeClaims);
        Long iatClaim = timeClaims.get(Claims.iat.name());
        Long authTimeClaim = timeClaims.get(Claims.auth_time.name());
        Long expClaim = timeClaims.get(Claims.exp.name());

        String uri = baseURL.toExternalForm() + "endp/echo";
        WebTarget echoEndpointTarget = ClientBuilder.newClient()
            .target(uri)
            .queryParam("input", "hello")
            ;
        Response response = echoEndpointTarget.request(TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION, "Bearer "+token).get();
        Assert.assertEquals(response.getStatus(), HttpURLConnection.HTTP_OK);
        String reply = response.readEntity(String.class);
        // Must return hello, user={token upn claim}
        Assert.assertEquals(reply, "hello, user=jdoe@example.com");
    }

    public void testKeyAsLocation() throws Exception {

    }
}
