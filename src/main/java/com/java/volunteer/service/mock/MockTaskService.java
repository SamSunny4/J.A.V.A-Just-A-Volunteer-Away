package com.java.volunteer.service.mock;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.TaskHistory;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mock implementation of TaskService for testing and development
 */
public class MockTaskService implements TaskService {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, List<TaskHistory>> taskHistories = new HashMap<>();
    private int nextTaskId = 1;
    private int nextHistoryId = 1;
    
    public MockTaskService() {
        // Create some sample tasks
        createSampleTasks();
    }
    
    private void createSampleTasks() {
        // Create a pending task
        Task task1 = new Task();
        task1.setTaskId(nextTaskId++);
        task1.setTitle("Help with grocery shopping");
        task1.setDescription("Need help with weekly grocery shopping. I can't carry heavy items.");
        task1.setRequesterId(2); // Elderly user
        task1.setLocation("123 Main St, Anytown");
        task1.setScheduledDate(LocalDate.now().plusDays(2));
        task1.setScheduledTime(LocalTime.of(10, 0));
        task1.setEstimatedDuration(60);
        task1.setStatus(Task.Status.AVAILABLE);
        task1.setUrgencyLevel(Task.UrgencyLevel.MEDIUM);
        task1.setCreatedAt(LocalDateTime.now());
        tasks.put(task1.getTaskId(), task1);
        
        // Create an assigned task
        Task task2 = new Task();
        task2.setTaskId(nextTaskId++);
        task2.setTitle("Help with yard work");
        task2.setDescription("Need help with mowing the lawn and trimming bushes.");
        task2.setRequesterId(2); // Elderly user
        task2.setVolunteerId(3); // Volunteer
        task2.setLocation("123 Main St, Anytown");
        task2.setScheduledDate(LocalDate.now().plusDays(5));
        task2.setScheduledTime(LocalTime.of(14, 0));
        task2.setEstimatedDuration(120);
        task2.setStatus(Task.Status.ASSIGNED);
        task2.setUrgencyLevel(Task.UrgencyLevel.LOW);
        task2.setCreatedAt(LocalDateTime.now().minusDays(1));
        tasks.put(task2.getTaskId(), task2);
        
        // Create a completed task
        Task task3 = new Task();
        task3.setTaskId(nextTaskId++);
        task3.setTitle("Ride to doctor appointment");
        task3.setDescription("Need a ride to my doctor's appointment and back.");
        task3.setRequesterId(2); // Elderly user
        task3.setVolunteerId(3); // Volunteer
        task3.setLocation("123 Main St to 456 Medical Blvd");
        task3.setScheduledDate(LocalDate.now().minusDays(3));
        task3.setScheduledTime(LocalTime.of(9, 30));
        task3.setEstimatedDuration(90);
        task3.setStatus(Task.Status.COMPLETED);
        task3.setUrgencyLevel(Task.UrgencyLevel.HIGH);
        task3.setCreatedAt(LocalDateTime.now().minusDays(7));
        tasks.put(task3.getTaskId(), task3);
        
        // Create task histories
        createTaskHistory(task2.getTaskId(), 2, null, Task.Status.PENDING, Task.Status.AVAILABLE, "Task created", TaskHistory.ChangedBy.ELDERLY);
        createTaskHistory(task2.getTaskId(), null, 3, Task.Status.AVAILABLE, Task.Status.ASSIGNED, "Task assigned", TaskHistory.ChangedBy.VOLUNTEER);
        
        createTaskHistory(task3.getTaskId(), 2, null, Task.Status.PENDING, Task.Status.AVAILABLE, "Task created", TaskHistory.ChangedBy.ELDERLY);
        createTaskHistory(task3.getTaskId(), null, 3, Task.Status.AVAILABLE, Task.Status.ASSIGNED, "Task assigned", TaskHistory.ChangedBy.VOLUNTEER);
        createTaskHistory(task3.getTaskId(), null, 3, Task.Status.ASSIGNED, Task.Status.IN_PROGRESS, "Task started", TaskHistory.ChangedBy.VOLUNTEER);
        createTaskHistory(task3.getTaskId(), null, 3, Task.Status.IN_PROGRESS, Task.Status.COMPLETED, "Task completed", TaskHistory.ChangedBy.VOLUNTEER);
    }
    
    private void createTaskHistory(int taskId, Integer elderlyId, Integer volunteerId, 
                                  Task.Status oldStatus, Task.Status newStatus, 
                                  String notes, TaskHistory.ChangedBy changedBy) {
        TaskHistory history = new TaskHistory();
        history.setHistoryId(nextHistoryId++);
        history.setTaskId(taskId);
        history.setChangedById(elderlyId != null ? elderlyId : (volunteerId != null ? volunteerId : 0));
        history.setPreviousVolunteerId(volunteerId);
        history.setNewVolunteerId(volunteerId);
        history.setPreviousStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setNotes(notes);
        history.setChangedBy(changedBy);
        history.setChangedAt(LocalDateTime.now());
        
        if (!taskHistories.containsKey(taskId)) {
            taskHistories.put(taskId, new ArrayList<>());
        }
        taskHistories.get(taskId).add(history);
    }

    @Override
    public Task createTask(Task task) {
        task.setTaskId(nextTaskId++);
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(Task.Status.AVAILABLE);
        tasks.put(task.getTaskId(), task);
        
        // Create initial task history
        createTaskHistory(
            task.getTaskId(),
            task.getRequesterId(),
            null,
            null,
            Task.Status.AVAILABLE,
            "Task created",
            TaskHistory.ChangedBy.ELDERLY
        );
        
        return task;
    }

