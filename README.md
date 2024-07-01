# Puzzle Quest

# Trabalho para a minha faculdade

O Puzzle Quest é um jogo baseado em Java em que os jogadores se revezam para combinar peças em um tabuleiro. O jogo apresenta uma variedade de tipos de peças, cada uma com seu próprio efeito exclusivo no estado do jogo.

## Recursos

- Jogo para dois jogadores com mecânica baseada em turnos.
- Diferentes tipos de peças (caveira, vermelha, azul, verde, amarela, dourada, experiência) com efeitos exclusivos.
- Capacidade de salvar e carregar o estado do jogo.
- Capacidade de excluir um estado de jogo salvo.
- O estado do jogo é salvo após cada turno.

## Classes

- `Game`: A classe principal que controla o fluxo do jogo.
- `Player`: Representa um jogador no jogo.
- `Tile`: Representa uma peça no tabuleiro do jogo.
- `Board`: Representa o tabuleiro do jogo.
- `PuzzleQuestMain`: O ponto de entrada do aplicativo.

## Como executar

Para executar o jogo, execute a classe `PuzzleQuestMain`.

## Como jogar

1. Inicie um novo jogo ou carregue um jogo salvo anteriormente.
2. Os jogadores se revezam para trocar peças adjacentes no tabuleiro e formar uma combinação de pelo menos três peças semelhantes.
3. Cada tipo de peça tem um efeito exclusivo no estado do jogo.
4. O jogo termina quando a saúde de um jogador chega a zero.
