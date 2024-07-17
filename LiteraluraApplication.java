package com.alurachallenge.literalura;

import com.alurachallenge.literalura.principal.MenuPrincipal;
import com.alurachallenge.literalura.repositorio.AutorRepositorio;
import com.alurachallenge.literalura.repositorio.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.alurachallenge.literalura.repositorio")
@ComponentScan(basePackages = {"com.alurachallenge.literalura"})
public class LiteraluraApplication implements CommandLineRunner {

@Autowired
	private LibroRepositorio libroRepositorio;
@Autowired
	private AutorRepositorio autorRepositorio;
	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MenuPrincipal menuPrincipal = new MenuPrincipal(libroRepositorio, autorRepositorio);
		menuPrincipal.muestraElMenu();
	}
}
