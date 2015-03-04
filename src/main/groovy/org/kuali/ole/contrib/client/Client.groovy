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
import org.kuali.ole.contrib.client.exception.ServerResponseException

import javax.ws.rs.core.Response

/**
 * Sample client for accessing OLE Loader APIs.  Provides an abstraction over
 * the HTTP operations.
 *
 * <p>Instances of this class are <em>not</em> threadsafe.
 *
 * @author Adam Constabaris
 */
class Client {

    private ObjectType defaultType

    private ClientSupport client

    private JsonBuilder json


    public Client(String baseURI, ObjectType defaultType = null) {
        this.defaultType = defaultType;
        client = new ClientSupport(base:baseURI)
    }

    /**
     * Fetches all of the objects of a given type.
     * @param defaultType the defaultType of object to be fetched.
     * @return
     */
    public List<?> getAll(ObjectType type = this.defaultType) {
        // nb this assumes no paging.
        def resp = client.fetchCollection(type)
        if ( resp.status != 200 ) {
            throw new ServerResponseException("Error response from server (${resp.status}", resp)
        }
        def responseObject = resp.getJSON()
        testResponse(resp, type)

        // determine database IDs and enrich any references with
        // data we can determine
        // TODO : fully resolve references.
        enrichResponse(responseObject).items
    }

    // expands references etc. in JSON response (response.getJSON())
    private Object enrichResponse( Object responseObject ) {
                // determine database IDs and enrich any references with
                // data we can determine
                // TODO : fully resolve references.
                responseObject.items.each {
                    item ->
                        item.databaseId = getId( it.'@id' )
                        item.each { key, value ->
                            if ( value instanceof Map ) {
                                // we have a reference.
                                value.objectType = ObjectType.findByContext(value.'@context')
                                value.databaseId = getId( value.'@id' )
                            }
                        }
                }
        responseObject
    }

    // throws an exception if the response doesn't "look right"
    private void testResponse(Response resp, ObjectType type) {
        def responseObject = resp.getJSON()
        if ( !responseObject.hasProperty("@context") || responseObject.'@context' != type.contextURI ) {
            throw new ServerResponseException("Response to query for '${type.objectName}' did not have expected context (${type.contextURI})", resp)
        }
        if ( !responseObject.hasProperty("items") ) {
            throw new ServerResponseException("Could not find items in server response", resp)
        }
    }

    /**
     * Creates new objects of the given type.
     * @param newObjects a list of objects to be createcd.
     * @param type the type of objects in newObjects.
     * @return the created objects response from the server.
     */
    public Object createNew(List<Map<String,?>> newObjects, ObjectType type = this.defaultType) {
        newObjects.each {
            it.'@context' = type.contextURI
        }
        def body = json(newObjects).toString()
        def resp = client.doPost(type, body)
        if ( !( resp.status in [ 200, 201, 204 ] ) ) {
            throw new ServerResponseException("Received HTTP ${resp.statusInfo} from server in response to POST", resp)
        }
        testResponse(resp, type)
        enrichResponse(resp.getJSON()).items
    }


    public Object createNew(Map<String, ?> newObject, ObjectType type = this.defaultType) {
        return createNew([newObject], type)[0]
    }

    // Extracts the database ID from a URI used by the API
    // URIs are epxected to end with <code>/{databaseId}</code>
    Long getId(String idURI) {
        return Long.parseLong(idURI.substring(idURI.lastIndexOf("/")+1), 10)
    }
}
