package com.patterns.config;

public class Config {
    private static Config instance;

    private String parserType = "sax";
    private String xmlPath = "src/main/resources/employees.xml";

    private Config() {}

    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }

    public String getParserType() { return parserType; }
    public String getXmlPath() { return xmlPath; }

    public void setParserType(String type) { this.parserType = type; }
    public void setXmlPath(String path) { this.xmlPath = path; }
}
