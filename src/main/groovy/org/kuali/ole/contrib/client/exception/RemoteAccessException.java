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

/**
 *
 * Base exception class indicating server communication problem.
 *
 * <p>Generally, errors in accessing a remote service fall into two different
 * categories: ones where an exception is encountered during the communciation
 * process (e.g. unable tor reach the server), and ones where the communication
 * completes successfully, but there is something amiss with the <em>content</em>
 * of the communciation.  This class provides a base for both types of problem
 * but allows dealing with them in different manners.</p>
 *
 *
 *  @author Adam Constabaris
 */
public class RemoteAccessException extends RuntimeException {


    public RemoteAccessException(String message) {
        super(message);
    }

    public RemoteAccessException(String message, Throwable cause) {
        super(message,cause);
    }
}
