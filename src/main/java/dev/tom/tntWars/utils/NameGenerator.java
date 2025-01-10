package dev.tom.tntWars.utils;

import com.github.javafaker.Faker;
import dev.tom.tntWars.TNTWars;

public class NameGenerator {

    public static String generateName() {
        Faker faker = TNTWars.getFaker();
        String s = faker.ancient().titan()+ "-" + faker.ancient().god()+ "-" + faker.random().nextInt(1,100);
        s.replace(" ", "-");
        return s;
    }

}
