package br.com.dev4u.faroldocs.cucumber;

import br.com.dev4u.faroldocs.FarolDocsApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = FarolDocsApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