    @Override
    public Optional<Task> getTaskById(int taskId) {
        return Optional.ofNullable(tasks.get(taskId));
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getTasksByRequesterId(int requesterId) {
        return tasks.values().stream()
            .filter(task -> task.getRequesterId() == requesterId)
            .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksByVolunteerId(int volunteerId) {
        return tasks.values().stream()
            .filter(task -> task.getVolunteerId() != null && task.getVolunteerId() == volunteerId)
            .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksByStatus(Task.Status status) {
        return tasks.values().stream()
            .filter(task -> task.getStatus() == status)
            .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAvailableTasks() {
        return tasks.values().stream()
            .filter(task -> task.getStatus() == Task.Status.AVAILABLE)
            .collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksByScheduledDate(LocalDate date) {
        return tasks.values().stream()
            .filter(task -> task.getScheduledDate().equals(date))
            .collect(Collectors.toList());
    }

    @Override
    public Task assignTask(int taskId, int volunteerId, int changedById, TaskHistory.ChangedBy changedBy) {
        Task task = tasks.get(taskId);
        if (task != null && task.getStatus() == Task.Status.AVAILABLE) {
            task.setVolunteerId(volunteerId);
            task.setStatus(Task.Status.ASSIGNED);
            task.setUpdatedAt(LocalDateTime.now());
            
            // Create history record
            createTaskHistory(
                taskId,
                task.getRequesterId(),
                volunteerId,
                Task.Status.AVAILABLE,
                Task.Status.ASSIGNED,
                "Task assigned to volunteer",
                changedBy
            );
            
            return task;
        }
        return null;
    }

    @Override
    public Task startTask(int taskId, int volunteerId) {
        Task task = tasks.get(taskId);
        if (task != null && task.getStatus() == Task.Status.ASSIGNED && 
            task.getVolunteerId() != null && task.getVolunteerId() == volunteerId) {
            
            task.setStatus(Task.Status.IN_PROGRESS);
            task.setUpdatedAt(LocalDateTime.now());
            
            // Create history record
            createTaskHistory(
                taskId,
                null,
                volunteerId,
                Task.Status.ASSIGNED,
                Task.Status.IN_PROGRESS,
                "Task started",
                TaskHistory.ChangedBy.VOLUNTEER
            );
            
            return task;
        }
        return null;
    }

    @Override
    public Task completeTask(int taskId, int volunteerId) {
        Task task = tasks.get(taskId);
        if (task != null && 
            (task.getStatus() == Task.Status.ASSIGNED || task.getStatus() == Task.Status.IN_PROGRESS) && 
            task.getVolunteerId() != null && task.getVolunteerId() == volunteerId) {
            
            task.setStatus(Task.Status.COMPLETED);
            task.setUpdatedAt(LocalDateTime.now());
            
            // Create history record
            createTaskHistory(
                taskId,
                null,
                volunteerId,
                task.getStatus(),
                Task.Status.COMPLETED,
                "Task completed",
                TaskHistory.ChangedBy.VOLUNTEER
            );
            
            return task;
        }
        return null;
    }

    @Override
    public Task cancelTask(int taskId, int userId, TaskHistory.ChangedBy changedBy, String reason) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setStatus(Task.Status.CANCELLED);
            task.setUpdatedAt(LocalDateTime.now());
            
            // Create history record
            createTaskHistory(
                taskId,
                changedBy == TaskHistory.ChangedBy.ELDERLY ? userId : null,
                changedBy == TaskHistory.ChangedBy.VOLUNTEER ? userId : null,
                task.getStatus(),
                Task.Status.CANCELLED,
                "Task cancelled: " + reason,
                changedBy
            );
            
            return task;
        }
        return null;
    }

    @Override
    public Task reassignTask(int taskId, int requesterId, String reason) {
        Task task = tasks.get(taskId);
        if (task != null && task.getRequesterId() == requesterId && 
            task.getVolunteerId() != null &&
            (task.getStatus() == Task.Status.ASSIGNED || task.getStatus() == Task.Status.IN_PROGRESS)) {
            
            Integer previousVolunteerId = task.getVolunteerId();
            task.setPreviousVolunteerId(previousVolunteerId);
            task.setVolunteerId(null);
            task.setReassignmentReason(reason);
            task.setStatus(Task.Status.AVAILABLE);
            task.setUpdatedAt(LocalDateTime.now());
            
            // Create history record
            createTaskHistory(
                taskId,
                requesterId,
                previousVolunteerId,
                task.getStatus(),
                Task.Status.AVAILABLE,
                "Task reassigned: " + reason,
                TaskHistory.ChangedBy.ELDERLY
            );
            
            return task;
        }
        return null;
    }

    @Override
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            task.setUpdatedAt(LocalDateTime.now());
            tasks.put(task.getTaskId(), task);
            return task;
        }
        return null;
    }

    @Override
    public boolean deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            taskHistories.remove(taskId);
            return true;
        }
        return false;
    }

    @Override
    public List<TaskHistory> getTaskHistory(int taskId) {
        return taskHistories.getOrDefault(taskId, new ArrayList<>());
    }
    
    // Helper method to set User objects for tasks
    public void setUserForTask(Task task, User requester, User volunteer) {
        task.setRequester(requester);
        task.setVolunteer(volunteer);
    }
}