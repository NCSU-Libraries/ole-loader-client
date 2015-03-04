package org.kuali.ole.contrib.client.exception;

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
import javax.ws.rs.core.Response;

/**
 * Exception indicating that the server's response to a request involves an error of
 * some sort.
 *
 * For example, if the HTTP status code in response to an operation is unexpected
 * (404, 400, etc.), or the response format is incorrect (e.g. JSON object is
 * missing required elements)
 *
 * @author Adam Constabaris
 */
public class ServerResponseException extends RemoteAccessException {


    private Response response;

    public ServerResponseException(String message, Response response) {
        super(message);
        this.response = response;
    }

    /**
     * Gets the response on which this exception is based.
     * @return
     */
    public Response getResponse() {
        return response;
    }
}
