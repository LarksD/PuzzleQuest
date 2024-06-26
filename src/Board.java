import java.util.Random;
import java.io.*;

public class Board implements Serializable{
    private Tile[][] tiles;
    private Random random;
    private Game game;

    public Board(Game game) {
        this.game = game;
        tiles = new Tile[8][8];
        random = new Random();
    }

    public void initialize() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(random.nextInt(7));
            }
        }
        shuffleBoard();
    }

    // 0: Skull, 1: Red, 2: Blue, 3: Green, 4: Yellow, 5: Gold, 6: Experience
    public void calculos(int length, Tile current, Game game) { // quebrar e procurar matches dnv
        Player opponent = game.getOpponent(game.getCurrentPlayer());
        if (current.getType() == 0) { // reduz a vida do opponente em 1
            if (game.getCurrentPlayer().getDoubleDamage()) {
                opponent.takeDamage(2 * length);
                game.getCurrentPlayer().setDoubleDamage(false);
            } else {
                opponent.takeDamage(1 * length);
            }
        } else if (current.getType() == 1) { // Aumenta os pontos de vida do jogador, 1 ponto por esfera.
            game.getCurrentPlayer().addHealth(1 * length);
        } else if (current.getType() == 2) { // Um jogo de esferas azuis transforma todas as esferas vermelhas em caveiras.
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (tiles[i][j].getType() == 1) {
                        tiles[i][j] = new Tile(0);
                    }
                }
            }
        } else if (current.getType() == 3) { // Transforma todas as caveiras em esferas vermelhas.
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (tiles[i][j].getType() == 0) {
                        tiles[i][j] = new Tile(1);
                    }
                }
            }
        } else if (current.getType() == 4) { // Esvazia o ouro do inimigo.
            opponent.setGold(0);
        } else if (current.getType() == 5) { // Ouro: Adiciona 1x ouro. Quando atingir 10x de ouro, você causará dano dobrado no seu próximo turno e seu ouro se tornará zero.
            game.getCurrentPlayer().setGold(game.getCurrentPlayer().getGold() + 1);

            if (game.getCurrentPlayer().getGold() >= 10) {
                game.getCurrentPlayer().setGold(game.getCurrentPlayer().getGold() - 10);
                game.getCurrentPlayer().setDoubleDamage(true);

            }

        } else if (current.getType() == 6) { // A cada 10x de experiência, a vida máxima do seu inimigo é permanentemente reduzida em 10 pontos (e sua experiência é zerada).
            game.getCurrentPlayer().addExperience(1);

            if (game.getCurrentPlayer().getExperience() >= 10) {
                game.getCurrentPlayer().removeExperience(10);
                opponent.setMaxHealth(opponent.getMaxHealth() - 10);
            }

        }

    }

    public void quebrar(int row, int col, int length, int direction) { // quebrar as peças, remover e adicionar novas
        if (direction == 0) { // Horizontal
            // Trocar combos horizontais por null
            for (int i = 0; i < length; i++) {
                tiles[row][col + i] = null;
            }
            // Mover as peças para baixo
            for (int i = row; i > 0; i--) {
                for (int j = col; j < col + length; j++) {
                    tiles[i][j] = tiles[i - 1][j];
                }
            }
            // Gerar novas peças no topo
            for (int j = col; j < col + length; j++) {
                tiles[0][j] = new Tile(random.nextInt(7));
            }
        } else { // Vertical
            // Trocar combos verticais por null
            for (int i = 0; i < length; i++) {
                tiles[row + i][col] = null;
            }
            // Mover as peças para baixo
            for (int i = row + length - 1; i >= length; i--) {
                tiles[i][col] = tiles[i - length][col];
            }
            // Gerar novas peças no topo
            for (int i = 0; i < length; i++) {
                tiles[i][col] = new Tile(random.nextInt(7));
            }
        }
        lookForMatches(true); //chamar lookForMatches para verificar se há novos combos
    }


    public int lookForMatches(boolean calc) { //usando int em vez de boolean para retornar 0, 1 ou 2
        // procurar combos horizontais
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 6; col++) {
                Tile current = tiles[row][col];
                if (current == null) continue;
                int matchLength = 1;
                while (col + matchLength < 8 && current.equals(tiles[row][col + matchLength])) {
                    matchLength++;
                }
                if (matchLength > 3) {
                    System.out.println("Match of " + matchLength + " at (" + row + ", " + col + ")");
                    if (calc) {
                        calculos(matchLength, current, game);
                        quebrar(row, col, matchLength, 0);
                    }
                    return 2;
                }
                if (matchLength == 3) {
                    System.out.println("Match of " + matchLength + " at (" + row + ", " + col + ")");

                    if (calc) {
                        calculos(matchLength, current, game);
                        quebrar(row, col, matchLength, 0);
                    }
                    return 1;
                }
            }
        }

        // Procurar combos verticais
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 6; row++) {
                Tile current = tiles[row][col];
                if (current == null) continue;
                int matchLength = 1;
                while (row + matchLength < 8 && current.equals(tiles[row + matchLength][col])) {
                    matchLength++;
                }
                if (matchLength > 3) {
                    System.out.println("Match of " + matchLength + " at (" + row + ", " + col + ")");

                    if (calc) { //se for pra quebrar e lidar com efeitos
                        calculos(matchLength, current, game);
                        quebrar(row, col, matchLength, 1);
                    }
                    return 2;
                }
                if (matchLength == 3) {
                    System.out.println("Match of " + matchLength + " at (" + row + ", " + col + ")");

                    if (calc) {
                        calculos(matchLength, current, game);
                        quebrar(row, col, matchLength, 1);
                    }
                    return 1;
                }
            }
        }

        return 0;
    }




    private void shuffleBoard() {
        // dar shuffle até que não haja combos
        while (lookForMatches(false) != 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int x = random.nextInt(8);
                    int y = random.nextInt(8);
                    swap(i, j, x, y);
                }
            }
        }
    }

    public void swap(int x1, int y1, int x2, int y2) { // reponsavel por trocar as peças de lugar
        Tile temp = tiles[x1][y1];
        tiles[x1][y1] = tiles[x2][y2];
        tiles[x2][y2] = temp;
    }

    @Override
    public String toString() { //Override no metodo toString para retornar o tabuleiro usando o stringBuilder
        StringBuilder sb = new StringBuilder();

        // Coluna com emojis
        String[] numberEmojis = {"0️⃣", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣"};
        sb.append("  ");
        for (int col = 0; col < 8; col++) {
            sb.append(numberEmojis[col]).append(" ");
        }
        sb.append("\n");

        // Rows with labels and emojis
        String[] letterEmojis = {"🅰", "🅱", "🅲️", "🅳️", "🅴️", "🅵️", "🅶️", "🅷️"};
        for (int row = 0; row < 8; row++) {
            sb.append(letterEmojis[row]).append(" ");
            for (int col = 0; col < 8; col++) {
                sb.append(tiles[row][col]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
