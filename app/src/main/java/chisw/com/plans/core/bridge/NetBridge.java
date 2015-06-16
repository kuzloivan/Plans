package chisw.com.plans.core.bridge;

import com.parse.ParseException;

public interface NetBridge {

    void registerUser(String pName, String pPassword) throws ParseException;
    void loginUser(String pName, String pPassword);

}
