package com.Marcelina.RealTimeTalk.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String photoUrl;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(mappedBy = "participants",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Conversation> conversations;


    @Column(name = "connected")
    private Boolean connected = false;

}
