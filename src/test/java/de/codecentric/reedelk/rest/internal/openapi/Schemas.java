package de.codecentric.reedelk.rest.internal.openapi;

public enum Schemas implements JsonProvider {

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
