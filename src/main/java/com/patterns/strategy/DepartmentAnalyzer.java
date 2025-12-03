package com.patterns.strategy;

import com.patterns.model.Employee;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepartmentAnalyzer implements Analyzer {
    @Override
    public void analyze(List<Employee> employees) {

        Map<String, Long> stats = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));

        System.out.println("Сотрудники по отделам:");
        stats.forEach((d, c) -> System.out.println(d + " — " + c + " чел."));
    }
}
