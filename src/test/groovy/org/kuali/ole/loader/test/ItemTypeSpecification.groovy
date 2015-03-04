package org.kuali.ole.loader.test

import edu.ncsu.lib.ole.test.JsonValidator
import groovy.json.JsonBuilder
import org.kuali.ole.contrib.client.ClientSupport
import org.kuali.ole.contrib.client.ObjectType
import spock.lang.Shared
import spock.lang.Specification

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
import spock.lang.Stepwise

/**
 * Specification for loading item types.
 *
 * @author Adam Constabaris
 */
@Stepwise
class ItemTypeSpecification extends Specification {


    ClientSupport client


    @Shared
    String newCode

   	@Shared
   	ConfigObject config

   	@Shared
   	JsonValidator validator

   	def setup() {
   		config = new ConfigSlurper().parse( getClass().getResource("/dataTypes.groovy") )
   		client = new ClientSupport((String)config.dataTypes.apiBase)
   		validator = new JsonValidator()
   	}

    // because of the stepwise annotation, this should run first.  If
    // it fails, so will most of the tests below
    def "POST to collection URI creates new item type"() {
        when:
            // do our best to create a random new code ...
            newCode = "${UUID.randomUUID().toString().substring(12)}".toString()
            def newItemType = [ code: "FLAMMERY- }" ]
            def result = client.doPost(ObjectType.ITEM_TYPE, new JsonBuilder(newItemType).toString())
        then:
            result.status in [ 200, 201, 204 ]
            result.JSON.containsKey('@context')
            result.JSON.'@context' == ObjectType.ITEM_TYPE.contextURI
            result.JSON.'@id' != null
    }

   	def "test GET request to base URL"() {
   		when:
   			def result = client.fetchCollection(ObjectType.ITEM_TYPE)
   		then:
            result.status == 200
   			result.JSON.containsKey("@context")
            result.JSON.'@context' == ObjectType.ITEM_TYPE.contextURI
            result.JSON.items.find { it.code == newCode }
   	}
}
