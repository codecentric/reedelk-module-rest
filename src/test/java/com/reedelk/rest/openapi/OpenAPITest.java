package com.reedelk.rest.openapi;

import com.reedelk.rest.openapi.components.Components;
import com.reedelk.rest.openapi.components.SchemaObject;
import com.reedelk.rest.openapi.info.InfoObject;
import com.reedelk.rest.openapi.paths.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

class OpenAPITest {

    @Test
    void shouldCorrectlyCreateDocument() {
        // Given
        OpenAPI openapi = new OpenAPI();
        openapi.setPaths(createPaths());
        openapi.setInfo(createInfoObject());
        openapi.setComponents(createComponents());

        JSONObject serialize = openapi.serialize();
        String actual = serialize.toString(2);

        String expected = JSONS.SampleOpenAPI.string();

        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    private Components createComponents() {
        Components components = new Components();
        SchemaObject schemaObject = new SchemaObject();
        schemaObject.setSchema("{\n" +
                "        \"type\": \"object\",\n" +
                "        \"discriminator\": {\n" +
                "          \"propertyName\": \"petType\"\n" +
                "        },\n" +
                "        \"properties\": {\n" +
                "          \"name\": {\n" +
                "            \"type\": \"string\"\n" +
                "          },\n" +
                "          \"petType\": {\n" +
                "            \"type\": \"string\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"required\": [\n" +
                "          \"name\",\n" +
                "          \"petType\"\n" +
                "        ]\n" +
                "      }");
        components.add("Pet", schemaObject);
        return components;
    }

    private Paths createPaths() {
        Paths paths = new Paths();
        paths.add("/", createPathItemObject());
        return paths;
    }

    private PathItemObject createPathItemObject() {
        PathItemObject pathItemObject = new PathItemObject();
        pathItemObject.setGet(createGetOperation());
        return pathItemObject;
    }

    private OperationObject createGetOperation() {
        OperationObject getOperation = new OperationObject();
        getOperation.setDescription("List API versions");
        getOperation.setOperationId("listVersionsv2");
        getOperation.setResponses(createResponses());
        return getOperation;
    }

    private ResponsesObject createResponses() {
        ResponsesObject responsesObject = new ResponsesObject();
        responsesObject.add("200", createResponseObject());
        return responsesObject;
    }

    private ResponseObject createResponseObject() {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setDescription("200 response");
        responseObject.add("application/json", mediaTypeObject());
        return responseObject;
    }

    private MediaTypeObject mediaTypeObject() {
        MediaTypeObject mediaTypeObject = new MediaTypeObject();
        mediaTypeObject.setSchema(referenceObject());
        return mediaTypeObject;
    }

    private ReferenceObject referenceObject() {
        ReferenceObject referenceObject = new ReferenceObject();
        referenceObject.set$ref("#/components/schemas/Pet");
        return referenceObject;
    }

    private InfoObject createInfoObject() {
        InfoObject infoObject = new InfoObject();
        infoObject.setTitle("Simple API overview");
        infoObject.setDescription("Simple API overview description");
        infoObject.setVersion("v2");
        return infoObject;
    }
}
