/*
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.jwt.tck.container.jaxrs;

import java.util.Optional;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import org.eclipse.microprofile.jwt.Claims;

@Path("/endp")
@RolesAllowed({"Echoer", "Tester"})
@RequestScoped
public class ClaimValueInjectionEndpoint {
    @Inject
    @Claim("raw_token")
    private ClaimValue<String> rawToken;
    @Inject
    @Claim("iss")
    private ClaimValue<String> issuer;
    @Inject
    @Claim("jti")
    private ClaimValue<String> jti;
    @Inject
    @Claim("aud")
    private ClaimValue<Set<String>> aud;
    @Inject
    @Claim("iat")
    private ClaimValue<Long> issuedAt;
    @Inject
    @Claim("sub")
    private ClaimValue<Optional<String>> optSubject;
    @Inject
    @Claim("auth_time")
    private ClaimValue<Optional<Long>> authTime;
    @Inject
    @Claim("custom-missing")
    private ClaimValue<Optional<Long>> custom;
    @Inject
    @Claim("customString")
    private ClaimValue<String> customString;
    @Inject
    @Claim("customInteger")
    private ClaimValue<JsonNumber> customInteger;
    @Inject
    @Claim("customDouble")
    private ClaimValue<JsonNumber> customDouble;
    @Inject
    @Claim("customBoolean")
    private ClaimValue<Boolean> customBoolean;

    @Inject
    @Claim(standard = Claims.raw_token)
    private ClaimValue<String> rawTokenStandard;
    @Inject
    @Claim(standard = Claims.iss)
    private ClaimValue<String> issuerStandard;
    @Inject
    @Claim(standard = Claims.jti)
    private ClaimValue<String> jtiStandard;
    @Inject
    @Claim(standard = Claims.aud)
    private ClaimValue<Set<String>> audStandard;
    @Inject
    @Claim(standard = Claims.iat)
    private ClaimValue<Long> issuedAtStandard;
    @Inject
    @Claim(standard = Claims.sub)
    private ClaimValue<String> subStandard;
    @Inject
    @Claim(standard = Claims.auth_time)
    private ClaimValue<Long> authTimeStandard;

    @GET
    @Path("/verifyInjectedIssuer")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedIssuer(@QueryParam("iss") String iss) {
        boolean pass = false;
        String msg;
        String issValue = issuer.getValue();
        if (issValue == null || issValue.length() == 0) {
            msg = Claims.iss.name() + "value is null or empty, FAIL";
        } else if (issValue.equals(iss)) {
            msg = Claims.iss.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.iss.name(), issValue, iss);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }
    @GET
    @Path("/verifyInjectedIssuerStandard")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedIssuerStandard(@QueryParam("iss") String iss) {
        boolean pass = false;
        String msg;
        String issValue = issuerStandard.getValue();
        if (issValue == null || issValue.length() == 0) {
            msg = Claims.iss.name() + "value is null or empty, FAIL";
        } else if (issValue.equals(iss)) {
            msg = Claims.iss.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.iss.name(), issValue, iss);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedRawToken")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedRawToken(@QueryParam("raw_token") String rt) {
        boolean pass = false;
        String msg;
        // raw_token
        String rawTokenValue = rawToken.getValue();
        if (rawTokenValue == null || rawTokenValue.length() == 0) {
            msg = Claims.raw_token.name() + "value is null or empty, FAIL";
        } else if (rawTokenValue.equals(rt)) {
            msg = Claims.raw_token.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.raw_token.name(), rawTokenValue, rt);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }
    @GET
    @Path("/verifyInjectedRawTokenStandard")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedRawTokenStandard(@QueryParam("raw_token") String rt) {
        boolean pass = false;
        String msg;
        // raw_token
        String rawTokenValue = rawTokenStandard.getValue();
        if (rawTokenValue == null || rawTokenValue.length() == 0) {
            msg = Claims.raw_token.name() + "value is null or empty, FAIL";
        } else if (rawTokenValue.equals(rt)) {
            msg = Claims.raw_token.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.raw_token.name(), rawTokenValue, rt);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedJTI")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedJTI(@QueryParam("jti") String jwtID) {
        boolean pass = false;
        String msg;
        // jti
        String jtiValue = jti.getValue();
        if (jtiValue == null || jtiValue.length() == 0) {
            msg = Claims.jti.name() + "value is null or empty, FAIL";
        } else if (jtiValue.equals(jwtID)) {
            msg = Claims.jti.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.jti.name(), jtiValue, jwtID);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }
    @GET
    @Path("/verifyInjectedJTIStandard")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedJTIStandard(@QueryParam("jti") String jwtID) {
        boolean pass = false;
        String msg;
        // jti
        String jtiValue = jtiStandard.getValue();
        if (jtiValue == null || jtiValue.length() == 0) {
            msg = Claims.jti.name() + "value is null or empty, FAIL";
        } else if (jtiValue.equals(jwtID)) {
            msg = Claims.jti.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.jti.name(), jtiValue, jwtID);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedAudience")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedAudience(@QueryParam("aud") String audience) {
        boolean pass = false;
        String msg;
        // aud
        Set<String> audValue = aud.getValue();
        if (audValue == null || audValue.size() == 0) {
            msg = Claims.aud.name() + "value is null or empty, FAIL";
        } else if (audValue.contains(audience)) {
            msg = Claims.aud.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.aud.name(), audValue, audience);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }
    @GET
    @Path("/verifyInjectedAudienceStandard")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedAudienceStandard(@QueryParam("aud") String audience) {
        boolean pass = false;
        String msg;
        // aud
        Set<String> audValue = audStandard.getValue();
        if (audValue == null || audValue.size() == 0) {
            msg = Claims.aud.name() + "value is null or empty, FAIL";
        } else if (audValue.contains(audience)) {
            msg = Claims.aud.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.aud.name(), audValue, audience);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedIssuedAt")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedIssuedAt(@QueryParam("iat") Long iat) {
        boolean pass = false;
        String msg;
        // iat
        Long iatValue = issuedAt.getValue();
        if (iatValue == null || iatValue.intValue() == 0) {
            msg = Claims.iat.name() + "value is null or empty, FAIL";
        } else if (iatValue.equals(iat)) {
            msg = Claims.iat.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.iat.name(), iatValue, iat);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }
    @GET
    @Path("/verifyInjectedIssuedAtStandard")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedIssuedAtStandard(@QueryParam("iat") Long iat) {
        boolean pass = false;
        String msg;
        // iat
        Long iatValue = issuedAtStandard.getValue();
        if (iatValue == null || iatValue.intValue() == 0) {
            msg = Claims.iat.name() + "value is null or empty, FAIL";
        } else if (iatValue.equals(iat)) {
            msg = Claims.iat.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.iat.name(), iatValue, iat);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedOptionalSubject")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedOptionalSubject(@QueryParam("sub") String subject) {
        boolean pass = false;
        String msg;
        // sub
        Optional<String> optSubValue = optSubject.getValue();
        if (optSubValue == null || !optSubValue.isPresent()) {
            msg = Claims.sub.name() + " value is null or missing, FAIL";
        } else if (optSubValue.get().equals(subject)) {
            msg = Claims.sub.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.sub.name(), optSubValue, subject);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }
    @GET
    @Path("/verifyInjectedSubjectStandard")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedSubjectStandard(@QueryParam("sub") String subject) {
        boolean pass = false;
        String msg;
        // sub
        String subValue = subStandard.getValue();
        if (subValue == null || subValue.length() == 0) {
            msg = Claims.sub.name() + " value is null or missing, FAIL";
        } else if (subValue.equals(subject)) {
            msg = Claims.sub.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.sub.name(), subValue, subject);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedOptionalAuthTime")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedOptionalAuthTime(@QueryParam("auth_time") Long authTime) {
        boolean pass = false;
        String msg;
        // auth_time
        Optional<Long> optAuthTimeValue = this.authTime.getValue();
        if (optAuthTimeValue == null || !optAuthTimeValue.isPresent()) {
            msg = Claims.auth_time.name() + " value is null or missing, FAIL";
        } else if (optAuthTimeValue.get().equals(authTime)) {
            msg = Claims.auth_time.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.auth_time.name(), optAuthTimeValue, authTime);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }
    @GET
    @Path("/verifyInjectedAuthTimeStandard")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedAuthTimeStandard(@QueryParam("auth_time") Long authTime) {
        boolean pass = false;
        String msg;
        // auth_time
        Long authTimeValue = authTimeStandard.getValue();
        if (authTimeValue == null || authTimeValue == 0) {
            msg = Claims.auth_time.name() + " value is null or zero, FAIL";
        } else if (authTimeValue.equals(authTime)) {
            msg = Claims.auth_time.name() + " PASS";
            pass = true;
        } else {
            msg = String.format("%s: %s != %s", Claims.auth_time.name(), authTimeValue, authTime);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    /**
     * Verify that values exist and that types match the corresponding Claims enum
     * 
     * @return a series of pass/fail statements regarding the check for each injected claim
     */
    @GET
    @Path("/verifyInjectedOptionalCustomMissing")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedOptionalCustomMissing() {
        boolean pass = false;
        String msg;
        // custom-missing
        Optional<Long> customValue = custom.getValue();
        if (customValue == null) {
            msg = "custom-missing value is null, FAIL";
        } else if (!customValue.isPresent()) {
            msg = "custom-missing PASS";
            pass = true;
        } else {
            msg = String.format("custom: %s != %s", null, customValue.get());
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedCustomString")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedCustomString(@QueryParam("value") String value) {
        boolean pass = false;
        String msg;
        String customValue = customString.getValue();
        if (customValue == null || customValue.length() == 0) {
            msg = "customString value is null or empty, FAIL";
        } else if (customValue.equals(value)) {
            msg = "customString PASS";
            pass = true;
        } else {
            msg = String.format("customString: %s != %s", customValue, value);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedCustomInteger")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedCustomInteger(@QueryParam("value") Long value) {
        boolean pass = false;
        String msg;
        Object test = customInteger.getValue();
        System.out.printf("+++ verifyInjectedCustomInteger, JsonNumber.class.CL: %s\n",
                JsonNumber.class.getClassLoader());
        System.out.printf("+++ customInteger.CL: %s\n",
                test.getClass().getClassLoader());
        Class[] ifaces = test.getClass().getInterfaces();
        for (Class iface : ifaces) {
            System.out.printf("%s: %s\n", iface, iface.getClassLoader());
        }
        JsonNumber customValue = JsonNumber.class.cast(test);
        if (customValue == null || customValue.isIntegral() == false) {
            msg = "customInteger value is null or not integral, FAIL";
        } else if (customValue.longValueExact() == value) {
            msg = "customInteger PASS";
            pass = true;
        } else {
            msg = String.format("customInteger: %d != %d", customValue, value);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedCustomDouble")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedCustomDouble(@QueryParam("value") Double value) {
        boolean pass = false;
        String msg;
        JsonNumber customValue = customDouble.getValue();
        if (customValue == null) {
            msg = "customDouble value is null, FAIL";
        } else if (Math.abs(customValue.doubleValue() - value.doubleValue()) < 0.00001) {
            msg = "customDouble PASS";
            pass = true;
        } else {
            msg = String.format("customDouble: %s != %.8f", customValue, value);
        }
        JsonObject result = Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
        return result;
    }

    @GET
    @Path("/verifyInjectedCustomBoolean")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject verifyInjectedCustomBoolean(@QueryParam("value") String value) {
        boolean pass = false;
        String msg;
        final Boolean customValue = customBoolean.getValue();
        if (customValue == null) {
            msg = "customBoolean value is null, FAIL";
        } else if (customValue == Boolean.valueOf(value)) {
            msg = "customBoolean PASS";
            pass = true;
        } else {
            msg = String.format("customBoolean: %s != %s", customValue, value);
        }
        return Json.createObjectBuilder()
                .add("pass", pass)
                .add("msg", msg)
                .build();
    }
}
