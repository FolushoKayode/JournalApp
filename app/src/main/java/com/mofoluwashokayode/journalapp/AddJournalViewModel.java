package com.mofoluwashokayode.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mofoluwashokayode.journalapp.database.AppDatabase;
import com.mofoluwashokayode.journalapp.database.JournalEntry;


public class AddJournalViewModel extends ViewModel {


    private LiveData<JournalEntry> journal;


    public AddJournalViewModel(AppDatabase database, int journalId) {
        journal = database.journalDao().loadJournalById(journalId);
    }


    public LiveData<JournalEntry> getJournal() {
        return journal;
    }
}
