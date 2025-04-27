package com.jwcomptech.shared;

import com.jwcomptech.shared.utils.Misc;
import com.jwcomptech.shared.utils.SingletonUtils;
import com.jwcomptech.shared.values.ImmutablePropsValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.maven.api.model.Model;
import org.apache.maven.model.v4.MavenStaxReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.jwcomptech.shared.Literals.cannotBeNullOrEmpty;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNullOrEmpty;
import static java.nio.charset.StandardCharsets.UTF_8;

@EqualsAndHashCode
@ToString
@SuppressWarnings("unused")
public class POMManager {
    private final ImmutablePropsValue internalProps;
    @Getter
    private final Model internalPOM;

    public static POMManager getInstance() {
        return SingletonUtils.getInstance(POMManager.class, POMManager::new);
    }

    public POMManager() {
        String internalPOMPath = "/pom.properties";
        try(InputStream internalPOMStream = Main.class.getResourceAsStream(internalPOMPath)) {
            internalProps = new ImmutablePropsValue(internalPOMStream);
            String artifactId = internalProps.get("artifactId");
            checkArgumentNotNullOrEmpty(artifactId, cannotBeNullOrEmpty("artifact"));
            final String rootPath = "pom.xml";
            final Path path = Paths.get(rootPath);
            final String currentPackage = Main.class.getPackage().getName();
            final String metaPath = "/META-INF/maven/" + currentPackage + "/" + artifactId + "/pom.xml";

            if(Files.exists(path)) {
                internalPOM = new MavenStaxReader().read(Files.newBufferedReader(path, UTF_8));
            } else {
                try {
                    internalPOM = new MavenStaxReader().read(Misc.class.getResourceAsStream(metaPath));
                } catch(XMLStreamException e) {
                    throw new IllegalStateException("Cannot Access Project POM File!");
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot Access Project POM File!", e);
        } catch (XMLStreamException e) {
            throw new IllegalStateException("Cannot Process Project POM File!", e);
        }
    }

    public Model getPOM(final String path) throws IOException, XMLStreamException {
        return getPOM(Paths.get(path));
    }

    public Model getPOM(final Path path) throws IOException, XMLStreamException {
        return new MavenStaxReader().read(Files.newBufferedReader(path, UTF_8));
    }
}
