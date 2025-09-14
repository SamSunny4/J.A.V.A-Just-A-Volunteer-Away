package com.java.volunteer.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;

/**
 * Screen for creating a new task request as an elderly user
 */
public class CreateTaskScreen extends BaseScreen {
    private final TaskService taskService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public CreateTaskScreen(User currentUser, TaskService taskService) {
        super(currentUser);
        this.taskService = taskService;
    }
    
    @Override
    public BaseScreen display() {
        showHeader("Create a New Task Request");
        
        // Build task object
        Task task = new Task();
        task.setRequesterId(currentUser.getUserId());
        task.setStatus(Task.Status.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        
        // Get task details
        String title = getRequiredInput("Task title");
        task.setTitle(title);
        
        String description = getRequiredInput("Task description (be specific about what help you need)");
        task.setDescription(description);
        
        String location = getRequiredInput("Location (where should the volunteer come to?)");
        task.setLocation(location);
        
        // Get and validate date
        LocalDate date = null;
        while (date == null) {
            try {
                String dateStr = getRequiredInput("Date (YYYY-MM-DD)");
                date = LocalDate.parse(dateStr, DATE_FORMATTER);
                
                // Validate that date is not in the past
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Date cannot be in the past. Please enter a current or future date.");
                    date = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
        
        // Get and validate time
        LocalTime time = null;
        while (time == null) {
            try {
                String timeStr = getRequiredInput("Time (24-hour format, HH:MM)");
                time = LocalTime.parse(timeStr, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use 24-hour format (HH:MM).");
            }
        }
        
        // Set date and time
        task.setScheduledDate(date);
        task.setScheduledTime(time);
        
        // Get estimated duration
        int duration = getNumericInput("Estimated duration (in minutes)");
        task.setEstimatedDuration(duration);
        
        // Optional urgency level
        System.out.println("\nUrgency Level:");
        System.out.println("1. Low");
        System.out.println("2. Medium");
        System.out.println("3. High");
        int urgencyChoice = getNumericInput("Select urgency level (1-3)");
        
        Task.UrgencyLevel urgencyLevel;
        switch (urgencyChoice) {
            case 1:
                urgencyLevel = Task.UrgencyLevel.LOW;
                break;
            case 3:
                urgencyLevel = Task.UrgencyLevel.HIGH;
                break;
            default:
                urgencyLevel = Task.UrgencyLevel.MEDIUM;
        }
        task.setUrgencyLevel(urgencyLevel);
        
        // Confirm creation
        System.out.println("\nTask Summary:");
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Location: " + task.getLocation());
        System.out.println("Date & Time: " + task.getScheduledDate() + " " + task.getScheduledTime());
        System.out.println("Duration: " + task.getEstimatedDuration() + " minutes");
        System.out.println("Urgency: " + task.getUrgencyLevel().toString());
        
        if (confirm("Do you want to create this task request?")) {
            Task createdTask = taskService.createTask(task);
            if (createdTask != null) {
                showMessage("Task request created successfully! Task ID: " + createdTask.getTaskId() + 
                          "\nVolunteers will be able to see your request.");
            } else {
                showMessage("Failed to create task request. Please try again.");
            }
        } else {
            showMessage("Task creation cancelled.");
        }
        
        return new MainMenuScreen(currentUser, null, taskService, null);
    }
    

}