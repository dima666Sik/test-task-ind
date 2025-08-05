package org.ind.telegram.config;

import it.tdlight.Init;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.util.UnsupportedNativeLibraryException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.ind.telegram.service.PostHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Log4j2
public class TdLightConfig {
    private SimpleTelegramClient simpleTelegramClient;
    private SimpleTelegramClientFactory simpleTelegramClientFactory;
    @Value("${telegram.client.api-id}")
    private Integer apiId;
    @Value("${telegram.client.api-hash}")
    private String apiHash;
    @Value("${telegram.client.session-dir}")
    private String sessionDir;
    @Value("${telegram.client.data-dir}")
    private String dataDir;
    @Value("${telegram.client.download-dir}")
    private String downloadDir;

    @PostConstruct
    public void init() throws UnsupportedNativeLibraryException {
        Init.init(); // Init native libs
    }

    @Bean
    public SimpleTelegramClientFactory simpleTelegramClientFactory() {
        simpleTelegramClientFactory = new SimpleTelegramClientFactory();
        return simpleTelegramClientFactory;
    }

    @Bean
    public SimpleTelegramClient simpleTelegramClient(PostHandler postHandler, SimpleTelegramClientFactory factory) {
        // change it on real data... and turn APIToken.example() into new APIToken(apiId, apiHash);
        APIToken apiToken = APIToken.example();

        TDLibSettings settings = TDLibSettings.create(apiToken);
        Path sessionPath = Paths.get(sessionDir);
        settings.setDatabaseDirectoryPath(sessionPath.resolve(dataDir));
        settings.setDownloadedFilesDirectoryPath(sessionPath.resolve(downloadDir));

        SimpleTelegramClientBuilder builder = factory.builder(settings);

        builder.addUpdateHandler(TdApi.UpdateNewMessage.class, updateNewMessage ->
            postHandler.onMessage(updateNewMessage, simpleTelegramClient));
        builder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onAuthUpdate);

        simpleTelegramClient = builder.build(AuthenticationSupplier.consoleLogin());

        return simpleTelegramClient;
    }

    private void onAuthUpdate(TdApi.UpdateAuthorizationState update) {
        if (update.authorizationState instanceof TdApi.AuthorizationStateReady) {
            log.info("Telegram client authorized");
        }
    }

    @PreDestroy
    public void shutdown() {
        simpleTelegramClient.sendClose();
        simpleTelegramClientFactory.close();
    }
}
