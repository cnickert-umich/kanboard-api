package edu.umich.kanboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class KanboardApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(KanboardApiApplication.class, args);
	}


}
