package org.kuali.ole.loader.test

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

import org.kuali.ole.contrib.client.ClientSupport
import org.kuali.ole.contrib.client.ObjectType
import edu.ncsu.lib.ole.test.JsonValidator
import groovy.json.JsonBuilder
import spock.lang.Shared
import spock.lang.Specification

/**
 * Specification for the JSON-based OLE loader for locations.
 *
 * Currently these are the only tests available, but tests for the other
 * parts of the API will be very similar.
 *
 * @author Adam Constabaris
 */
class LocationLoaderSpecification extends Specification {
	
	
	ClientSupport client
	
	@Shared
	ConfigObject config
	
	@Shared
	JsonValidator validator
	
	def setup() {
		config = new ConfigSlurper().parse( getClass().getResource("/dataTypes.groovy") )
		client = new ClientSupport((String)config.dataTypes.apiBase)
		validator = new JsonValidator()
	}
	
	
	def "test GET request to base URL"() {
		when:
			def result = client.fetchCollection(ObjectType.LOCATION)
		then:
            result.status == 200
			result.JSON.containsKey("@context")
            result.JSON.'@context' == ObjectType.LOCATION.contextURI
	}


    def "Response to GET (location.id == 1) validates"() {
        // this should be parameterized, or run after creating a new object in a known
        // state.
        when:
            def result = client.doGet(ObjectType.LOCATION, 1)
        then:
            result.status == 200
            validator.validateObject( ObjectType.LOCATION.objectName, result.getText() ).success
    }

    def "test POST request to collection #client.URL"() {
        setup:
            // ideally, one would be able to look up the @id for the relevant location level
            // and for the parent location.  So I do not currently expect this to work
            // as-is, but it illustrates the basic idea -- AJC
            def object = [ name: "New Location", code: "MKRSPACE", level: [ '@id' : "${config.apiBase}/${ObjectType.LOCATION_LEVEL.objectName}/5".toString(), code:"SHELVING"] ]
            def processed = new JsonBuilder(object).toPrettyString()
        when:
            def result = client.doPost(ObjectType.LOCATION, null, processed)
        then:
            result.status in [ 200, 201, 204 ]
            '@id' in result.JSON
    }

}
