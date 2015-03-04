package org.kuali.ole.contrib.client;

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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of known object types supported by the OLE loader API.
 *
 * @author Adam Constabaris
 */
public enum ObjectType {

    BIB_STATUS("bibStatus"),
    CALL_NUMBER("callNumberType"),
    ITEM_AVAILABILITY_STATUS("itemAvailabilityStatus"),
    ITEM_TYPE("itemType"),
    LOCATION("location"),
    LOCATION_LEVEL("locationLevel"),
    STATISTICAL_SEARCH_CODE("statSearchCode");

    private static final Map<String, ObjectType> contexts = new HashMap<>();


    private String objectName;

    private String contextURI;

    private ObjectType(String objectName) {
        this.objectName = objectName;
        this.contextURI = String.format("http://ole.kuali.org/standards/api/%s.jsonld", objectName);
    }


    public String getObjectName() {
        return objectName;
    }

    public static EnumSet<ObjectType> allTypes() {
        return EnumSet.allOf(ObjectType.class);
    }

    public String getContextURI() {
        return contextURI;
    }

    /**
     * Looks up an instance of this class by its context URI.
     * @param contextURI the URI to be looked up.
     * @return the enumerated value that maps to the context.
     * @throws IllegalArgumentException if the contextURI is unknown.
     */
    public static ObjectType findByContext( String contextURI ) {
        ObjectType result = contexts.get(contextURI);
        if ( result == null ) {
            throw new IllegalArgumentException("Context URI '" + contextURI + "' is an unknwon object type");
        }
        return result;

    }

}
