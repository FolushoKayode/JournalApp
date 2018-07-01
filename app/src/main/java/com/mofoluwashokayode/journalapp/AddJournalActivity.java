/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mofoluwashokayode.journalapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mofoluwashokayode.journalapp.database.AppDatabase;
import com.mofoluwashokayode.journalapp.database.JournalEntry;

import java.util.Date;


public class AddJournalActivity extends BaseActivity {

    // Extra for the journal ID to be received in the intent
    public static final String EXTRA_JOURNAL_ID = "extraJournalId";
    // Extra for the journal ID to be received after rotation
    public static final String INSTANCE_JOURNAL_ID = "instanceJournalId";

    // Constant for default journal id to be used when not in update mode
    private static final int DEFAULT_JOURNAL_ID = -1;
    // Constant for logging
    private static final String TAG = AddJournalActivity.class.getSimpleName();
    // Fields for views
    EditText mEditTextTitle;
    EditText mEditTextEntry;


    private int mJournalId = DEFAULT_JOURNAL_ID;

    // Member variable for the Database
    private AppDatabase mDb;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);


        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            setTitle("Update Journal");

            if (mJournalId == DEFAULT_JOURNAL_ID) {
                // populate the UI
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);


                AddJournalViewModelFactory factory = new AddJournalViewModelFactory(mDb, mJournalId);

                final AddJournalViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddJournalViewModel.class);


                viewModel.getJournal().observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journalEntry) {
                        viewModel.getJournal().removeObserver(this);
                        populateUI(journalEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditTextTitle = findViewById(R.id.editTextJournalTitle);
        mEditTextEntry = findViewById(R.id.editTextJournalEntry);


    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param journal the journalEntry to populate the UI
     */
    private void populateUI(JournalEntry journal) {
        if (journal == null) {
            return;
        }

        mEditTextTitle.setText(journal.getTitle());
        mEditTextEntry.setText(journal.getEntry());

    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new journal data into the underlying database.
     */
    public void onSaveMenuClicked() {

        //hide keyboard
        hideKeyboard(this);
        String title = mEditTextTitle.getText().toString();
        String entry = mEditTextEntry.getText().toString();

        Date date = new Date();

        final JournalEntry journal = new JournalEntry(title, entry, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mJournalId == DEFAULT_JOURNAL_ID) {
                    // insert new journal
                    mDb.journalDao().insertJournal(journal);
                } else {
                    //update journal
                    journal.setId(mJournalId);
                    mDb.journalDao().updateJournal(journal);
                }
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_add_journal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "save" menu option
            case R.id.action_save:
                onSaveMenuClicked();

                return true;
            // Respond to a click on the "signout" menu option
            case R.id.action_signout:
                UserSignOutFunction();


                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (JournalActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
