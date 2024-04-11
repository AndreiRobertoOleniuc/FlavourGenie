package ch.webec.recipeapp.adapters;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class GCPCloudStorageAPI {

    private final WebClient webClient;
    private final Storage storage;
    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;

    @Autowired
    public GCPCloudStorageAPI(Storage storage) {
        // Configure a larger buffer size limit for the WebClient
        int bufferSize = 16 * 1024 * 1024; // for example, 16 MB
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs()
                        .maxInMemorySize(bufferSize))
                .build();
        this.webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .build();
        this.storage = storage;
    }

    public String uploadImage(String imageUrl, String recipeName) {
        // Step 1: Download the image
        Mono<byte[]> imageMono = this.webClient.get()
                .uri(URI.create(imageUrl))
                .retrieve()
                .bodyToMono(byte[].class);

        byte[] imageData = imageMono.block(); // Consider making your method reactive to avoid blocking

        // Define the object name in the bucket
        String sanitizedRecipeName = recipeName.replaceAll("[^a-zA-Z0-9\\-]", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String objectName = sanitizedRecipeName + "_" + timestamp;

        // Step 2: Upload to GCS Bucket
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png") // Set the content type appropriately
                .build();

        // Upload the image
        storage.create(blobInfo, imageData);

        // Step 3: Make the uploaded image publicly accessible
        // Note: This requires the storage.objects.setIamPolicy permission
        storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        // Return the public URL to access the uploaded file
        return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
    }
}
