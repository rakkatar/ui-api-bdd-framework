package pl.recruitment.qa.api;

import pl.recruitment.qa.core.ConfigProvider;

public class NbpApiClient {
    public String getBaseUrl() {
        return ConfigProvider.getEnv("NBP_API_URL", "https://api.nbp.pl/api");
    }
}
