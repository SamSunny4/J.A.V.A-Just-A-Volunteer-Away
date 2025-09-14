package com.java.volunteer.ui;

import java.util.List;

import com.java.volunteer.model.Task;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;

/**
 * Screen for elderly users to view their task requests
 */
public class MyTasksScreen extends BaseScreen {
    private final TaskService taskService;
    
    public MyTasksScreen(User currentUser, TaskService taskService) {
        super(currentUser);
        this.taskService = taskService;
    }
    
    @Override
    public BaseScreen display() {
        showHeader("My Task Requests");
        
        List<Task> tasks = taskService.getTasksByRequesterId(currentUser.getUserId());
        
        if (tasks.isEmpty()) {
            showMessage("You haven't created any task requests yet.");
            return new MainMenuScreen(currentUser, null, taskService, null);
        }
        
        displayTasks(tasks);
        
        int choice = showMenu(
            "View Task Details",
            "Create New Task",
            "Back to Main Menu"
        );
        
        switch (choice) {
            case 1:
                return viewTaskDetails(tasks);
            case 2:
                return new CreateTaskScreen(currentUser, taskService);
            case 3:
                return new MainMenuScreen(currentUser, null, taskService, null);
            case 0:
                return null;
            default:
                return this;
        }
    }
    
    private void displayTasks(List<Task> tasks) {
        System.out.println("\nYour task requests:");
        System.out.println(String.format("%-5s %-30s %-15s %-15s %-10s", 
            "ID", "Title", "Date", "Volunteer", "Status"));
        System.out.println("--------------------------------------------------------------------------------");
        
        for (Task task : tasks) {
            String volunteerName = "None";
            if (task.getVolunteerId() != null && task.getVolunteer() != null) {
                volunteerName = task.getVolunteer().getFullName();
            }
            
            System.out.println(String.format("%-5d %-30s %-15s %-15s %-10s",
                task.getTaskId(),
                truncateString(task.getTitle(), 28),
                task.getScheduledDate().toString(),
                truncateString(volunteerName, 13),
                task.getStatus()));
        }
    }
    
    private BaseScreen viewTaskDetails(List<Task> tasks) {
        int taskId = getNumericInput("\nEnter task ID to view details (0 to go back)");
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
        
        showHeader("Task Details");
        
        System.out.println("\nTitle: " + selectedTask.getTitle());
        System.out.println("Description: " + selectedTask.getDescription());
        System.out.println("Location: " + selectedTask.getLocation());
        System.out.println("Date & Time: " + selectedTask.getScheduledDate() + " " + selectedTask.getScheduledTime());
        System.out.println("Duration: " + selectedTask.getEstimatedDuration() + " minutes");
        System.out.println("Status: " + selectedTask.getStatus());
        System.out.println("Urgency: " + selectedTask.getUrgencyLevel());
        
        String volunteerInfo = "No volunteer assigned";
        if (selectedTask.getVolunteerId() != null && selectedTask.getVolunteer() != null) {
            volunteerInfo = "Name: " + selectedTask.getVolunteer().getFullName() + 
                          "\nPhone: " + selectedTask.getVolunteer().getPhoneNumber();
        }
        System.out.println("\nVolunteer Information:\n" + volunteerInfo);
        
        // Show options based on task status
        if (selectedTask.getStatus() == Task.Status.PENDING || 
            selectedTask.getStatus() == Task.Status.AVAILABLE) {
            
            if (confirm("\nWould you like to cancel this task?")) {
                taskService.cancelTask(selectedTask.getTaskId(), currentUser.getUserId(), 
                                     null, "Cancelled by requester");
                showMessage("Task has been cancelled.");
            }
        } else if ((selectedTask.getStatus() == Task.Status.ASSIGNED || 
                   selectedTask.getStatus() == Task.Status.IN_PROGRESS) &&
                   selectedTask.getVolunteerId() != null) {
            
            if (confirm("\nWould you like to manage the assigned volunteer?")) {
                return new ManageVolunteersScreen(currentUser, taskService, null);
            }
        }
        
        showMessage("Press Enter to continue...");
        return this;
    }
    
    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}