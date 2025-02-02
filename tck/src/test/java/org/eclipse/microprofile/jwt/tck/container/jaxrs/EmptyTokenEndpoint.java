/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/endp")
public class EmptyTokenEndpoint {
    @Inject
    private JsonWebToken jsonWebToken;

    @GET
    @Path("/verifyEmptyToken")
    public JsonObject verifyEmptyToken() {
        boolean pass = jsonWebToken.getName() == null &&
                jsonWebToken.getClaimNames() == null &&
                jsonWebToken.getClaim("") == null &&
                jsonWebToken.getRawToken() == null;

        return Json.createObjectBuilder().add("pass", pass).build();
    }

    @GET
    @Path("/verifyNonEmptyToken")
    public JsonObject verifyNonEmptyToken() {
        boolean pass = jsonWebToken.getName() != null &&
                jsonWebToken.getClaimNames() != null &&
                jsonWebToken.getRawToken() != null;

        return Json.createObjectBuilder().add("pass", pass).build();
    }
}
