package ru.job4j.dreamjob.model;

import java.io.Serializable;
import java.util.Objects;

public class Candidate implements Serializable {
    private int id;
    private String name;
    private String desc;
    private String created;
    private boolean visible;
    private City city;
    private byte[] photo;

    public Candidate() {

    }

    public Candidate(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public Candidate(int id, String name, String desc, String created) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.created = created;
    }

    public Candidate(int id, String name, String desc, String created, boolean visible, City city) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.created = created;
        this.visible = visible;
        this.city = city;
    }

    public Candidate(int id, String name, String desc, String created, boolean visible, City city, byte[] photo) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.created = created;
        this.visible = visible;
        this.city = city;
        this.photo = photo;
    }

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id
                && Objects.equals(name, candidate.name)
                && Objects.equals(desc, candidate.desc)
                && Objects.equals(created, candidate.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, created);
    }
}
