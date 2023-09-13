package org.acme.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@StaticInitSafe
@ConfigMapping(prefix = "elasticsearch")
public interface ElasticConfigs {
    @WithName("index.name")
    @WithDefault("event")
    String indexName();
}
