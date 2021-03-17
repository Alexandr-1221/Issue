package ru.netology.manager;

import ru.netology.domain.Issue;
import ru.netology.domain.IssueComparator;
import ru.netology.domain.NotFoundException;
import ru.netology.repository.IssueRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class IssueManager {

    private IssueRepository repository;

    public IssueManager(IssueRepository repository){
        this.repository = repository;
    }

    public void add(Issue issue){
        repository.save(issue);
    }

    public List<Issue> getAll(){
        return repository.findAll();
    }

    public List<Issue> open() {
        List<Issue> result = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (issue.isStatusOpen()) {
                result.add(issue);
            }
        }
        return result;
    }

    public List<Issue> close() {
        List<Issue> result = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (!issue.isStatusOpen()) {
                result.add(issue);
            }
        }
        return result;
    }

    public List<Issue> filter(Predicate<Issue> filter) {
        List<Issue> result = new ArrayList<>();
        for (Issue issue : repository.findAll()) {
            if (filter.test(issue)) {
                result.add(issue);
            }
        }
        return result;
    }

    public List<Issue> filterByAuthor(String author) {
        return filter(issue -> issue.getAuthor().equalsIgnoreCase(author));
    }

    public List<Issue> filterByLabel(String label) {
        return filter(issue -> issue.getLabel().contains(label));
    }

    public List<Issue> filterByAssignee(String assignee) {
        return filter(issue -> issue.getAssignedTo().equalsIgnoreCase(assignee));
    }

    public List<Issue> sortNewest() {
        List<Issue> result = this.getAll();
        result.sort(new IssueComparator());
        return result;
    }

    public List<Issue> sortOldest() {
        List<Issue> result = this.getAll();
        result.sort(new IssueComparator().reversed());
        return result;
    }

    public void openIssue(int id) {
        Issue issue = repository.getById(id);
        if (issue == null) {
            throw new NotFoundException("Issue with id: " + id + " not found");
        }
        else if (!issue.isStatusOpen()) {
            issue.setStatusOpen(true);
        }
    }

    public void closeIssue(int id) {
        Issue issue = repository.getById(id);
        if (issue == null) {
            throw new NotFoundException("Issue with id: " + id + " not found");
        }
        else if (issue.isStatusOpen()) {
            issue.setStatusOpen(false);
        }
    }

}
