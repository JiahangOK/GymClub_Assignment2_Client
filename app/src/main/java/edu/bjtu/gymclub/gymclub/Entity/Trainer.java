package edu.bjtu.gymclub.gymclub.Entity;

import android.graphics.Bitmap;

public class Trainer {
    private String trainer_name;
    private String trainer_image;
    private String trainer_intro;

    public Trainer(String trainer_name, String trainer_image,String trainer_intro){
        this.trainer_name = trainer_name;
        this.trainer_image = trainer_image;
        this.trainer_intro = trainer_intro;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public void setTrainer_image(String trainer_image) {
        this.trainer_image = trainer_image;
    }

    public String getTrainer_image() {
        return trainer_image;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_intro(String trainer_intro) {
        this.trainer_intro = trainer_intro;
    }

    public String getTrainer_intro() {
        return trainer_intro;
    }
}
