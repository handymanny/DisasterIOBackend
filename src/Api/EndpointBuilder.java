package Api;

import spark.Service;

public interface EndpointBuilder {
  void configure(Service spark, String basePath);
}