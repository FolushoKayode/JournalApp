package com.mofoluwashokayode.journalapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "journal")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String entry;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @Ignore
    public JournalEntry(String title, String entry, Date updatedAt) {
        this.title = title;
        this.entry = entry;

        this.updatedAt = updatedAt;
    }

    public JournalEntry(int id, String title, String entry, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.entry = entry;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
