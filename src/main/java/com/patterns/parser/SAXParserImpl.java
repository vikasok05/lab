package com.patterns.parser;

import com.patterns.model.Employee;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SAXParserImpl implements XMLParser {
    @Override
    public List<Employee> parse(String filePath) {
        List<Employee> list = new ArrayList<>();

        try {
            var factory = SAXParserFactory.newInstance();
            var parser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                Employee current;
                StringBuilder content = new StringBuilder();

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attrs) {
                    if (qName.equals("employee")) {
                        current = new Employee();
                        current.setId(Integer.parseInt(attrs.getValue("id")));
                    }
                    content.setLength(0);
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    content.append(ch, start, length);
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    switch (qName) {
                        case "name" -> current.setName(content.toString());
                        case "department" -> current.setDepartment(content.toString());
                        case "position" -> current.setPosition(content.toString());
                        case "salary" -> current.setSalary(Double.parseDouble(content.toString()));
                        case "employee" -> list.add(current);
                    }
                }
            };
            parser.parse(new File(filePath), handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
