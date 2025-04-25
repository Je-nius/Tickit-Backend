package jenius.performanceservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String savedFileName;

    private String contentType;

    @Builder
    public Image(String fileName, String savedFileName, String contentType) {
        this.fileName = fileName;
        this.savedFileName = savedFileName;
        this.contentType = contentType;
    }

}
