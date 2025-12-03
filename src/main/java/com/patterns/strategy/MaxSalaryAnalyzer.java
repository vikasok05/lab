package com.patterns.strategy;

import com.patterns.model.Employee;

import java.util.Comparator;
import java.util.List;

public class MaxSalaryAnalyzer implements Analyzer {

    @Override
    public void analyze(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("Список сотрудников пуст.");
            return;
        }

        Employee top = employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary))
                .orElse(null);

        System.out.println("Сотрудник с максимальной зарплатой:");
        System.out.println(top);
    }
}
