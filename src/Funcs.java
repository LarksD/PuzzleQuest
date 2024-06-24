import java.io.*;

public class Funcs {
    public static void saveGame(Game game, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(game);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Game loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Game) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteGame(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
