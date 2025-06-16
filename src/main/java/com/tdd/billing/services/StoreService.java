package com.tdd.billing.services;
import com.tdd.billing.dto.StoreRequestDTO;
import com.tdd.billing.dto.StoreResponseDTO;
import com.tdd.billing.entities.Store;
import com.tdd.billing.repositories.StoreRepository;
import com.tdd.billing.mappers.StoreMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final S3Service s3Service;

    public StoreService(StoreRepository storeRepository, S3Service s3Service) {
        this.storeRepository = storeRepository;
        this.s3Service = s3Service;
    }
    @Value("${github.token}")
    private String githubToken;


    public Store create(StoreRequestDTO storeDTO, MultipartFile file) throws IOException {
        String photoUrl = (file != null && !file.isEmpty()) ? s3Service.uploadFile(file) : null;
        Store store = StoreMapper.toEntity(storeDTO);
        store.setLogo(photoUrl);

        Store savedStore = storeRepository.save(store);
        try {
            deployStoreToNetlify(storeDTO, savedStore.getLogo());
        } catch (Exception e) {
            // Puedes loguearlo o lanzar una excepción controlada
            System.err.println("Fallo al disparar el despliegue: " + e.getMessage());
        }

        return savedStore;
    }

    private void deployStoreToNetlify(StoreRequestDTO storeDTO, String logoUrl) throws IOException, InterruptedException {
        // Construir el JSON con el nuevo campo themeJson
        String json = """
{
  "ref": "main",
  "inputs": {
    "name": "%s",
    "url": "%s",
    "email": "%s",
    "contact": "%s",
    "nit": "%s",
    "logo": "%s",
    "description": "%s",
    "status": "%s",
    "address": "%s",
    "themeJson": "%s"
  }
}
""".formatted(
                storeDTO.getName(),
                storeDTO.getUrl(),
                storeDTO.getEmail(),
                storeDTO.getContact(),
                storeDTO.getNit(),
                logoUrl != null ? logoUrl : storeDTO.getLogo(),
                storeDTO.getDescription(),
                String.valueOf(storeDTO.getStatus()),
                storeDTO.getAddress(),
                storeDTO.getThemeJson().replace("\"", "\\\"").replace("\n", "") // importante escapar
        );


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/Danyel3096/Netlify-Deployer/actions/workflows/deploy.yml/dispatches"))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + githubToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 204) {
            throw new RuntimeException("Fallo al disparar el pipeline: " + response.body());
        }
    }



    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    public Store update(Store store) {
        return storeRepository.save(store);
    }

    public Optional<StoreResponseDTO> getStoreDTOById(Long id) {
        return storeRepository.findById(id)
                .map(StoreMapper::toDTO);
    }
}
