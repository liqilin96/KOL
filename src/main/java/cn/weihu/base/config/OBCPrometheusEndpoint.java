package cn.weihu.base.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

@Endpoint(id = "pro")
@Configuration
public class OBCPrometheusEndpoint {

    @ReadOperation
    public String endpoint() {
        return this.writeRegistry(Collections.emptySet());
    }

    public String writeRegistry(Set<String> metricsToInclude) {
        try {
            Writer writer = new StringWriter();
            TextFormat.write004(writer, CollectorRegistry.defaultRegistry.filteredMetricFamilySamples(metricsToInclude));
            return writer.toString();
        } catch(IOException var3) {
            throw new RuntimeException("Writing metrics failed", var3);
        }
    }
}
