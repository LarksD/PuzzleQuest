import java.util.Scanner;
import java.io.*;

public class Game implements Serializable { //Serializable para que o jogo possa ser salvo
    private Player player1;
    private Player player2;
    private Board board;
    private boolean firstTurn = true;
    private transient Scanner scanner; //transient para que o scanner não seja serializado
    private boolean isPlayer1Turn;

    public Game() {
        scanner = new Scanner(System.in); //inicializar o scanner pois ele não é serializado
        board = new Board(this);
        isPlayer1Turn = false;
    }


    public void showMenu() {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("        Puzzle Quest");
            System.out.println("==============================");
            System.out.println(" 1. 🆕 Jogo Novo");
            System.out.println(" 2. 📂 Carregar Jogo");
            System.out.println(" 3. 🗑️ Deletar Jogo Salvo");
            System.out.println(" 4. 🚪 Sair");
            System.out.println("==============================");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    newGame();
                    break;
                case 2:
                    loadGame();
                    break;
                case 3:
                    deleteSavedGame();
                    break;
                case 4:
                    System.out.println("Saindo do jogo. Até a próxima!");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }


    private void newGame() {
        System.out.println("Digite o nome do jogador 1: ");
        String player1name = scanner.nextLine();
        player1 = new Player(player1name);

        System.out.println("Digite o nome do jogador 2: ");
        String player2name = scanner.nextLine();
        player2 = new Player(player2name);

        player1.reset();
        player2.reset();
        board.initialize();
        playGame();
    }



    private void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("jogosalvo.dat"))) {
            Game loadedGame = (Game) ois.readObject();
            this.player1 = loadedGame.player1;
            this.player2 = loadedGame.player2;
            this.board = loadedGame.board;
            System.out.println("Jogo carregado com sucesso.");
            playGame();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Falha ao carregar jogo.");
        }
    }

    private void deleteSavedGame() {
        File saveFile = new File("jogosalvo.dat");
        if (saveFile.exists() && saveFile.delete()) {
            System.out.println("Jogo salvo excluido com sucesso.");
        } else {
            System.out.println("Nenhum jogo salvo encontrado.");
        }
    }

    private boolean saveGame() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("jogosalvo.dat"))) {
            oos.writeObject(this);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Player getCurrentPlayer() {
        return isPlayer1Turn ? player1 : player2;
    }

    public Player getOpponent(Player currentPlayer) {
        if (currentPlayer == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    private void playGame() {
        while (true) {
            if (firstTurn) {
                System.out.println(board);
                firstTurn = false;
            }
            if (!takeTurn(player1)) break;
            System.out.println(board);  // print o tabuleiro depois da jogada do jogador 1
            if (!takeTurn(player2)) break;
            System.out.println(board);  // print o tabuleiro depois da jogada do jogador 2
        }
    }

    private boolean takeTurn(Player player) {
        System.out.println("Vez do " + player.getName());
        System.out.println("Vida: " + player.getHealth());
        System.out.println("Ouro: " + player.getGold());
        System.out.println("Experiencia: " + player.getExperience());
        System.out.println("Dano Duplo: " + (player.getDoubleDamage() ? "Sim" : "Não"));
        System.out.println("Max Vida: " + player.getMaxHealth());

        int x = -1, y = -1;
        int direction;

        while (true) {
            System.out.println("Digite a linha (A-H): ");
            String rowInput = scanner.next();
            System.out.println("Digite a coluna (0-7): ");
            String columnInput = scanner.next();
            scanner.nextLine(); // Consume newline
            System.out.println("Digite a direção:\n1:Pra Cima\n2:Pra Baixo \n3:Esquerda \n4:Direita");
            direction = scanner.nextInt();

            x = getRowIndex(rowInput);
            y = getColumnIndex(columnInput);

            if (x != -1 && y != -1 && isValidMove(x, y, direction)) {
                break;
            } else {
                System.out.println("Move invalido. Tente Novamente.");
            }
        }

        makeMove(x, y, direction);



        int matchResult = board.lookForMatches(true); //salvar o retorno para fazer as próximas verificações
        if (matchResult != 0) {
            System.out.println("combo!");
        }

        if (matchResult == 2) { // acresentar turno
            System.out.println("Jogada Extra!");
            System.out.println(board); //print o tabuleiro com as peças trocadas
            takeTurn(player);
        }

        if (saveGame()) { // salvar o jogo a cada jogada
            System.out.println("Jogo salvo com sucesso.");
        } else {
            System.out.println("Falha ao salvar jogo.");
        }

        return checkGameOver(player); //verificar se o jogo acabou
    }

    private int getRowIndex(String rowInput) {
        switch (Character.toLowerCase(rowInput.charAt(0))) {
            case 'a': return 0;
            case 'b': return 1;
            case 'c': return 2;
            case 'd': return 3;
            case 'e': return 4;
            case 'f': return 5;
            case 'g': return 6;
            case 'h': return 7;
            default: return -1;
        }
    }

    private int getColumnIndex(String columnInput) {
        switch (columnInput) {
            case "0": return 0;
            case "1": return 1;
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            default: return -1;
        }
    }

    private boolean isValidMove(int x, int y, int direction) { //verificar se o movimento é válido dentro do tabuleiro
        if (x < 0 || x >= 8 || y < 0 || y >= 8) {
            return false;
        }

        int targetX = x, targetY = y;
        switch (direction) {
            case 1:
                targetX = x - 1;
                break;
            case 2:
                targetX = x + 1;
                break;
            case 3:
                targetY = y - 1;
                break;
            case 4:
                targetY = y + 1;
                break;
            default:
                return false;
        }

        if (targetX < 0 || targetX >= 8 || targetY < 0 || targetY >= 8) {
            return false;
        }

        return true;
    }

    private void makeMove(int x, int y, int direction) { //fazer o movimento atraves de troca de peças
        switch (direction) {
            case 1:
                board.swap(x, y, x - 1, y);
                break;
            case 2:
                board.swap(x, y, x + 1, y);
                break;
            case 3:
                board.swap(x, y, x, y - 1);
                break;
            case 4:
                board.swap(x, y, x, y + 1);
                break;
        }
    }

    private boolean checkGameOver(Player player) {
        Player opponent = getOpponent(player);
        if (player.getHealth() <= 0 || opponent.getHealth() <= 0) {   //verificar se a vida do jogador ou do oponente é menor ou igual a 0
            System.out.println(player.getName() + " foi derrotado!");
            return false;
        }
        return true;
    }
}
