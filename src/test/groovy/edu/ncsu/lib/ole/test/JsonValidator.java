package edu.ncsu.lib.ole.test;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.kuali.ole.contrib.client.ObjectType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for loading JSON schemas and validating documents against them.
 */
public class JsonValidator {

    private ObjectMapper mapper = new ObjectMapper();

    final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

    /**
     * Cache of compiled schemas.
     */
    private final Map<String, JsonSchema> schemas = new ConcurrentHashMap<>();


    public ProcessingReport validateObject(ObjectType objectType, InputStream input) {
        try {
            return loadObjectSchema(objectType).validate(mapper.readTree(input));
        } catch (IOException iox) {
            throw new IllegalStateException("error reading input data", iox);
        } catch (ProcessingException px) {
            throw new IllegalStateException("Unable to process JSON", px);
        }

    }


    public ProcessingReport validateObject(ObjectType objectType, String document) {
        try {
            return loadObjectSchema(objectType).validate(loadFromString(document));
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public ProcessingReport validateDocument(String schemaName, String inputString) {
        return validateDocument( schemaName, new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8)));
    }


    public ProcessingReport validateDocument( String schemaName, InputStream input ) {
        try {
            return loadSchemaByName(schemaName).validate( mapper.readTree( input ) );
        } catch (ProcessingException e) {
            throw new RuntimeException("unable to process JSON from input", e);
        } catch (IOException e) {
            throw new IllegalStateException("unable to read content", e);
        }
    }




    JsonSchema loadSchemaByName(String schemaName) {
        if (!schemas.containsKey(schemaName)) {
            String path = "/schemas/" + schemaName + ".json";
            try {
                URI theURI = getClass().getResource(path).toURI();
                JsonSchema schema = factory.getJsonSchema(theURI.toString());
                schemas.put(schemaName, schema);
            } catch (ProcessingException e) {
                throw new IllegalStateException("Unable to read schema for " + schemaName, e);
            } catch (NullPointerException | URISyntaxException badx) {
                throw new IllegalStateException("Unable to load " + schemaName + " schema from classpath", badx);
            }
        }
        return schemas.get(schemaName);
    }

    public JsonSchema loadObjectSchema(ObjectType objectType) {
        return loadSchemaByName(objectType.getObjectName());

    }

    // reads a STring into a JsonNode (that can be validated, e.g.)
    private JsonNode loadFromString(String jsonData) throws IOException {
        return mapper.readTree(jsonData);
    }


}
