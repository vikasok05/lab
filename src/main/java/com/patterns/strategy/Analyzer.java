package com.patterns.strategy;

import com.patterns.model.Employee;
import java.util.List;

public interface Analyzer {
    void analyze(List<Employee> employees);
}
