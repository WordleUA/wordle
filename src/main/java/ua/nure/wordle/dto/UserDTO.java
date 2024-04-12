package ua.nure.wordle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nure.wordle.entity.Enum.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private UserRole role;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private Boolean is_banned;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private Integer game_win_count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private Integer game_count;


}
