package ch.webec.recipeapp.adapters;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class GCPCloudStorageAPI {

    private WebClient webClient;
    private final Storage storage;
    private final String bucketName;

    public GCPCloudStorageAPI( Storage storage, @Value("${spring.cloud.gcp.storage.bucket-name}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public String uploadImage(String imageUrl) {
        try {
            System.out.println(imageUrl);
            this.webClient = WebClient.builder().baseUrl(imageUrl).build();
            Flux<DataBuffer> imageBufferFlux = webClient.get()
                    .uri(imageUrl)
                    .accept(MediaType.APPLICATION_OCTET_STREAM)
                    .retrieve()
                    .bodyToFlux(DataBuffer.class);

            byte[] imageBytes = imageBufferFlux.reduce(new byte[0], (a, b) -> {
                byte[] combined = new byte[a.length + b.readableByteCount()];
                System.arraycopy(a, 0, combined, 0, a.length);
                b.read(combined, a.length, b.readableByteCount());
                return combined;
            }).block();

            InputStream imageStream = new ByteArrayInputStream(imageBytes);

            String blobName = UUID.randomUUID().toString();
            BlobId blobId = BlobId.of(bucketName, blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            Blob blob = storage.create(blobInfo, imageStream);
            return blob.getMediaLink();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image to GCP Cloud Storage", e);
        }
    }
}
