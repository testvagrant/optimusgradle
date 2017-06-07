package com.testvagrant.optimus.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.JsonSchema
import com.github.fge.jsonschema.main.JsonSchemaFactory
import org.apache.commons.io.IOUtils
import org.junit.Test

/**
 * Created by abhishek on 05/06/17.
 */
class JsonValidator {
    public static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
    public static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "\$schema";

    static void validateTestFeed(String testFeed) throws IOException, ProcessingException {
        String testFeedJson = new File("src/test/resources/" + testFeed).text
        String schemaJson = getAppJson("testFeedSchema.json")

        final JsonSchema schemaNode = getSchemaNode(schemaJson);
        final JsonNode jsonNode = getJsonNode(testFeedJson);

        ProcessingReport report = schemaNode.validate(jsonNode);
        if (!report.isSuccess()) {
            throw new RuntimeException(testFeed + " is invalid!\n" +
                    report.toString())
        }
    }


    static String getAppJson(String fileName) {
        String result = "";
        ClassLoader classLoader = JsonValidator.class.getClassLoader()
        try {
            InputStream resourceAsStream = classLoader.getResourceAsStream(fileName)
            result = IOUtils.toString(resourceAsStream)
        } catch (FileNotFoundException f) {
            throw new RuntimeException("File not found exception")
        } catch (IOException e) {
            e.printStackTrace()
        }

        return result
    }


    static JsonNode getJsonNode(String jsonFile)
            throws IOException {
        return JsonLoader.fromString(jsonFile)
    }

    static JsonSchema getSchemaNode(String schemaText)
            throws IOException, ProcessingException {
        final JsonNode schemaNode = getJsonNode(schemaText);
        return _getSchemaNode(schemaNode);
    }

    private static JsonSchema _getSchemaNode(JsonNode jsonNode)
            throws ProcessingException {
        final JsonNode schemaIdentifier = jsonNode.get(JSON_SCHEMA_IDENTIFIER_ELEMENT);
        if (null == schemaIdentifier) {
            ((ObjectNode) jsonNode).put(JSON_SCHEMA_IDENTIFIER_ELEMENT, JSON_V4_SCHEMA_IDENTIFIER);
        }

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        return factory.getJsonSchema(jsonNode);
    }
}
