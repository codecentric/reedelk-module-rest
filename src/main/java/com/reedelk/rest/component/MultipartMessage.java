package com.reedelk.rest.component;

import com.reedelk.rest.component.multipart.PartDefinition;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.ByteArrayContent;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.Part;
import com.reedelk.runtime.api.message.content.Parts;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
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
    private ScriptEngineService scriptEngine;
    @Reference
    private ConverterService converterService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        Parts allParts = new Parts();
        Optional.ofNullable(parts).ifPresent(partDefinitionMap ->
                partDefinitionMap.forEach((partName, partDefinition) -> {
                    if (StringUtils.isNotBlank(partName)) {
                        Part part = buildPartFrom(flowContext, message, partName, partDefinition);
                        allParts.put(partName, part);
                    }
                }));

        return MessageBuilder.get().withJavaObject(allParts).build();
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

    private Part buildPartFrom(FlowContext flowContext,
                               Message message,
                               String partName,
                               PartDefinition partDefinition) {

        Part.Builder partBuilder = Part.builder().name(partName);

        // Part Content
        String mimeTypeAsString = partDefinition.getMimeType();
        MimeType mimeType = MimeType.parse(mimeTypeAsString, MimeType.APPLICATION_BINARY);
        DynamicByteArray content = partDefinition.getContent();

        byte[] data = scriptEngine.evaluate(content, flowContext, message).orElse(null);
        partBuilder.content(new ByteArrayContent(data, mimeType));

        // Part attributes
        DynamicStringMap attributesDynamicMap = partDefinition.getAttributes();
        Map<String, String> attributes = scriptEngine.evaluate(attributesDynamicMap, flowContext, message);
        attributes.forEach(partBuilder::attribute);

        return partBuilder.build();
    }
}
