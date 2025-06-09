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
@Table(name = "Favorite_Grammars")
public class FavoriteGrammar {
    @Id
    @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    private int id;

    @ManyToMany
    @JoinTable(
            name = "grammar_favorite_grammar",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "grammar_id")
    )
    private Set<Grammar> grammars;

    @Column(name = "added_at")
    private Date addedAt;

    @PrePersist
    protected void onAdd() {
        this.addedAt = new Date();
    }
}
