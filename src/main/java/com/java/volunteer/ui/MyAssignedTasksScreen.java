package com.java.volunteer.ui;

import java.util.List;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;

/**
 * Screen for volunteers to view and manage their assigned tasks
 */
public class MyAssignedTasksScreen extends BaseScreen {
    private final TaskService taskService;
    
    public MyAssignedTasksScreen(User currentUser, TaskService taskService) {
        super(currentUser);
        this.taskService = taskService;
    }
    
    @Override
    public BaseScreen display() {
        showHeader("My Assigned Tasks");
        
        List<Task> assignedTasks = taskService.getTasksByVolunteerId(currentUser.getUserId());
        
        if (assignedTasks.isEmpty()) {
            showMessage("You don't have any assigned tasks.");
            return new MainMenuScreen(currentUser, null, taskService, null);
        }
        
        // Filter out only active tasks (not completed or cancelled)
        List<Task> activeTasks = assignedTasks.stream()
            .filter(task -> task.getStatus() == Task.Status.ASSIGNED || task.getStatus() == Task.Status.IN_PROGRESS)
            .collect(java.util.stream.Collectors.toList());
        
        // Filter completed tasks
        List<Task> completedTasks = assignedTasks.stream()
            .filter(task -> task.getStatus() == Task.Status.COMPLETED)
            .collect(java.util.stream.Collectors.toList());
            
        displayActiveTasks(activeTasks);
        
        if (!completedTasks.isEmpty()) {
            System.out.println("\nRecently completed tasks:");
            System.out.println(String.format("%-5s %-30s %-15s", 
                "ID", "Title", "Date"));
            System.out.println("----------------------------------------------------");
            
            for (Task task : completedTasks.stream().limit(5).collect(java.util.stream.Collectors.toList())) {
                System.out.println(String.format("%-5d %-30s %-15s",
                    task.getTaskId(),
                    truncateString(task.getTitle(), 28),
                    task.getScheduledDate().toString()));
            }
        }
        
        if (activeTasks.isEmpty()) {
            int choice = showMenu(
                "Find Available Tasks",
                "Back to Main Menu"
            );
            
            switch (choice) {
                case 1:
                    return new AvailableTasksScreen(currentUser, taskService);
                case 2:
                case 0:
                    return new MainMenuScreen(currentUser, null, taskService, null);
                default:
                    return this;
            }
        } else {
            int choice = showMenu(
                "Manage Task",
                "Find More Tasks",
                "Back to Main Menu"
            );
            
            switch (choice) {
                case 1:
                    return manageTask(activeTasks);
                case 2:
                    return new AvailableTasksScreen(currentUser, taskService);
                case 3:
                case 0:
                    return new MainMenuScreen(currentUser, null, taskService, null);
                default:
                    return this;
            }
        }
    }
    
    private void displayActiveTasks(List<Task> tasks) {
        System.out.println("\nYour active tasks:");
        System.out.println(String.format("%-5s %-30s %-15s %-10s", 
            "ID", "Title", "Date", "Status"));
        System.out.println("----------------------------------------------------");
        
        for (Task task : tasks) {
            System.out.println(String.format("%-5d %-30s %-15s %-10s",
                task.getTaskId(),
                truncateString(task.getTitle(), 28),
                task.getScheduledDate().toString(),
                task.getStatus()));
        }
    }
    
    private BaseScreen manageTask(List<Task> tasks) {
        int taskId = getNumericInput("\nEnter task ID to manage (0 to go back)");
        if (taskId == 0) {
            return this;
        }
        
        Task selectedTask = tasks.stream()
            .filter(task -> task.getTaskId() == taskId)
            .findFirst()
            .orElse(null);
        
        if (selectedTask == null) {
            showMessage("Invalid task ID selected.");
            return this;
        }
        
        return taskDetailsScreen(selectedTask);
    }
    
    private BaseScreen taskDetailsScreen(Task task) {
        showHeader("Task Details");
        
        System.out.println("\nTitle: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Location: " + task.getLocation());
        System.out.println("Date & Time: " + task.getScheduledDate() + " " + task.getScheduledTime());
        System.out.println("Duration: " + task.getEstimatedDuration() + " minutes");
        System.out.println("Status: " + task.getStatus());
        System.out.println("Urgency: " + task.getUrgencyLevel());
        
        if (task.getRequester() != null) {
            System.out.println("\nRequested by: " + task.getRequester().getFullName());
            System.out.println("Contact: " + task.getRequester().getPhoneNumber());
        }
        
        String[] options;
        if (task.getStatus() == Task.Status.ASSIGNED) {
            options = new String[]{"Start Task", "Contact Requester", "Back"};
        } else if (task.getStatus() == Task.Status.IN_PROGRESS) {
            options = new String[]{"Complete Task", "Contact Requester", "Back"};
        } else {
            options = new String[]{"Contact Requester", "Back"};
        }
        
        int choice = showMenu(options);
        
        if (task.getStatus() == Task.Status.ASSIGNED && choice == 1) {
            return startTask(task);
        } else if (task.getStatus() == Task.Status.IN_PROGRESS && choice == 1) {
            return completeTask(task);
        } else if ((task.getStatus() == Task.Status.ASSIGNED && choice == 2) || 
                  (task.getStatus() == Task.Status.IN_PROGRESS && choice == 2) ||
                  choice == 1) {
            showMessage("Contact information for " + task.getRequester().getFullName() + ":\n" +
                      "Phone: " + task.getRequester().getPhoneNumber() + "\n" +
                      "Email: " + task.getRequester().getEmail());
            return taskDetailsScreen(task);
        } else {
            return this;
        }
    }
    
    private BaseScreen startTask(Task task) {
        if (confirm("Are you sure you want to start this task now?")) {
            Task updatedTask = taskService.startTask(task.getTaskId(), currentUser.getUserId());
            
            if (updatedTask != null) {
                showMessage("Task has been started. Good luck!");
                return this;
            } else {
                showMessage("Failed to start the task.");
                return taskDetailsScreen(task);
            }
        }
        return taskDetailsScreen(task);
    }
    
    private BaseScreen completeTask(Task task) {
        if (confirm("Have you completed this task? This will be recorded in your volunteer points.")) {
            Task updatedTask = taskService.completeTask(task.getTaskId(), currentUser.getUserId());
            
            if (updatedTask != null) {
                showMessage("Task has been marked as completed. Thank you for volunteering!");
                return this;
            } else {
                showMessage("Failed to complete the task.");
                return taskDetailsScreen(task);
            }
        }
        return taskDetailsScreen(task);
    }
    
    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}