package org.ind.telegram.service;

import it.tdlight.Init;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.util.UnsupportedNativeLibraryException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Log4j2
public class TelegramClientInitializer {
    private final SimpleTelegramClient client;
    private final SimpleTelegramClientFactory factory;

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

    public TelegramClientInitializer(PostHandler postHandler, SimpleTelegramClientFactory factory) {
        this.factory = factory;

        TDLibSettings settings = TDLibSettings.create(new APIToken(apiId, apiHash));
        Path sessionPath = Paths.get(sessionDir);
        settings.setDatabaseDirectoryPath(sessionPath.resolve(dataDir));
        settings.setDownloadedFilesDirectoryPath(sessionPath.resolve(downloadDir));

        SimpleTelegramClientBuilder builder = this.factory.builder(settings);

        builder.addUpdateHandler(TdApi.UpdateNewMessage.class, postHandler::onMessage);
        builder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onAuthUpdate);

        this.client = builder.build(AuthenticationSupplier.consoleLogin());
    }

    private void onAuthUpdate(TdApi.UpdateAuthorizationState update) {
        if (update.authorizationState instanceof TdApi.AuthorizationStateReady) {
            log.info("Telegram client authorized");
        }
    }

    @PostConstruct
    public void init() throws UnsupportedNativeLibraryException {
        Init.init(); // Init native libs
    }

    @PreDestroy
    public void shutdown() {
        client.sendClose();
        factory.close();
    }
}
