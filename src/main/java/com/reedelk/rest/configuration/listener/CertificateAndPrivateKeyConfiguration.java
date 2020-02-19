package com.reedelk.rest.configuration.listener;

import com.reedelk.runtime.api.annotation.Example;
import com.reedelk.runtime.api.annotation.Hint;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.PropertyDescription;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = CertificateAndPrivateKeyConfiguration.class, scope = PROTOTYPE)
public class CertificateAndPrivateKeyConfiguration implements Implementor {

    @Example("/var/certificates/cacert.crt")
    @Hint("/var/certificates/cacert.crt")
    @Property("Certificate")
    @PropertyDescription("The path on the filesystem to the certificate file.")
    private String certificateFile;

    @Example("/var/certificates/private.key")
    @Hint("/var/certificates/private.key")
    @Property("Private key")
    @PropertyDescription("The path on the filesystem to the private key.")
    private String privateKeyFile;

    public String getCertificateFile() {
        return certificateFile;
    }

    public void setCertificateFile(String certificateFile) {
        this.certificateFile = certificateFile;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }
}
