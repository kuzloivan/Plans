package chisw.com.dayit.core.callback;

import com.parse.SaveCallback;

import java.util.Date;

/**
 * Created by vdbo on 22.06.15.
 */
public interface OnSaveCallback {
    void getId(String id, long updatedAtParseTime);
}
