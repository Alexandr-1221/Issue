package ru.netology.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.domain.Issue;
import ru.netology.domain.NotFoundException;
import ru.netology.repository.IssueRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class IssueManagerTest {
    private IssueRepository repository = new IssueRepository();
    private IssueManager manager = new IssueManager(repository);
    private Issue first = new Issue(1, "Issue 1", "Issue text", Set.of("component: Jupiter", "status: blocked"), "Author 1", 6,
            "Link 1", 3, "Milestone 1", true);
    private Issue second = new Issue(2, "Issue 2", "Issue text", Set.of("status: duplicate", "theme: concurrency"), "Author 2", 1, "Link 2", 2, "Milestone 2", true);
    private Issue third = new Issue(3, "Issue 3", "Issue text", Set.of("component: Jupiter"), "Author 3", 2, "Link 3", 4, "Milestone 3", false);
    private Issue fourth = new Issue(4, "Issue 4", "Issue text", Set.of("status: blocked"), "Author 3", 4, "Link 4", 1, "Milestone 4", true);
    private Issue fifth = new Issue(5, "Issue 5", "Issue text", Set.of("theme: concurrency"), "Author 5", 3, "Link 3", 10, "Milestone 5", false);
    private Issue sixth = new Issue(6, "Issue 6", "Issue text", Set.of("status: duplicate"), "Author 6", 5, "Link 6", 7, "Milestone 6", true);

    @Nested
    public class Empty {

        @Test
        void shouldAddInGetOpenedIssues() {
            manager.add(first);
            List<Issue> actual = manager.open();
            List<Issue> expected = List.of(first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldGetClosedIssues() {
            List<Issue> actual = manager.close();
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }

        @Test
        void shouldFilterByLabel() {
            List<Issue> actual = manager.filterByLabel("component: Jupiter");
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }

        @Test
        void shouldSortNewest() {
            List<Issue> actual = manager.sortNewest();
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }

        @Test
        void shouldOpenIssue() {
            assertThrows(NotFoundException.class, () -> manager.openIssue(3));
        }

        @Test
        void shouldCloseIssue() {
            assertThrows(NotFoundException.class, () -> manager.closeIssue(3));
        }
    }

    @Nested
    public class SingleItem {

        @BeforeEach
        public void setUp() {
            manager.add(first);
        }

        @Test
        void shouldGetOpenedIssues() {
            List<Issue> actual = manager.open();
            List<Issue> expected = List.of(first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldGetClosedIssues() {
            List<Issue> actual = manager.close();
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }


        @Test
        void shouldFilterByAuthor() {
            List<Issue> actual = manager.filterByAuthor("Author 1");
            List<Issue> expected = List.of(first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldFilterByAssignee() {
            List<Issue> actual = manager.filterByAssignee("Link 1");
            List<Issue> expected = List.of(first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldSortNewest() {
            List<Issue> actual = manager.sortNewest();
            List<Issue> expected = List.of(first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldCloseIssue() {
            manager.closeIssue(1);
            boolean actual = repository.getById(1).isStatusOpen();
            assertEquals(false, actual);
        }

        @Test
        void shouldOpenIssueNotExist() {
            assertThrows(NotFoundException.class, () -> manager.closeIssue(2));
        }
    }

    @Nested
    public class MultipleItems {

        @BeforeEach
        public void setUp() {
            manager.add(first);
            manager.add(second);
            manager.add(third);
            manager.add(fourth);
            manager.add(fifth);
            manager.add(sixth);
        }

        @Test
        void shouldGetOpenedIssues() {
            List<Issue> actual = manager.open();
            List<Issue> expected = List.of(first, second, fourth, sixth);
            assertEquals(expected, actual);
        }

        @Test
        void shouldGetClosedIssues() {
            List<Issue> actual = manager.close();
            List<Issue> expected = List.of(third, fifth);
            assertEquals(expected, actual);
        }

        @Test
        void shouldFilterByAuthor() {
            List<Issue> actual = manager.filterByAuthor("Author 3");
            List<Issue> expected = List.of(third, fourth);
            assertEquals(expected, actual);
        }

        @Test
        void shouldFilterByLabel() {
            List<Issue> actual = manager.filterByLabel("component: Jupiter");
            List<Issue> expected = List.of(first, third);
            assertEquals(expected, actual);
        }

        @Test
        void shouldFilterByAssignee() {
            List<Issue> actual = manager.filterByAssignee("Link 3");
            List<Issue> expected = List.of(third, fifth);
            assertEquals(expected, actual);
        }

        @Test
        void shouldFilterByAuthorNotExist() {
            List<Issue> actual = manager.filterByAssignee("text");
            List<Issue> expected = new ArrayList<>();
            assertEquals(expected, actual);
        }

        @Test
        void shouldSortNewest() {
            List<Issue> actual = manager.sortNewest();
            List<Issue> expected = List.of(first, sixth, fourth, fifth, third, second);
            assertEquals(expected, actual);
        }

        @Test
        void shouldSortOldest() {
            List<Issue> actual = manager.sortOldest();
            List<Issue> expected = List.of(second, third, fifth, fourth, sixth, first);
            assertEquals(expected, actual);
        }

        @Test
        void shouldOpenClosedIssue() {
            manager.openIssue(2);
            boolean actual = repository.getById(2).isStatusOpen();
            assertEquals(true, actual);
        }

        @Test
        void shouldOpenOpenedIssue() {
            manager.openIssue(1);
            boolean actual = repository.getById(1).isStatusOpen();
            assertEquals(true, actual);
        }

        @Test
        void shouldCloseOpenedIssue() {
            manager.closeIssue(1);
            boolean actual = repository.getById(1).isStatusOpen();
            assertEquals(false, actual);
        }

        @Test
        void shouldCloseClosedIssue() {
            manager.closeIssue(2);
            boolean actual = repository.getById(2).isStatusOpen();
            assertEquals(false, actual);
        }
    }

}