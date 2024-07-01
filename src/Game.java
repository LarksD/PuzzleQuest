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
            scanner.nextLine(); // Consumir o next line

            switch (choice) {
                case 1:
                    newGame();
                    break;
                case 2:
                    String loadFilename = selectGame();
                    if (loadFilename != null) {
                        loadGame(loadFilename);
                    }
                    break;
                case 3:
                    String deleteFilename = selectGame();
                    if (deleteFilename != null) {
                        deleteSavedGame(deleteFilename);
                    }
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
        firstTurn = true;
        playGame();
    }



    private void loadGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
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

    private void deleteSavedGame(String filename) {
        File saveFile = new File(filename);
        if (saveFile.exists() && saveFile.delete()) {
            System.out.println("Jogo salvo excluido com sucesso.");
        } else {
            System.out.println("Falha ao excluir jogo salvo.");
        }
    }


    private boolean saveGame() {
        String filename = player1.getName() + " vs " + player2.getName() + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
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
            if (takeTurn(player1)) break;
            System.out.println(board);  // print o tabuleiro depois da jogada do jogador 1
            if (takeTurn(player2)) break;
            System.out.println(board);  // print o tabuleiro depois da jogada do jogador 2
        }
    }

    private String selectGame() {
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".dat"));

        if (listOfFiles.length == 0) {
            System.out.println("Nenhum jogo encontrado.");
            return null;
        }

        for (int i = 0; i < listOfFiles.length; i++) {
            System.out.println((i + 1) + ". " + listOfFiles[i].getName());
        }

        System.out.println("Selecione o jogo informando o número correspondente ou 0 para cancelar:");
        int gameNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (gameNumber == 0) {
            return null;
        }

        if (gameNumber < 1 || gameNumber > listOfFiles.length) {
            System.out.println("Seleção inválida. Tente novamente.");
            return selectGame();
        }

        return listOfFiles[gameNumber - 1].getName();
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
                System.out.println("Movimento invalido. Tente Novamente.");
                System.out.println(board);
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

        if (!board.hasValidMove()) {
            board.shuffleBoard();
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

    private boolean isValidMove(int x, int y, int direction) {
        // Verificar se a posição é válida
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

        // Simular a troca de peças
        board.swap(x, y, targetX, targetY);

        // Procurar por combinações
        boolean hasMatch = board.lookForMatches(false) != 0;

        // Reverter a troca
        board.swap(x, y, targetX, targetY);

        // Retornar se a troca é válida
        return hasMatch;
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
        if (opponent.getHealth() <= 0) {
            System.out.println("Parabéns " + player.getName() + "! Você venceu!");
            return true;
        }

        return false;
    }
}
