package com.task.product.catalog.domain.model.category;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {

    @Id
    private UUID id;

    private String name;

    private String description;

    @Column(name = "parent_id")
    private UUID parentId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_closure",
            joinColumns = @JoinColumn(name = "ancestor_id"),
            inverseJoinColumns = @JoinColumn(name = "descendant_id")
    )
    private Set<Category> descendants = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_closure",
            joinColumns = @JoinColumn(name = "descendant_id"),
            inverseJoinColumns = @JoinColumn(name = "ancestor_id")
    )
    private Set<Category> ancestors = new HashSet<>();

    public Category(final String name, final String description, final UUID parentId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.parentId = parentId;
    }

}
