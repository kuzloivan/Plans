package chisw.com.plans.core.bridge;

public interface NetBridge {

    public void registerUser(String pName, String pPassword);
    public void loginUser(String pName, String pPassword);

}
