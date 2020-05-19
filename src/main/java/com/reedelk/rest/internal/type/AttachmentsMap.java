package com.reedelk.rest.internal.type;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.message.content.Attachment;

import java.util.HashMap;

@Type(displayName = "Map<String,Attachment>",
        mapKeyType = String.class,
        mapValueType = Attachment.class)
public class AttachmentsMap extends HashMap<String, Attachment> {
}
