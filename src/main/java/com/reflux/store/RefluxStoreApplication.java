package com.reflux.store;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RefluxStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefluxStoreApplication.class, args);
        System.out.println("Reflux Store Application Started");
    }

}
