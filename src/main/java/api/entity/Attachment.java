package api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String contentType;

    private String url;

    private Long size;

    @CreationTimestamp
    private LocalDateTime uploadAt;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private User uploadedBy;



}
