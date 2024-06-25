import java.io.*;

public class Player implements Serializable{
    private String name;
    private int health;
    private int maxHealth;
    private int gold;
    private int experience;
    private boolean doubleDamage = false;

    public Player(String name) {
        this.name = name;
        reset();
    }

    public void reset() {
        this.health = 55;
        this.gold = 0;
        this.experience = 0;
    }

    public void setDoubleDamage(boolean doubleDamage) {
        this.doubleDamage = doubleDamage;
    }

    public boolean getDoubleDamage() {
        return doubleDamage;
    }

    public String getName() {
        return name;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public void removeExperience(int experience) {
        this.experience -= experience;
    }

    public void addHealth(int health) {
        if (this.health + health > this.maxHealth) {
            this.health = this.maxHealth;
        } else {
            this.health += health;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getGold() {
        return gold;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getExperience() {
        return experience;
    }
}
