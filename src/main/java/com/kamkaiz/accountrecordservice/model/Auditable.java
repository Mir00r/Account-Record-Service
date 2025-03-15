package com.kamkaiz.accountrecordservice.model;

import java.time.LocalDateTime;

/**
 * Interface for entities that need to track creation and modification timestamps.
 * This follows the Interface Segregation Principle by separating auditing concerns.
 */
public interface Auditable {
    LocalDateTime getCreatedAt();
    void setCreatedAt(LocalDateTime createdAt);
    LocalDateTime getUpdatedAt();
    void setUpdatedAt(LocalDateTime updatedAt);
}