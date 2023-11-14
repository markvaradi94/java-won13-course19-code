package ro.fasttrackit.homework;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GymMember(
        String name,
        LocalDate birthDate
) {
}
