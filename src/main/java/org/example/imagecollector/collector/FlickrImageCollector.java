package org.example.imagecollector.collector;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("collector")
public class FlickrImageCollector implements CommandLineRunner {
    @Value("${flickr.api.key}")
    private String apiKey;

    @Value("${flickr.api.shared.secret}")
    private String sharedSecret;

    @Value("${collector.request.delay}")
    private Integer requestDelay;

    private final static String[] PHOTO_TAGS = {"cat"};

    private RabbitTemplate rabbitTemplate;

    private static Logger logger = LoggerFactory.getLogger(FlickrImageCollector.class);

    public FlickrImageCollector(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        Flickr f = new Flickr(apiKey, sharedSecret, new REST());

        SearchParameters sp = new SearchParameters();
        sp.setTags(PHOTO_TAGS);

        for (int i=0; true; i++) {
            PhotoList<Photo> photos = f.getPhotosInterface().search(sp, 500, i);
            if(photos.isEmpty()) {
                break;
            }

            for (Photo photo : photos) {
                String photoSource = String.format("https://farm%s.staticflickr.com/%s/%s_%s.jpg",
                        photo.getFarm(),
                        photo.getServer(),
                        photo.getId(),
                        photo.getSecret());
                logger.info("Sending: " + photoSource);
                rabbitTemplate.convertAndSend("photos", photoSource);
                Thread.sleep(requestDelay);;
            }
        }
    }
}
