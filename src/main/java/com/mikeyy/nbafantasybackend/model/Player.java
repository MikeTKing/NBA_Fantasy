package com.mikeyy.nbafantasybackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "name", unique = true)
    private String name;

    private String team;
    private String position;
    private Integer age;
    private Double pts;
    private Double reb;
    private Double ast;
    private Double stl;
    private Double blk;
    @Column(name = "`to`")
    private Double to;
    private Double pf;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, String team, String position, Integer age,
                  Double pts, Double reb, Double ast, Double stl, Double blk,
                  Double to, Double pf) {
        this.name = name;
        this.team = team;
        this.position = position;
        this.age = age;
        this.pts = pts;
        this.reb = reb;
        this.ast = ast;
        this.stl = stl;
        this.blk = blk;
        this.to = to;
        this.pf = pf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getPts() {
        return pts;
    }

    public void setPts(Double pts) {
        this.pts = pts;
    }

    public Double getReb() {
        return reb;
    }

    public void setReb(Double reb) {
        this.reb = reb;
    }

    public Double getAst() {
        return ast;
    }

    public void setAst(Double ast) {
        this.ast = ast;
    }

    public Double getStl() {
        return stl;
    }

    public void setStl(Double stl) {
        this.stl = stl;
    }

    public Double getBlk() {
        return blk;
    }

    public void setBlk(Double blk) {
        this.blk = blk;
    }

    public Double getTo() {
        return to;
    }

    public void setTo(Double to) {
        this.to = to;
    }

    public Double getPf() {
        return pf;
    }

    public void setPf(Double pf) {
        this.pf = pf;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", position='" + position + '\'' +
                ", age=" + age +
                ", pts=" + pts +
                ", reb=" + reb +
                ", ast=" + ast +
                ", stl=" + stl +
                ", blk=" + blk +
                ", to=" + to +
                ", pf=" + pf +
                '}';
    }
}