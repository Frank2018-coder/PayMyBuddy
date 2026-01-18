package com.mboumda.paymybuddy.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "connections")
@IdClass(ConnectionId.class)
public class ConnectionEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "buddy_id", nullable = false)
    private Long buddyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buddy_id", insertable = false, updatable = false)
    private UserEntity buddy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters / Setters

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getBuddyId() { return buddyId; }
    public void setBuddyId(Long buddyId) { this.buddyId = buddyId; }

    public UserEntity getUser() { return user; }
    public UserEntity getBuddy() { return buddy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
