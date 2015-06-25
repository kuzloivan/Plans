package chisw.com.plans.core.bridge;

import android.content.Context;

/**
 * Created by vdboo_000 on 25.06.2015.
 */
public interface SyncBridge {
    void wasAdding(int planId);
    void wasEditing(int planId);
    void wasDeleting(int planId);
    void startSynchronization(final Context ctxt);
}
