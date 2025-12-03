package com.patterns.strategy;

import com.patterns.model.Employee;
import java.util.List;

public class SalaryAnalyzer implements Analyzer {
    @Override
    public void analyze(List<Employee> employees) {
        double avg = employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);

        System.out.println("Средняя зарплата: " + avg);
    }
}
