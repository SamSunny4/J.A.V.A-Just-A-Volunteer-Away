package com.java.volunteer.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.dao.TaskDao;
import com.java.volunteer.dao.TaskHistoryDao;
import com.java.volunteer.dao.UserDao;
import com.java.volunteer.dao.UserPointsDao;
import com.java.volunteer.model.Task;
import com.java.volunteer.model.TaskHistory;
import com.java.volunteer.model.User;
import com.java.volunteer.service.TaskService;

/**
 * Implementation of TaskService
 */
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskDao taskDao;
    private final TaskHistoryDao taskHistoryDao;
    private final UserDao userDao;
    private final UserPointsDao userPointsDao;
    
    public TaskServiceImpl(TaskDao taskDao, TaskHistoryDao taskHistoryDao, 
                          UserDao userDao, UserPointsDao userPointsDao) {
        this.taskDao = taskDao;
        this.taskHistoryDao = taskHistoryDao;
        this.userDao = userDao;
        this.userPointsDao = userPointsDao;
    }

    @Override
    public Task createTask(Task task) {
        // Make sure the requester exists
        Optional<User> requester = userDao.findById(task.getRequesterId());
        if (!requester.isPresent()) {
            throw new IllegalArgumentException("Requester not found: " + task.getRequesterId());
        }
        
        // Set initial status
        task.setStatus(Task.Status.AVAILABLE);
        
        logger.info("Creating task: {}", task.getTitle());
        return taskDao.create(task);
    }

    @Override
    public Optional<Task> getTaskById(int taskId) {
        return taskDao.findById(taskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskDao.findAll();
    }

    @Override
    public List<Task> getTasksByRequesterId(int requesterId) {
        return taskDao.findByRequesterId(requesterId);
    }

    @Override
    public List<Task> getTasksByVolunteerId(int volunteerId) {
        return taskDao.findByVolunteerId(volunteerId);
    }

    @Override
    public List<Task> getTasksByStatus(Task.Status status) {
        return taskDao.findByStatus(status);
    }

    @Override
    public List<Task> getAvailableTasks() {
        return taskDao.findAvailableTasks();
    }

    @Override
    public List<Task> getTasksByScheduledDate(LocalDate date) {
        return taskDao.findByScheduledDate(date);
    }

    @Override
    public Task assignTask(int taskId, int volunteerId, int changedById, TaskHistory.ChangedBy changedBy) {
        Optional<Task> optTask = taskDao.findById(taskId);
        if (!optTask.isPresent()) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        
        Task task = optTask.get();
        if (task.getStatus() != Task.Status.AVAILABLE) {
            throw new IllegalStateException("Task is not available for assignment: " + taskId);
        }
        
        // Make sure the volunteer exists
        Optional<User> volunteer = userDao.findById(volunteerId);
        if (!volunteer.isPresent()) {
            throw new IllegalArgumentException("Volunteer not found: " + volunteerId);
        }
        
        // Record the previous status
        Task.Status previousStatus = task.getStatus();
        
        // Assign the task to the volunteer
        boolean success = taskDao.assignTask(taskId, volunteerId);
        if (!success) {
            throw new RuntimeException("Failed to assign task: " + taskId);
        }
        
        // Reload the task
        task = taskDao.findById(taskId).orElseThrow(() -> new RuntimeException("Failed to reload task: " + taskId));
        
        // Record the change in task history
        taskHistoryDao.recordStatusChange(
            taskId,
            changedById,
            changedBy,
            previousStatus,
            task.getStatus(),
            "Task assigned to volunteer: " + volunteer.get().getUsername()
        );
        
        logger.info("Task {} assigned to volunteer {}", taskId, volunteerId);
        return task;
    }

    @Override
    public Task startTask(int taskId, int volunteerId) {
        Optional<Task> optTask = taskDao.findById(taskId);
        if (!optTask.isPresent()) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        
        Task task = optTask.get();
        
        // Check if the task is assigned to this volunteer
        if (task.getVolunteerId() == null || task.getVolunteerId() != volunteerId) {
            throw new IllegalStateException("Task is not assigned to this volunteer: " + volunteerId);
        }
        
        if (task.getStatus() != Task.Status.ASSIGNED) {
            throw new IllegalStateException("Task is not in ASSIGNED status: " + taskId);
        }
        
        // Record the previous status
        Task.Status previousStatus = task.getStatus();
        
        // Change status to IN_PROGRESS
        boolean success = taskDao.updateStatus(taskId, Task.Status.IN_PROGRESS);
        if (!success) {
            throw new RuntimeException("Failed to start task: " + taskId);
        }
        
        // Reload the task
        task = taskDao.findById(taskId).orElseThrow(() -> new RuntimeException("Failed to reload task: " + taskId));
        
        // Record the change in task history
        taskHistoryDao.recordStatusChange(
            taskId,
            volunteerId,
            TaskHistory.ChangedBy.VOLUNTEER,
            previousStatus,
            task.getStatus(),
            "Task started by volunteer"
        );
        
        logger.info("Task {} started by volunteer {}", taskId, volunteerId);
        return task;
    }

    @Override
    public Task completeTask(int taskId, int volunteerId) {
        Optional<Task> optTask = taskDao.findById(taskId);
        if (!optTask.isPresent()) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        
        Task task = optTask.get();
        
        // Check if the task is assigned to this volunteer
        if (task.getVolunteerId() == null || task.getVolunteerId() != volunteerId) {
            throw new IllegalStateException("Task is not assigned to this volunteer: " + volunteerId);
        }
        
        if (task.getStatus() != Task.Status.IN_PROGRESS && task.getStatus() != Task.Status.ASSIGNED) {
            throw new IllegalStateException("Task cannot be completed from current status: " + task.getStatus());
        }
        
        // Record the previous status
        Task.Status previousStatus = task.getStatus();
        
        // Change status to COMPLETED
        boolean success = taskDao.updateStatus(taskId, Task.Status.COMPLETED);
        if (!success) {
            throw new RuntimeException("Failed to complete task: " + taskId);
        }
        
        // Update volunteer points
        userPointsDao.addTaskCompletion(volunteerId, task.getEstimatedDuration());
        
        // Reload the task
        task = taskDao.findById(taskId).orElseThrow(() -> new RuntimeException("Failed to reload task: " + taskId));
        
        // Record the change in task history
        taskHistoryDao.recordStatusChange(
            taskId,
            volunteerId,
            TaskHistory.ChangedBy.VOLUNTEER,
            previousStatus,
            task.getStatus(),
            "Task completed by volunteer"
        );
        
        logger.info("Task {} completed by volunteer {}", taskId, volunteerId);
        return task;
    }

    @Override
    public Task cancelTask(int taskId, int userId, TaskHistory.ChangedBy changedBy, String reason) {
        Optional<Task> optTask = taskDao.findById(taskId);
        if (!optTask.isPresent()) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        
        Task task = optTask.get();
        
        // Cannot cancel if already completed
        if (task.getStatus() == Task.Status.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed task: " + taskId);
        }
        
        // Record the previous status
        Task.Status previousStatus = task.getStatus();
        
        // Change status to CANCELLED
        boolean success = taskDao.updateStatus(taskId, Task.Status.CANCELLED);
        if (!success) {
            throw new RuntimeException("Failed to cancel task: " + taskId);
        }
        
        // Reload the task
        task = taskDao.findById(taskId).orElseThrow(() -> new RuntimeException("Failed to reload task: " + taskId));
        
        // Record the change in task history
        taskHistoryDao.recordStatusChange(
            taskId,
            userId,
            changedBy,
            previousStatus,
            task.getStatus(),
            "Task cancelled. Reason: " + reason
        );
        
        logger.info("Task {} cancelled by {} {}", taskId, changedBy, userId);
        return task;
    }

    @Override
    public Task reassignTask(int taskId, int requesterId, String reason) {
        Optional<Task> optTask = taskDao.findById(taskId);
        if (!optTask.isPresent()) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        
        Task task = optTask.get();
        
        // Check if the requester is the one who created the task
        if (task.getRequesterId() != requesterId) {
            throw new IllegalStateException("Only the task requester can reassign the task: " + requesterId);
        }
        
        // Cannot reassign if already completed or cancelled
        if (task.getStatus() == Task.Status.COMPLETED || task.getStatus() == Task.Status.CANCELLED) {
            throw new IllegalStateException("Cannot reassign task with status: " + task.getStatus());
        }
        
        // Store information for history
        Task.Status previousStatus = task.getStatus();
        Integer previousVolunteerId = task.getVolunteerId();
        
        // Reassign the task (removes volunteer)
        boolean success = taskDao.reassignTask(taskId, reason);
        if (!success) {
            throw new RuntimeException("Failed to reassign task: " + taskId);
        }
        
        // Update volunteer points if there was a volunteer
        if (previousVolunteerId != null) {
            userPointsDao.addTaskReassignment(previousVolunteerId);
        }
        
        // Reload the task
        task = taskDao.findById(taskId).orElseThrow(() -> new RuntimeException("Failed to reload task: " + taskId));
        
        // Record the change in task history
        taskHistoryDao.recordVolunteerReassignment(
            taskId,
            requesterId,
            TaskHistory.ChangedBy.ELDERLY,
            previousStatus,
            task.getStatus(),
            previousVolunteerId,
            null, // New volunteer ID is null
            reason,
            "Volunteer removed by requester"
        );
        
        logger.info("Task {} reassigned by requester {}", taskId, requesterId);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        // Make sure the task exists
        Optional<Task> existingTask = taskDao.findById(task.getTaskId());
        if (!existingTask.isPresent()) {
            throw new IllegalArgumentException("Task not found: " + task.getTaskId());
        }
        
        logger.info("Updating task: {}", task.getTaskId());
        return taskDao.update(task);
    }

    @Override
    public boolean deleteTask(int taskId) {
        // Only allow deletion if task is PENDING or AVAILABLE
        Optional<Task> task = taskDao.findById(taskId);
        if (!task.isPresent()) {
            return false;
        }
        
        if (task.get().getStatus() != Task.Status.PENDING && task.get().getStatus() != Task.Status.AVAILABLE) {
            throw new IllegalStateException("Cannot delete task with status: " + task.get().getStatus());
        }
        
        logger.info("Deleting task: {}", taskId);
        return taskDao.deleteById(taskId);
    }

    @Override
    public List<TaskHistory> getTaskHistory(int taskId) {
        return taskHistoryDao.findByTaskId(taskId);
    }
}