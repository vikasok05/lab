package com.patterns;

import com.patterns.config.Config;
import com.patterns.model.Employee;
import com.patterns.parser.*;
import com.patterns.strategy.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Config config = Config.getInstance();
        config.setParserType("dom");   // можно переключить на sax

        XMLParser parser = ParserFactory.createParser(config.getParserType());
        List<Employee> employees = parser.parse(config.getXmlPath());

        System.out.println("Загружено сотрудников: " + employees.size());
        employees.forEach(System.out::println);

        Analyzer analyzer = new DepartmentAnalyzer(); // можно менять стратегию
        analyzer.analyze(employees);
    }
}
