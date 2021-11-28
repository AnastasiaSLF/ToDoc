package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskDataRepository {
    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public LiveData<List<Task>> getTasks(){ return this.taskDao.getTasks(); }

    public void createTask(Task task){ taskDao.insertTask(task); }

    public void deleteTask(long id){ taskDao.deleteTask(id); }


    public LiveData<List<Task>> getTasksAlphabetical() {
        return this.taskDao.getTasksAlphabeticalAZ();
    }

    public LiveData<List<Task>> getTasksInverted() {
        return this.taskDao.getTasksAlphabeticalZA();
    }

    public LiveData<List<Task>> getTasksRecentFirst() {
        return this.taskDao.getTasksNewToOld();
    }

    public LiveData<List<Task>> getTasksOldFirst() {
        return this.taskDao.getTasksOldToNew();
    }

}
