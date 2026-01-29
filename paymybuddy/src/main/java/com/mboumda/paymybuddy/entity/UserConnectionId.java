package com.mboumda.paymybuddy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserConnectionId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "connection_id")
    private Long connectionId;

    public UserConnectionId() {}

    public UserConnectionId(Long userId, Long connectionId) {
        this.userId = userId;
        this.connectionId = connectionId;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getConnectionId() { return connectionId; }
    public void setConnectionId(Long connectionId) { this.connectionId = connectionId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserConnectionId that = (UserConnectionId) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(connectionId, that.connectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, connectionId);
    }
}
