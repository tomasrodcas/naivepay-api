package cl.ufro.dci.naive.controller.auth;

import cl.ufro.dci.naive.domain.Customer;

public class LoginResponse {


    public UserSession userSession;
    public String token;
    public String refreshToken;

    public LoginResponse(long cusId, String name, int role, String token, String refreshToken){
        this.userSession = new UserSession(cusId, name, role);
        this.token = token;
        this.refreshToken = refreshToken;
    }
}

class UserSession{
    public long cusId;
    public String name;
    public int role;

    public UserSession(long cusId, String name, int role){
        this.cusId = cusId;
        this.name = name;
        this.role = role;
    }
}
