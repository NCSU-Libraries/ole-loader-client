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

import groovy.json.JsonSlurper

import javax.ws.rs.core.Response

/**
 * Groovy Extension that simplifies getting JSON and text of JAX-RS client
 * responses.
 *
 * @author Adam Constabaris
 */
class ResponseExtension {

    /**
     * Gets the contents of the response entity as a groovy data structure.
     * <p>
     *     Note: internally calls <code>bufferEntitye()</code> on the response
     *     so the method can be invoked multiple times with the same result.
     * </p>
     * @param response
     * @return the content of the response, processed into a groovy object (map or list)
     * @see JsonSlurper
     */
    public static Object getJSON(Response response) {
        assert response.bufferEntity()
        return new JsonSlurper()
                .parseText(
                    response.readEntity(String.class)
        )
    }

    /**
     * Gets the contest of the response entity as a String.
     * @param response
     * @return
     */
    public static String getText(Response response) {
        assert response.bufferEntity(), "Entity is not buffered, so refusing to read response"
        return response.readEntity(String.class)
    }
}
