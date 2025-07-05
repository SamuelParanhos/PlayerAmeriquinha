package br.edu.ifnmg.spp1.playerameriquinha;

import io.github.guisso.meleeinterface.Decision;
import io.github.guisso.meleeinterface.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerAmeriquinha implements IPlayer {

    private final List<Decision> opponentMoves = new ArrayList<>();

    @Override
    public String getDeveloperName() {
        return "Samuel Paranhos";
    }

    @Override
    public String getEngineName() {
        return "AMERICA MG MAIOR DE MINAS!!!!"; 
    }

        /*
         Estratégia base:
         - Começa cooperando.
         - Perdoa traições isoladas se vierem após cooperação.
         - Puna oponente se houver traições consecutivas.
         - Torna-se mais agressivo a final da partida.
         */
    
    @Override
    public Decision makeMyMove(Decision opponentLastMove) {

        // Reseta o histórico ao início de um novo confronto
        if (opponentLastMove == Decision.NONE) {
            opponentMoves.clear();
            return Decision.COOPERATE;
        }

        // Salva o último movimento do oponente no histórico
        opponentMoves.add(opponentLastMove);

        int currentRound = opponentMoves.size();

        // Sempre começa cooperando
        if (currentRound == 1) {
            return Decision.COOPERATE;
        }

        // A partir da rodada 180, passa a adotar uma postura mais agressiva
    
        if (currentRound >= 180) {
            return Decision.DEFECT;
        }

        // Detecta duas traições consecutivas
        if (currentRound >= 2) {
            Decision secondLast = opponentMoves.get(currentRound - 2);
            Decision last = opponentMoves.get(currentRound - 1);

            if (secondLast == Decision.DEFECT && last == Decision.DEFECT) {
                return Decision.DEFECT;
            }

            // Se antes houve cooperação, perdoa uma traição isolada
            if (secondLast == Decision.COOPERATE && last == Decision.DEFECT && currentRound >= 3) {
                Decision thirdLast = opponentMoves.get(currentRound - 3);
                if (thirdLast == Decision.COOPERATE) {
                    return Decision.COOPERATE;
                }
            }
        }

        // Analisa os últimos movimentos, com uma janela de no máximo 50 jogadas
        int windowSize = Math.min(50, currentRound);
        int defectCount = 0;
        for (int i = currentRound - windowSize; i < currentRound; i++) {
            if (opponentMoves.get(i) == Decision.DEFECT) {
                defectCount++;
            }
        }

        double defectRate = (double) defectCount / windowSize;

        // Se a taxa de traição recente for alta, responde com traição
        if (defectRate > 0.3) {
            return Decision.DEFECT;
        }

        // Movimento padrão: copia a última jogada do oponente
        return opponentLastMove;    
    }
}