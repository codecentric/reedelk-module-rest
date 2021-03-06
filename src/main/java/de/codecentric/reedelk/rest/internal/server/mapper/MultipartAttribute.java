package de.codecentric.reedelk.rest.internal.server.mapper;

import de.codecentric.reedelk.rest.internal.commons.HttpHeader;

public interface MultipartAttribute {

    String FILE_NAME = "filename";

    String CONTENT_TYPE = HttpHeader.CONTENT_TYPE;

    String TRANSFER_ENCODING = HttpHeader.TRANSFER_ENCODING;

}
