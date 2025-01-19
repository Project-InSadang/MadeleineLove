package sideproject.madeleinelove.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonPropertyOrder({"code", "new_token", "access_token", "payload"})
public class ListApiResponse<T> {
    private final String code;
    @JsonProperty("new_token")
    private final boolean isNewToken;
    @JsonProperty("access_token")
    private final String accessToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T payload;
    public static <T> ResponseEntity<ListApiResponse<T>> onSuccess(boolean isNewToken, String accessToken, T payload) {
        ListApiResponse<T> response = new ListApiResponse<>("200", isNewToken, accessToken, payload);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
