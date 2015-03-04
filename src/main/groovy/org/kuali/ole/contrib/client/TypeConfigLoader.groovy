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
/**
 * Loads configuration for types.
 *
 * TODO -- is this still necessary now that we have the ObjectType class?
 * @author Adam Constabaris
 */
class TypeConfigLoader {

    private ConfigObject config

    def types = new TreeMap<String, Object>({
        String a,String b -> return a.compareToIgnoreCase(b)
    })



    def TypeConfigLoader(String environment = "production") {
        config = new ConfigSlurper(environment).parse( getClass().getResource("/dataTypes.groovy") )
        initTypes()
    }


    def initTypes() {
        def typeData = config.dataTypes
        typeData.types.each {
            String type, data ->
                def entry = [:]
                entry.contextURL = typeData.contextURL ?: "${typeData.jsonLDBase}/${type}.jsonld".toString()
                entry.baseURL = "${typeData.apiBase}/${type}"
                types[type] = entry
        }
    }

    public static void main(String[] args) {
        println new TypeConfigLoader().types
    }
}
