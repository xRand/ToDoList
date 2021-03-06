package com.example.ilja.myapplication;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "Items")
public class ListItems extends Model {

    @Column(name = "box")
    boolean box;
    @Column(name = "todo")
    String todo;
    @Column(name = "created")
    String created;
    @Column(name = "done")
    String done;
    @Column(name = "photo")
    byte[] photo;

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public ListItems(){
        super();
    }
    public ListItems(boolean box, String todo, String created, String done, byte[] photo) {
        super();
        this.box = box;
        this.todo = todo;
        this.created = created;
        this.done = done;
        this.photo = photo;
    }

}
