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

import groovy.transform.Memoized

/**
 * Groovy trait for JSON reponses that support paging.  Can add this at runtime to
 * a class containing a <code>jsonObject</code> resulting from processing a Collection
 * API response, and will allow for accessing 'next' and 'prev' links.
 *
 * @author Adam Constabaris
 */
trait PagedJSONResponse {

    /**
     * Gets the URL for the next page, or the empty string if there isn't one.
     * @return
     */
    @Memoized
    String nextPage() {
        if ('links' in jsonObject) {
            return jsonObject.links.find {
                it.rel == 'next'
            }.href
        }
        ""
    }

    /**
     * Gets teh url for the previous page of results, or the empty string if there
     * isn't one.
     * @return
     */

    @Memoized
    String prevPage() {
        if ('links' in jsonObject) {
            return jsonObject.links.find {
                it.rel == 'prev'
            }.href
        }
        ""
    }
}
