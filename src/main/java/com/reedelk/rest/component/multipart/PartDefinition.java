package com.reedelk.rest.component.multipart;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = PartDefinition.class, scope = PROTOTYPE)
public class PartDefinition implements Implementor {

    @Property("Content")
    @InitValue("#[message.payload()]")
    @Hint("My part content")
    @Example("#[context.part1]")
    @Description("Sets the content of the current part object. " +
            "It could be a static (text only) value or a dynamic value.")
    private DynamicByteArray content;

    @Property("Mime type")
    @MimeTypeCombo
    @Example(MimeType.MIME_TYPE_APPLICATION_BINARY)
    @DefaultValue(MimeType.MIME_TYPE_APPLICATION_BINARY)
    @Description("Sets the mime type of the part object.")
    private String mimeType;

    @Property("Part Attributes")
    @TabGroup("Part Attributes")
    @KeyName("Attribute Name")
    @ValueName("Attribute Value")
    private DynamicStringMap attributes;

    public DynamicByteArray getContent() {
        return content;
    }

    public void setContent(DynamicByteArray content) {
        this.content = content;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public DynamicStringMap getAttributes() {
        return attributes;
    }

    public void setAttributes(DynamicStringMap attributes) {
        this.attributes = attributes;
    }
}
