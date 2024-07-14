package nl.fontys.s3.individual.news.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
