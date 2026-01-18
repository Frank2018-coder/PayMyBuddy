package com.mboumda.paymybuddy.model;

import java.io.Serializable;
import java.util.Objects;

public class ConnectionId implements Serializable {

    private Long userId;
    private Long buddyId;

    public ConnectionId() {}

    public ConnectionId(Long userId, Long buddyId) {
        this.userId = userId;
        this.buddyId = buddyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectionId)) return false;
        ConnectionId that = (ConnectionId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(buddyId, that.buddyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, buddyId);
    }

}
