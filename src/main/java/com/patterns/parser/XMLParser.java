package com.patterns.parser;

import com.patterns.model.Employee;
import java.util.List;

public interface XMLParser {
    List<Employee> parse(String filePath);
}
