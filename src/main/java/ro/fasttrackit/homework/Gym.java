package ro.fasttrackit.homework;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.time.Duration.ZERO;
import static java.util.stream.Collectors.*;
import static ro.fasttrackit.homework.TimeClassification.fromDuration;

@Data
@NoArgsConstructor
public class Gym {
    private Map<GymMember, Duration> subscriptions;

    public Gym(Map<GymMember, Duration> subscriptions) {
        this.subscriptions = new HashMap<>(subscriptions);
    }

    public Gym addMember(GymMember member) {
        if (!subscriptions.containsKey(member)) {
            subscriptions.put(member, ZERO);
        }

        return this;
    }

    public String registerTime(String name, Duration spentTime) {
        updateMemberTime(name, currentDuration -> currentDuration.minus(spentTime));
        return memberInfo(name);
    }

    public String addTime(String name, Duration additionalTime) {
        updateMemberTime(name, currentDuration -> currentDuration.plus(additionalTime));
        return memberInfo(name);
    }

    private void updateMemberTime(String name, Function<Duration, Duration> timeUpdater) {
        GymMember member = findByName(name);
        Duration memberDuration = subscriptions.get(member);
        Duration updatedDuration = timeUpdater.apply(memberDuration);

        if (updatedDuration.toHours() < 0) {
            updatedDuration = ZERO;
        }

        subscriptions.put(member, updatedDuration);
    }

    public String memberInfo(String name) {
        GymMember member = findByName(name);
        Duration memberTime = subscriptions.get(member);
        return "Member %s has %s hours left on his subscription.".formatted(member.name(), memberTime.toHours());
    }

    public Double averageAgeOfMembers() {
        return subscriptions.keySet().stream()
                .mapToInt(this::extractAge)
                .average()
                .orElseThrow(() -> new RuntimeException("Could not calculate average"));
    }

    public Integer maxMemberAge() {
        return subscriptions.keySet().stream()
                .mapToInt(this::extractAge)
                .max()
                .orElseThrow(() -> new RuntimeException("Could not find maximum age"));
    }

    public Integer minMemberAge() {
        return subscriptions.keySet().stream()
                .mapToInt(this::extractAge)
                .min()
                .orElseThrow(() -> new RuntimeException("Could not find minimum age"));
    }

    public Long totalRemainingDurationForAll() {
        return subscriptions.values().stream()
                .mapToLong(Duration::toHours)
                .sum();
    }

    public void writeReportToFile() {
        String fileName = "src/main/resources/reports/remaining-time-report-" + LocalDate.now() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            var report = generateReport();
            report.entrySet().forEach(set -> writeToFile(set, writer));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void writeToFile(Entry<TimeClassification, List<String>> set, BufferedWriter writer) {
        try {
            writer.write(set.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Map<TimeClassification, List<String>> generateReport() {
        return subscriptions.entrySet().stream()
                .collect(groupingBy(
                        set -> fromDuration(set.getValue().toHours()),
                        mapping(set -> set.getKey().name(), toList())
                ));
    }

    private GymMember findByName(String name) {
        return subscriptions.keySet().stream()
                .filter(member -> member.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find member with name %s".formatted(name)));
    }

    private int extractAge(GymMember member) {
        return LocalDate.now().getYear() - member.birthDate().getYear();
    }
}
