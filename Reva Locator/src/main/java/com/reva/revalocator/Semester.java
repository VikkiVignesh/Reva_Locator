package com.reva.revalocator;

import java.util.List;

public class Semester {
    private String name;
    private List<Section> sections;

    public Semester(String name, List<Section> sections) {
        this.name = name;
        this.sections = sections;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections;
    }
}
