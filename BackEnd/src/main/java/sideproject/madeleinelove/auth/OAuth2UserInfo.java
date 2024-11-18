package sideproject.madeleinelove.auth;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
}
