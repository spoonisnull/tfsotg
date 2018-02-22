package com.zpaz.tfsotg;

/**
 * Created by zsolt on 21/02/18.
 */

public interface TfsQueuedEntity {
    String getId();
    String getCreatedBy();
    String getCreatedOn();
    String getStatus();
    String getLogsUrl();
    String getUrl();
    String getModifiedBy();
    String getModifiedOn();
}
