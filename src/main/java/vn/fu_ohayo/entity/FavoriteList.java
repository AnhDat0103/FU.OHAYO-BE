package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Favorite_List")
public class FavoriteList {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "favorite_list_id")
    private int favoriteId;

    @Column(name = "favorite_list_name")
    private String favoriteListName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_public")
    private boolean isPublic = false;

    @Column(name = "created_at")
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
