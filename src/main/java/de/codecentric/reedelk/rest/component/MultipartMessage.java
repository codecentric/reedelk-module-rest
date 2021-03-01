package de.codecentric.reedelk.rest.component;

import de.codecentric.reedelk.rest.component.multipart.PartDefinition;
import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.Attachment;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("Multipart Message")
@Description("The Multipart Message component creates a multipart message to be used as a payload of the REST Client. " +
        "A multipart message can be defined to contain text only or binary parts. " +
        "A not empty part name is mandatory for each part defined inside 'Multipart message parts' map. " +
        "Parts with an empty name will be ignored.")
@Component(service = MultipartMessage.class, scope = PROTOTYPE)
public class MultipartMessage implements ProcessorSync {

    @DialogTitle("Part Definition")
    @KeyName("Part Name")
    @ValueName("Part Definition")
    @Property("Multipart message parts")
    @TabGroup("Multipart message parts")
    @Description("A map containing mappings between part names and part definitions.")
    private Map<String, PartDefinition> parts = new HashMap<>();

    @Reference
    ScriptEngineService scriptEngine;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        Map<String, Attachment> allParts = new HashMap<>();
        Optional.ofNullable(parts).ifPresent(partDefinitionMap ->
                partDefinitionMap.forEach((partName, partDefinition) -> {
                    if (StringUtils.isNotBlank(partName)) {
                        Attachment part = buildPartFrom(flowContext, message, partName, partDefinition);
                        allParts.put(partName, part);
                    }
                }));

        return MessageBuilder.get(MultipartMessage.class)
                .withJavaObject(allParts)
                .build();
    }

    public Map<String, PartDefinition> getParts() {
        return parts;
    }

    public void setParts(Map<String, PartDefinition> parts) {
        this.parts = parts;
    }

    public ScriptEngineService getScriptEngine() {
        return scriptEngine;
    }

    public void setScriptEngine(ScriptEngineService scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    private Attachment buildPartFrom(FlowContext flowContext,
                                     Message message,
                                     String partName,
                                     PartDefinition partDefinition) {

        Attachment.Builder partBuilder = Attachment.builder().name(partName);

        // Part Content
        String mimeTypeAsString = partDefinition.getMimeType();
        MimeType mimeType = MimeType.parse(mimeTypeAsString, MimeType.APPLICATION_BINARY);
        DynamicByteArray content = partDefinition.getContent();

        byte[] data = scriptEngine.evaluate(content, flowContext, message).orElse(null);
        partBuilder.data(data);
        partBuilder.mimeType(mimeType);

        // Part attributes
        DynamicStringMap attributesDynamicMap = partDefinition.getAttributes();
        Map<String, String> attributes = scriptEngine.evaluate(attributesDynamicMap, flowContext, message);
        attributes.forEach(partBuilder::attribute);

        return partBuilder.build();
    }
}
