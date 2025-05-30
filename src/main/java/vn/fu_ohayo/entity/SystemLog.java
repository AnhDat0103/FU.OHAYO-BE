package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.RoleEnum;

@Entity
@Table(name = "system_logs")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String action;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String details;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

}

