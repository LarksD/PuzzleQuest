import java.io.*;

public class Player implements Serializable{
    private String name;
    private int health;
    private int gold;
    private int experience;

    public Player(String name) {
        this.name = name;
        reset();
    }

    public void reset() {
        this.health = 55;
        this.gold = 0;
        this.experience = 0;
    }

    public String getName() {
        return name;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public int getHealth() {
        return health;
    }

    public int getGold() {
        return gold;
    }

    public int getExperience() {
        return experience;
    }
}
