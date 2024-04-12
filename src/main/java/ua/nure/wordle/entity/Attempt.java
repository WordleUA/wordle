package ua.nure.wordle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "attempt")
public class Attempt {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 6)
    @NotNull
    @Column(name = "attempted_word", nullable = false, length = 6)
    private String attemptedWord;

    @NotNull
    @Column(name = "attempt_time", nullable = false)
    private LocalTime attemptTime;

}