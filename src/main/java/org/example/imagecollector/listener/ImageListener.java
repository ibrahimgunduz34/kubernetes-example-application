package org.example.imagecollector.listener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
@Profile("downloader")
public class ImageListener {
    private static Logger logger = LoggerFactory.getLogger(ImageListener.class);

    @Value("${download.path}")
    private String downloadPath;

    @RabbitListener(queues = "photos")
    public void handle(String url) throws IOException {
        logger.info("New photo received: " + url);
        try {
            URL oUrl = new URL(url);
            String filename = FilenameUtils.getName(oUrl.getPath());
            logger.info("Downloading file from: " + url);
            File localFile = new File(downloadPath, filename);
            FileUtils.copyURLToFile(oUrl, localFile);
            logger.info("Download complete: " + localFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
