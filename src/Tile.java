import java.io.*;

public class Tile implements Serializable{
    private int type; // 0: Skull, 1: Red, 2: Blue, 3: Green, 4: Yellow, 5: Gold, 6: Experience

    public Tile(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) { //Override no metodo equals para comparar o tipo do objeto
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tile tile = (Tile) obj;
        return type == tile.type;
    }

    @Override
    public String toString() {
        switch (type) {
            case 0: return "💀";
            case 1: return "🔴";
            case 2: return "🔵";
            case 3: return "🟢";
            case 4: return "🟡";
            case 5: return "💰";
            case 6: return "✨";
            default: return " ";
        }
    }
}
