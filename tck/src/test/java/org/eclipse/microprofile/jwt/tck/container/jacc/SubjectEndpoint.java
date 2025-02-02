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
package org.eclipse.microprofile.jwt.tck.container.jacc;

import java.security.Principal;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/endp")
@DenyAll
@RequestScoped
public class SubjectEndpoint {
    @GET
    @Path("/getSubjectClass")
    @RolesAllowed("Tester")
    public String getSubjectClass(@Context SecurityContext sec) throws Exception {
        Subject subject = (Subject) PolicyContext.getContext("javax.security.auth.Subject.container");
        Set<? extends Principal> principalSet = subject.getPrincipals(JsonWebToken.class);
        if (principalSet.size() > 0) {
            return "subject.getPrincipals(JWTPrincipal.class) ok";
        }
        throw new IllegalStateException("subject.getPrincipals(JWTPrincipal.class) == 0");
    }
}
