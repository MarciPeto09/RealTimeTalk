package com.Marcelina.RealTimeTalk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="messages")
@AllArgsConstructor
@NoArgsConstructor
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonBackReference
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @ToString.Exclude
    private Users sender;

    @Column
    private String content;

    @Column
    private String fileName;

    @Column
    private String fileUrl;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
