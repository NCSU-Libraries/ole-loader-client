package org.kuali.ole.contrib.client

/*

     Copyright (C) 2015 North Carolina State University

     This program is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 3 of the License, or
     (at your option) any later version.

     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.

     You should have received a copy of the GNU General Public License
     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import groovy.json.JsonBuilder

import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriBuilder

/**
 * Wrapper for JAX-RS client to simplify processing JSON responses.
 */
class ClientSupport {


    private Client client

    private URI baseURI

    private String collectionBase

    private String objectTemplate

    def ClientSupport(String base= "http://localhost:8080/olefs/api", Client client = null) {
        if ( client != null ) {
            this.client = client;
        } else {
            this.client = ClientBuilder.newClient()
        }
        this.baseURI = URI.create(base)
        this.collectionBase = UriBuilder.fromUri(this.baseURI.toString() + "/{objectType}").toTemplate()
        this.objectTemplate = UriBuilder.fromUri(collectionBase + "/{objectId}").toTemplate()
    }



    Response fetchCollection(ObjectType type) {
        def uri = UriBuilder.fromUri(collectionBase).build(type.objectName)
        fetchJSON(uri)
    }

    Response doGet(ObjectType type, Object specifier) {
        def uri = UriBuilder.fromUri(objectTemplate).build(type.objectName,specifier)
        fetchJSON(uri)
    }

	
	Response fetchJSON( URI uri ) {
		Response resp = client.target(uri)
		.request()
		.accept(MediaType.APPLICATION_JSON_TYPE)
		.accept("application/ld+json")

		.buildGet()
		.invoke()
		assert resp.bufferEntity()
		return resp
	}

    /**
     * Gets the JAX-RS Response from fetching a remote URI that is
     * expected to return JSON or JSON-LD.
     * @param uri the URI of the JSON feed.
     * @return an enhanced Response object that includes a getJSON()
     * method for inspecting the response entity as a Groovy data structure
     * @see ResponseExtension#getJSON(Response)
     */
    Response fetchJSON( String uri ) {
		return fetchJSON( URI.create(uri) )
	}

    /**
     * POSTs an individual object for creation or update.
     * @param objectType the type of object being updated.
     * @param body the JSON body
     * @param id
     * @return
     */
    def doPost(ObjectType objectType, String body, String id = null) {
        if ( id ) {
            doPost( UriBuilder.fromUri(objectTemplate).build(objectType.objectName, id), body)
        } else {
            doPost( UriBuilder.fromUri(collectionBase).build(objectType.objectName), body)
        }
    }


    /**
     * Posts JSON to a specified URI
     * @param uri the URI target for the POST operation.
     * @param body the
     * @return
     */
    def doPost(URI uri , String body) {
        return doPost(uri, new ByteArrayInputStream(body.getBytes("utf-8")))
    }

    def doPost(URI uri, InputStream data) {
        client.target(uri)
            .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.TEXT_PLAIN_TYPE)
                .buildPost(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE))
                .invoke()
    }


    def doDelete(String uri) {
        client.target(uri)
            .request()
            .accept(MediaType.TEXT_PLAIN_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .buildDelete()
                .invoke()
    }

    /**
     * Submits a PATCH request with a JSON object derived from a supplied map.
     * @param uri the URI of the entity to be patched.
     * @param params the properties to be updated, as a Map
     * @return the response from the server to the PATCH request
     */
    def doPatch(String uri, Map params) {
        def json = new JsonBuilder()
        def body = json(params).toString()
        def resp = client.target(uri)
            .request()
            .accept(MediaType.APPLICATION_JSON_TYPE)
            .accept(MediaType.TEXT_PLAIN_TYPE)
            .build("PATCH", Entity.entity(body, MediaType.APPLICATION_JSON_TYPE) )
            .invoke()
        return resp

    }
}
