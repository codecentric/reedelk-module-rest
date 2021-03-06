package de.codecentric.reedelk.rest.component.listener;

import de.codecentric.reedelk.runtime.api.annotation.Description;
import de.codecentric.reedelk.runtime.api.annotation.Example;
import de.codecentric.reedelk.runtime.api.annotation.Hint;
import de.codecentric.reedelk.runtime.api.annotation.Property;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = CertificateAndPrivateKeyConfiguration.class, scope = PROTOTYPE)
public class CertificateAndPrivateKeyConfiguration implements Implementor {

    @Property("Certificate")
    @Example("/var/certificates/cacert.crt")
    @Hint("/var/certificates/cacert.crt")
    @Description("The path on the filesystem to the certificate file.")
    private String certificateFile;

    @Property("Private key")
    @Example("/var/certificates/private.key")
    @Hint("/var/certificates/private.key")
    @Description("The path on the filesystem to the private key.")
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
