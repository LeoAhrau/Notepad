package com.example.notepad;

public class Notes {

    String title;
    String content;

    public Notes(String title, String content) {
        this.title = title;
        this.content = this.content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return title;
    }
}