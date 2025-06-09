package vn.fu_ohayo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Favorite_Vocabulary")
public class FavoriteVocabulary {

   @Id @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    private int id;

    @ManyToMany
    @JoinTable(
            name = "vocabulary_favorite_vocabulary",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "vocabulary_id")
    )
    private Set<Vocabulary> vocabularies;

    @Column(name = "added_at")
    private Date addedAt;

    @PrePersist
    protected void onAdd() {
        this.addedAt = new Date();
    }
}
