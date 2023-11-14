package ro.fasttrackit;

import ro.fasttrackit.homework.Gym;
import ro.fasttrackit.homework.GymMember;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static java.time.temporal.ChronoUnit.HOURS;

public class Main {
    public static void main(String[] args) {
        Gym gym = new Gym(Map.of(
                new GymMember("Andrei", LocalDate.now().minusYears(20)), Duration.of(40, HOURS),
                new GymMember("Levi", LocalDate.now().minusYears(45)), Duration.of(5, HOURS),
                new GymMember("Tudor", LocalDate.now().minusYears(33)), Duration.of(15, HOURS),
                new GymMember("Paul", LocalDate.now().minusYears(60)), Duration.of(48, HOURS),
                new GymMember("Nimrod", LocalDate.now().minusYears(19)), Duration.of(20, HOURS)
        ));

        System.out.println(gym.averageAgeOfMembers());
        System.out.println(gym.maxMemberAge());
        System.out.println(gym.minMemberAge());

        System.out.println(gym.registerTime("andrei", Duration.of(3, HOURS)));
        System.out.println(gym.addTime("levi", Duration.of(10, HOURS)));
        gym.registerTime("nimrod", Duration.of(11, HOURS));

        gym.writeReportToFile();
    }
}