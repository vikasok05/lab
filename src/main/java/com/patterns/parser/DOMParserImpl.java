package com.patterns.parser;

import com.patterns.model.Employee;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class DOMParserImpl implements XMLParser {
    @Override
    public List<Employee> parse(String filePath) {
        List<Employee> list = new ArrayList<>();
        try {
            var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(filePath));

            NodeList nodes = doc.getElementsByTagName("employee");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element e = (Element) nodes.item(i);

                int id = Integer.parseInt(e.getAttribute("id"));
                String name = e.getElementsByTagName("name").item(0).getTextContent();
                String dep = e.getElementsByTagName("department").item(0).getTextContent();
                String pos = e.getElementsByTagName("position").item(0).getTextContent();
                double sal = Double.parseDouble(e.getElementsByTagName("salary").item(0).getTextContent());

                list.add(new Employee(id, name, dep, pos, sal));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
