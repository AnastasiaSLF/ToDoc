package com.cleanup.todoc.viewModel;

import static com.cleanup.todoc.viewModel.TaskViewModel.SortTaskList.DEFAULT;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;
    private LiveData<List<Project>> projects;

    public enum SortTaskList {
        DEFAULT,
        TASKS_Alphabetical,
        TASKS_Inverted,
        TASKS_FirstRecent,
        TASKS_FirstOld
    }
    public SortTaskList sortTaskList = DEFAULT;


    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
    }
    public LiveData<List<Task>> getTaskSorted() {
        switch (sortTaskList) {
            case TASKS_Alphabetical:
                return taskDataSource.getTasksAlphabetical();
            case TASKS_Inverted:
                return taskDataSource.getTasksInverted();
            case TASKS_FirstRecent:
                return taskDataSource.getTasksRecentFirst();
            case TASKS_FirstOld:
                return taskDataSource.getTasksOldFirst();
            default:
                return taskDataSource.getTasks();
        }
    }

    public LiveData<List<Project>> getProjects() {
        return projects;
    }

    public LiveData<List<Task>> getTasks() {
        return taskDataSource.getTasks();
    }

    public void createTask(final Task task) {
        executor.execute(() -> taskDataSource.createTask(task));
    }

    public void deleteTask(final Task task) {
        executor.execute(() -> taskDataSource.deleteTask(task.getId()));
    }


    public void init() {
        if (projects == null) {
            projects = projectDataSource.getProjects();
        }
    }

}
