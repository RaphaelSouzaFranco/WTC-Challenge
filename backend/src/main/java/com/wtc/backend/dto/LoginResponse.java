package com.wtc.backend.dto;

public class LoginResponse {
    private String token;
    private String refreshToken;
    private String tokenType;
    private OperatorDTO operator;

    public LoginResponse() {}
    public LoginResponse(String token, String refreshToken, String tokenType, OperatorDTO operator) {
        this.token = token; this.refreshToken = refreshToken;
        this.tokenType = tokenType; this.operator = operator;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String token, refreshToken, tokenType;
        private OperatorDTO operator;
        public Builder token(String v) { this.token = v; return this; }
        public Builder refreshToken(String v) { this.refreshToken = v; return this; }
        public Builder tokenType(String v) { this.tokenType = v; return this; }
        public Builder operator(OperatorDTO v) { this.operator = v; return this; }
        public LoginResponse build() { return new LoginResponse(token, refreshToken, tokenType, operator); }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public OperatorDTO getOperator() { return operator; }
    public void setOperator(OperatorDTO operator) { this.operator = operator; }
}
