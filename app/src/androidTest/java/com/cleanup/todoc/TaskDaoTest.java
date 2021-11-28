package com.cleanup.todoc;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private TodocDatabase database;

    private static Long PROJECT_ID = 1L;
    private static Project PROJECT_DEMO = new Project(1L,"demo",123);
    private static Task TASK_DEMO1 = new Task(1, 1L,"demo1",124);
    private static Task TASK_DEMO2 = new Task(2,1L,"demo2",125);
    private static Task TASK_DEMO3 = new Task(3,1L,"demo3",126);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetProject() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        Project project = this.database.projectDao().getProject(PROJECT_ID);
        assertTrue(project.getName().equals(PROJECT_DEMO.getName()) && project.getId() == PROJECT_ID);
    }

    @Test
    public void getItemsWhenNoItemInserted() throws InterruptedException {
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndGetTasks() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO1);
        this.database.taskDao().insertTask(TASK_DEMO2);
        this.database.taskDao().insertTask(TASK_DEMO3);

        List<Task> items = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertEquals(3, items.size());
    }

    @Test
    public void insertAndUpdateTasks() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO1);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        this.database.taskDao().updateTask(taskAdded);

        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.size() == 1 && tasks.get(0).getName().equals("demo1"));
    }

    @Test
    public void insertAndDeleteItem() throws InterruptedException {
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().insertTask(TASK_DEMO1);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        this.database.taskDao().deleteTask(taskAdded.getId());

        List<Task> items = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(items.isEmpty());
    }
}
