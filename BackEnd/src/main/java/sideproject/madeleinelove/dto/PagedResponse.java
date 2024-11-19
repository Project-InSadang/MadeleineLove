package sideproject.madeleinelove.dto;
import lombok.Data;
import java.util.List;

@Data
public class PagedResponse<T> {

    private List<T> data;
    private String nextCursor;

}
