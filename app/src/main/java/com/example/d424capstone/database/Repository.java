package com.example.d424capstone.database;

import android.app.Application;

import com.example.d424capstone.dao.HealthNoteDAO;
import com.example.d424capstone.entities.HealthNote;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Repository {

    private final HealthNoteDAO healthNoteDAO;
    private final ExecutorService executorService;

    // initializes the DAO and background thread executor
    public Repository(Application application) {
        HealthDatabaseBuilder db = HealthDatabaseBuilder.getDatabase(application);
        healthNoteDAO = db.healthNoteDAO();
        // Executor with a single thread for queueing database operations safely
        executorService = Executors.newSingleThreadExecutor();
    }

     // get all health notes from the database asynchronously.
    public List<HealthNote> getAllNotes() {
        Future<List<HealthNote>> future = executorService.submit(new Callable<List<HealthNote>>() {
            @Override
            public List<HealthNote> call() throws Exception {
                return healthNoteDAO.getAllNotes();
            }
        });
        try {
            return future.get(); // Wait for background thread to finish and return results
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

      // insert a new health note asynchronously.
    public void insert(final HealthNote healthNote) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                healthNoteDAO.insert(healthNote);
            }
        });
    }

    // update an existing health note asynchronously.
    public void update(final HealthNote healthNote) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                healthNoteDAO.update(healthNote);
            }
        });
    }

    // delete a health note asynchronously.
    public void delete(final HealthNote healthNote) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                healthNoteDAO.delete(healthNote);
            }
        });
    }

    // get health notes filtered by type asynchronously.
    public List<HealthNote> getNotesByType(final String type) {
        Future<List<HealthNote>> future = executorService.submit(new Callable<List<HealthNote>>() {
            @Override
            public List<HealthNote> call() throws Exception {
                return healthNoteDAO.getNotesByType(type);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    // search health notes asynchronously.
    public List<HealthNote> searchNotes(final String query) {
        Future<List<HealthNote>> future = executorService.submit(new Callable<List<HealthNote>>() {
            @Override
            public List<HealthNote> call() throws Exception {
                return healthNoteDAO.searchNotes(query);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
