package com.reva.revalocator;

import java.util.List;

public class Section {
    private String name;
    private List<Student> students;

    public Section(String name, List<Student> students) {
        this.name = name;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }
}

