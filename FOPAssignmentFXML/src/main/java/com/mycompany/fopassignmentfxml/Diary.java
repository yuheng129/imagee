package com.mycompany.fopassignmentfxml;

import java.time.LocalDate;

public class Diary {
    private LocalDate date;
    private String title;
    private String mood;
    private String content;
    private String status;
    private String lastModifed;

    public Diary() {
        date = null;
        title = null;
        mood = null;
        content = null;
        status = "Active";
        lastModifed = null;
    }

    public Diary (LocalDate date, String title, String mood, String content) {
        this.date = date;
        this.title = title;
        this.mood = mood;
        this.content = content;
        status = "Active";
        lastModifed = "null";
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getMood() {
        return mood;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getLastModified() {
        return lastModifed;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastModified(String lastModified) {
        this.lastModifed = lastModified;
    }

    @Override
    public String toString() {
        return String.format(DiarySystem.localDateToString(this.date) + "|" + this.title + "|" + this.mood + "|" + this.content + "|" + this.status + "|" + this.lastModifed);
    }
}
