package edu.ncsu.lib.ole.test
import groovy.json.JsonSlurper
import org.kuali.ole.contrib.client.ObjectType

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
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Unit tests for OLE Loader JSON schemas.
 * <p>
 * If these tests fail, then
 * any results from testing the APIs themselves will be suspect.
 * </p>
 * @author Adam Constabaris
 */
class JSONSchemaSpecification extends Specification {

    def "Basic JSON Schema validation works"() {
        setup:
            def validator = new JsonValidator()
        when:
            def locationLevels = getClass().getResourceAsStream("/examples/locations.json")
            def result = validator.validateDocument("collection", locationLevels)
        then:
            assert result.success : result
    }

    def "Loading JSON Schema from multiple files"() {
        setup:
            def validator = new JsonValidator()
        when:
            def locations = getClass().getResourceAsStream("/examples/singleLocation.json")
            def result = validator.validateObject(ObjectType.LOCATION, locations)
        then:
            assert result.success : result
    }


    def "Manual validation of location collection"() {
        setup:
            def resp = new FileLoader().readResponseFromFile("/examples/locations.json")
            def locations = new JsonSlurper().parseText(resp)
            def validator = new JsonValidator()
        expect:
            validator.validateDocument("collection", resp)
            locations.'@context' == ObjectType.LOCATION.contextURI
            locations.items.size() == 3
            [ true, true, true ] == locations.items.collect { locationItemIsValid(it) }
    }

    @Unroll
    def "test schema loading for #objectType.objectName syntax"() {
        setup:
            def validator = new JsonValidator()
        expect:
            validator.loadObjectSchema(objectType)
        where:
            objectType |  _
            ObjectType.BIB_STATUS | _
            ObjectType.CALL_NUMBER | _
            ObjectType.ITEM_AVAILABILITY_STATUS | _
            ObjectType.ITEM_TYPE | _
            ObjectType.LOCATION_LEVEL | _
            ObjectType.LOCATION | _
            ObjectType.STATISTICAL_SEARCH_CODE | _
    }


    // manual (non-schema-based) check
    static boolean locationItemIsValid(itemObject) {

            def failedTests = []

            [
                    "@context is present and valid OR absent" : { '@context' in itemObject ? itemObject.'@context' == CONTEXT  : true },
                    "code is present" : { itemObject.code },
                    "@id is present and a valid URI" : { itemObject.'@id' && {try { URI.create(itemObject.'@id') != null } catch( Exception e ) { false }}() },
                    "name is present" : { itemObject.name }
            ].each {
                testName, test ->
                    if ( !test() ) {
                        failedTests << testName
                    }
            }
            if ( failedTests ) { println itemObject; println failedTests }
            return failedTests.empty
        }
}
