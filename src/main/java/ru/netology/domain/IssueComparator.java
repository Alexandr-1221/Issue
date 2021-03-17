package ru.netology.domain;

import java.util.Comparator;

public class IssueComparator implements Comparator<Issue> {
    public int compare(Issue o1, Issue o2){
        return o2.getDate() - o1.getDate();
    }
}
