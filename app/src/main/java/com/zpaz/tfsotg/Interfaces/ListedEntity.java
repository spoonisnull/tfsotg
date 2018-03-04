package com.zpaz.tfsotg.Interfaces;

import com.zpaz.tfsotg.Enums.EntityStatus;

/**
 * Created by zsolt on 21/02/18.
 */

public interface ListedEntity {
    String getCreatedBy();
    String getTime();
    String getName();
    EntityStatus getStatus();
    String getDefinition();
}
