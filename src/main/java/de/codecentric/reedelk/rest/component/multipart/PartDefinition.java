package de.codecentric.reedelk.rest.component.multipart;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
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
    @Example(MimeType.AsString.APPLICATION_BINARY)
    @DefaultValue(MimeType.AsString.APPLICATION_BINARY)
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
