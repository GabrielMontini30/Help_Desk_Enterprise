package api.entity;

import api.util.Category;
import api.util.Priority;
import api.util.TicketStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String title;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    private User openedBy;

    @ManyToOne
    private User assignedTo;

    @OneToMany(mappedBy = "ticket")
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticket")
    private List<Attachment> attachments;

    @LastModifiedDate
    private LocalDateTime slaDeadline;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @LastModifiedDate
    private LocalDateTime closedAt;
}
