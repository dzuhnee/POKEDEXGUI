package com.pokedex.app;
import java.time.LocalDate;

public class TrainerBasic {
        private int trainerID;
        private String name;
        private LocalDate birthdate;
        private String gender;
        private String hometown;
        private String description;

        public TrainerBasic(int trainerId, String name, LocalDate birthdate, String gender, String hometown, String description) {
            this.trainerID = trainerId;
            this.name = name;
            this.birthdate = birthdate;
            this.gender = gender;
            this.hometown = hometown;
            this.description = description;
        }

        public int getTrainerID() {
            return trainerID;
        }

        public String getName() {
            return name;
        }

        public LocalDate getBirthdate() {
            return birthdate;
        }

        public String getGender() {
            return gender;
        }

        public String getHometown() {
            return hometown;
        }

        public String getDescription() {
            return description;
        }
}
