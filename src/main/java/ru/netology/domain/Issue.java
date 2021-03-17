package ru.netology.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Issue {
    private int id;
    private String name;
    private String text;
    private Set<String> label;
    private String author;
    private int date;
    private String assignedTo;
    private int comments;
    private String milestones;
    private boolean statusOpen;
}
