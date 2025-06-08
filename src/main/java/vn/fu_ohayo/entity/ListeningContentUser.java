//package vn.fu_ohayo.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Builder
//@Table(name = "content_listenings",
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "title")
//        },
//        indexes = {
//                @Index(name = "idx_listening_content_id", columnList = "content_id"),
//                @Index(name = "idx_listening_content_title", columnList = "title")
//        }
//)
//public class ListeningContentUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "content_id")
//    private Integer id;
//
//    @Column(name = "title", nullable = false)
//    private String title;
//
//    @Column(name = "image")
//    private String image;
//
//    @Column(name = "category", nullable = false)
//    private String category;
//
//    @Column(name = "url", nullable = false)
//    private String url;
//
//    @Column(name = "script_jp")
//    private String scriptJp;
//
//    @Column(name = "script_vn")
//    private String scriptVn;
//
//    @Column(name = "audio_file")
//    private String audioFile;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @OneToMany(mappedBy = "listeningContent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<ListeningContentUser> listeningContentsUsers;
//
//    @OneToMany(mappedBy = "listeningContent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<ExerciseQuestion> exerciseQuestions;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
//}