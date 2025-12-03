package com.patterns.parser;

public class ParserFactory {
    public static XMLParser createParser(String type) {
        return switch (type.toLowerCase()) {
            case "sax" -> new SAXParserImpl();
            case "dom" -> new DOMParserImpl();
            default -> throw new IllegalArgumentException("Unknown parser type: " + type);
        };
    }
}
