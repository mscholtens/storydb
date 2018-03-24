package model;

import java.awt.*;

/**
 * Created by Marijn on 23.12.17.
 */
public class Person {

    private String name;
    private String[] nicknames;
    private String[] altnames;
    private boolean sex; // male true, female false
    private String title;
    private boolean vip;
    private String placeOfBirth;
    private String residence;
    private String description;
    private boolean isHighlighted;
    private boolean isExpanded;

    public Person(String name, String nicknames, String altnames, String sex, String title, String vip,
                  String placeOfBirth, String residence, String description) {
        this.name = name;
        this.nicknames = nicknames.split(",");
        this.altnames = altnames.split(",");
        if(sex.equals("Male") || sex.equals("true")) {
            this.sex = true; // male
        } else {
            this.sex = false; // female
        }
        this.title = title;
        if(vip.equals("Yes") || vip.equals("true")) {
            this.vip = true; // vip
        } else {
            this.vip = false; // no vip
        }
        this.placeOfBirth = placeOfBirth;
        this.residence = residence;
        this.description = description;
        this.isHighlighted = false;
        this.isExpanded = false;
    }

    public String getNickNamesMerged() {
        return String.join(",", nicknames);
    }

    public String[] getNicknames() {
        return nicknames;
    }

    public void setNicknames(String[] nicknames) {
        this.nicknames = nicknames;
    }

    public String getAltNamesMerged() {
        return String.join(",", altnames);
    }

    public String[] getAltnames() {
        return altnames;
    }

    public void setAltnames(String[] altnames) {
        this.altnames = altnames;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getResidence() {
        return residence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void expand() {
        isExpanded = true;
    }

    public void collapse() {
        isExpanded = false;
    }
}
