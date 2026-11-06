package com.ndevstudios.synccanvas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_states")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false, unique = true)
    private Board board;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content_json", nullable = false, columnDefinition = "jsonb")
    private JsonNode contentJson;

    @Column(nullable = false)
    private Long version = 0L;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
