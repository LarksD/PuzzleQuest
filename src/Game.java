import java.util.Scanner;

public class Game {
    private Player player1;
    private Player player2;
    private Board board;
    private boolean firstTurn = true;

    Scanner scanner = new Scanner(System.in);

    public Game() {
        System.out.println("Digite o nome do jogador 1: ");
        String player1name = scanner.nextLine();
        player1 = new Player(player1name);
        System.out.println("Digite o nome do jogador 2: ");
        String player2name = scanner.nextLine();
        player2 = new Player(player2name);
        board = new Board();
    }

    public void showMenu() {
        while (true) {
            System.out.println("1. Jogo Novo");
            System.out.println("2. Carregar Jogo");
            System.out.println("3. Deletar Jogo Salvo");
            System.out.println("4. Sair");
            int choice = scanner.nextInt();
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
                    System.exit(0);
            }
        }
    }

    private void newGame() {
        player1.reset();
        player2.reset();
        board.initialize();
        playGame();
    }

    private void loadGame() {
        // carregar jogo salvo
    }

    private void deleteSavedGame() {
        // apagar jogo salvo
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

            /*System.out.println("Row input: " + rowInput + ", Column input: " + columnInput);
            System.out.println("Parsed coordinates: (" + x + ", " + y + ")");
            System.out.println("Direction: " + direction);
            */


            if (x != -1 && y != -1 && isValidMove(x, y, direction)) {
                break;
            } else {
                System.out.println("Move invalido. Tente Novamente.");
            }
        }

        makeMove(x, y, direction);

        // Implementar a logica de pontuação e game over

        if (board.lookForMatches()) {
            System.out.println("Trio Encontrado");
            // Pontuação e quebra das pedrinhas
        }

        return checkGameOver(player);
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

    private void makeMove(int x, int y, int direction) {
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
        if (player.getHealth() <= 0) {
            System.out.println(player.getName() + " foi derotado!");
            return false;
        }
        return true;
    }
}