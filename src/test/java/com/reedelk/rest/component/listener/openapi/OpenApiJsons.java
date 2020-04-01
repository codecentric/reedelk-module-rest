package com.reedelk.rest.component.listener.openapi;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class OpenApiJsons {

    public enum InfoObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "info_object_with_all_properties.json";
            }
        },

        WithDefaultProperties() {
            @Override
            public String path() {
                return "info_object_with_default_properties.json";
            }
        }
    }

    public enum LicenseObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "license_object_with_all_properties.json";
            }
        },

        WithDefaultProperties() {
            @Override
            public String path() {
                return "license_object_with_default_properties.json";
            }
        }
    }

    public enum ContactObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "contact_object_with_all_properties.json";
            }
        },

        WithDefaultProperties() {
            @Override
            public String path() {
                return "contact_object_with_default_properties.json";
            }
        }
    }

    public enum ServerObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "server_object_with_all_properties.json";
            }
        },

        WithDefaultProperties() {
            @Override
            public String path() {
                return "server_object_with_default_properties.json";
            }
        },
    }

    public enum ServerVariableObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "server_variable_object_with_all_properties.json";
            }
        },

        WithDefaultProperties() {
            @Override
            public String path() {
                return "server_variable_object_with_default_properties.json";
            }
        },
    }

    public enum OpenApiObject implements Provider {

        WithDefaultInfoAndServersAndPaths() {
            @Override
            public String path() {
                return "open_api_object_with_default_info_and_servers_and_paths.json";
            }
        }
    }

    public enum PathsObject implements Provider {

        WithDefaultPaths() {
            @Override
            public String path() {
                return "paths_object_with_default.json";
            }
        },

        WithDefaultOperation() {
            @Override
            public String path() {
                return "paths_object_with_default_operation.json";
            }
        },

        WithOperation() {
            @Override
            public String path() {
                return "paths_object_with_operation.json";
            }
        },

        WithOperationWithNullPath() {
            @Override
            public String path() {
                return "paths_object_with_operation_null_path.json";
            }
        }
    }

    public enum ComponentsObject implements Provider {

        WithNoSchemas() {
            @Override
            public String path() {
                return "components_object_with_no_schemas.json";
            }
        },

        WithSampleSchemas() {
            @Override
            public String path() {
                return "components_object_with_sample_schemas.json";
            }
        }
    }

    public enum HeaderObject implements Provider {

        WithDefaults() {
            @Override
            public String path() {
                return "header_object_with_defaults.json";
            }
        },

        WithAllPropertiesAndDefaultSchema() {
            @Override
            public String path() {
                return "header_object_with_all_properties_and_default_schema.json";
            }
        },

        WithAllPropertiesAndCustomSchema() {
            @Override
            public String path() {
                return "header_object_with_all_properties_and_custom_schema.json";
            }
        }
    }

    public enum MediaTypeObject implements Provider {

        WithSchema() {
            @Override
            public String path() {
                return "media_type_object_with_schema.json";
            }
        },

        WithExample() {
            @Override
            public String path() {
                return "media_type_object_with_example.json";
            }
        },

        WithSchemaAndExample() {
            @Override
            public String path() {
                return "media_type_object_with_schema_and_example.json";
            }
        },

        WithDefault() {
            @Override
            public String path() {
                return "media_type_with_default.json";
            }
        }
    }

    public enum OperationObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "operation_object_with_all_properties.json";
            }
        },

        WithDefault() {
            @Override
            public String path() {
                return "operation_object_with_default.json";
            }
        }
    }

    public enum ParameterObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "parameter_object_with_all_properties.json";
            }
        },

        WithDefault() {
            @Override
            public String path() {
                return "parameter_object_with_default.json";
            }
        },

        WithInPath() {
            @Override
            public String path() {
                return "parameter_object_with_in_path.json";
            }
        }
    }

    public enum RequestBodyObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "request_body_object_with_all_properties.json";
            }
        },

        WithDefault() {
            @Override
            public String path() {
                return "request_body_object_with_default.json";
            }
        }

    }

    public enum ResponseBodyObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "response_body_object_with_all_properties.json";
            }
        },

        WithDefault() {
            @Override
            public String path() {
                return "response_body_object_with_default.json";
            }
        }
    }

    public enum Schemas implements Provider {

        Pet() {
            @Override
            public String path() {
                return "pet.schema.json";
            }
        },

        Coordinates() {
            @Override
            public String path() {
                return "coordinates.schema.json";
            }
        }
    }

    public enum Examples implements Provider {

        OpenApiEmpty() {
            @Override
            public String path() {
                return "open_api_empty.json";
            }
        },

        OpenApiWithPathAndMethod() {
            @Override
            public String path() {
                return "open_api_with_path_and_method.json";
            }
        },

        OpenApiWithPathAndMethodAndHeaders() {
            @Override
            public String path() {
                return "open_api_with_path_and_method_and_headers.json";
            }
        },

        OpenApiWithPathAndMethodAndHeadersError() {
            @Override
            public String path() {
                return "open_api_with_path_and_method_and_headers_error.json";
            }
        },

        OpenApiWithPathParams() {
            @Override
            public String path() {
                return "open_api_with_path_params.json";
            }
        },

        OpenApiWithOverriddenPathParams() {
            @Override
            public String path() {
                return "open_api_with_overridden_path_params.json";
            }
        },

        OpenApiWithSchemas() {
            @Override
            public String path() {
                return "open_api_with_schemas.json";
            }
        },

        JsonPet() {
            @Override
            public String path() {
                return "pet.json";
            }
        },

        NoteXml() {
            @Override
            public String path() {
                return "note.xml";
            }
        }
    }

    interface Provider {

        String path();

        default URL url() {
            return OpenApiJsons.class.getResource(path());
        }

        default String string() {
            try (Scanner scanner = new Scanner(url().openStream(), UTF_8.toString())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            } catch (IOException e) {
                throw new RuntimeException("String from URI could not be read.", e);
            }
        }
    }
}
