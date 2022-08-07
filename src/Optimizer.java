import java.util.Arrays;
import java.util.HashMap;
import java.util.StringJoiner;

public class Optimizer {

    private static final int maxPop = 100;

    public static int[] getOptimalComposition(Team orcs) {
        int[] playerComp = new int[9];

        int pop = Math.min(maxPop, orcs.getPopulation() & ~1);
        if (pop == 0) pop = 100;

        BattleReport bestInitialReport = BattleReport.getBattleReport(new Team(playerComp), orcs);
        int bestSquads = -1;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                playerComp[i] += pop/2;
                playerComp[j] += pop/2;

                BattleReport newReport = BattleReport.getBattleReport(new Team(playerComp), orcs);
                if (newReport.betterThan(bestInitialReport)) {
                    bestInitialReport = newReport;
                    bestSquads = i * 9 + j;
                }

                playerComp[i] = 0;
                playerComp[j] = 0;
            }
        }

        if (bestSquads >= 0) {
            playerComp[bestSquads / 9] += pop/2;
            playerComp[bestSquads % 9] += pop/2;
        }

        BattleReport bestTweakReport = BattleReport.getBattleReport(new Team(playerComp), orcs);
        BattleReport bestSwapReport = BattleReport.getBattleReport(new Team(playerComp), orcs);
        int bestTweak;
        int bestSwap;
        boolean stepped = true;

        while (stepped) {
            stepped = false;

            bestTweak = -1;
            for (int i = 0; i < 36; i++) {
                if (i < 9 && playerComp[i] >= 1) {
                    playerComp[i]--;
                } else if (9 <= i && i < 18 && pop < maxPop) {
                    playerComp[i-9]++;
                } else if (18 <= i && i < 27 && playerComp[i-18] >= 5) {
                    playerComp[i-18] -= 5;
                } else if (27 <= i && pop < maxPop - 4) {
                    playerComp[i-27] += 5;
                } else continue;

                BattleReport newReport = BattleReport.getBattleReport(new Team(playerComp), orcs);

                // System.out.println("TEST " + Arrays.toString(playerComp) + "(" + pop + ") " + newReport);

                if (newReport.betterThan(bestTweakReport)) {
                    bestTweakReport = newReport;
                    bestTweak = i;
                    stepped = true;
                }

                if (i < 9) playerComp[i]++;
                else if (i < 18) playerComp[i-9]--;
                else if (i < 27) playerComp[i-18] += 5;
                else playerComp[i-27] -= 5;
            }

            bestSwap = -1;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (i == j || playerComp[i] <= 0 || playerComp[j] >= maxPop) continue;

                    playerComp[i]--;
                    playerComp[j]++;

                    BattleReport newReport = BattleReport.getBattleReport(new Team(playerComp), orcs);
                    // System.out.println("TEST " + Arrays.toString(playerComp) + "(" + pop + ") " + newReport);

                    if (newReport.betterThan(bestSwapReport)) {
                        bestSwapReport = newReport;
                        bestSwap = i * 9 + j;
                        stepped = true;
                    }

                    playerComp[i]++;
                    playerComp[j]--;
                }
            }

            if (stepped && bestTweakReport.betterThan(bestSwapReport)) {

                if (0 <= bestTweak && bestTweak < 9) {
                    playerComp[bestTweak]--;
                    pop--;
                } else if (9 <= bestTweak && bestTweak < 18) {
                    playerComp[bestTweak-9]++;
                    pop++;
                } else if (18 <= bestTweak && bestTweak < 27) {
                    playerComp[bestTweak-18] -= 5;
                    pop -= 5;
                } else if (27 <= bestTweak){
                    playerComp[bestTweak-27] += 5;
                    pop += 5;
                }

                bestSwapReport = bestTweakReport;

            } else if (stepped) {
                playerComp[bestSwap / 9]--;
                playerComp[bestSwap % 9]++;

                bestTweakReport = bestSwapReport;
            }

            System.out.println("BEST " + Arrays.toString(playerComp) + "(" + pop + ") " + bestTweakReport);
        }

        return playerComp;
    }

    public static String getPlayerCompReport(int[] comp) {
        StringJoiner report = new StringJoiner(", ");
        if (comp[0] > 0) report.add("Militia: " + comp[0]);
        if (comp[1] > 0) report.add("Soldier: " + comp[1]);
        if (comp[2] > 0) report.add("Knight: " + comp[2]);
        if (comp[3] > 0) report.add("Cuirassier: " + comp[3]);
        if (comp[4] > 0) report.add("Cavalry: " + comp[4]);
        if (comp[5] > 0) report.add("Archer: " + comp[5]);
        if (comp[6] > 0) report.add("Longbow: " + comp[6]);
        if (comp[7] > 0) report.add("Crossbow: " + comp[7]);
        if (comp[8] > 0) report.add("Cannon: " + comp[8]);

        return report.toString();
    }

    public static void main(String[] args) {
        HashMap<Integer, Integer> orcReq = new HashMap<>();

             orcReq.put(Unit.KNIGHT, 0);
         orcReq.put(Unit.CUIRASSIER, 0);
            orcReq.put(Unit.CAVALRY, 193);
            orcReq.put(Unit.LONGBOW, 0);
           orcReq.put(Unit.CROSSBOW, 0);
             orcReq.put(Unit.CANNON, 0);
             orcReq.put(Unit.MAZOGA, 0);
            orcReq.put(Unit.DURGASH, 0);

        Team orcs = new Team(orcReq);

        long start = System.currentTimeMillis();

        int[] comp = getOptimalComposition(orcs);

        System.out.println("Total Starting Orc Value: " + orcs.getTotalValue());

        System.out.println(getPlayerCompReport(comp));

        BattleReport.getBattleReport(new Team(comp), orcs);

        System.out.println(System.currentTimeMillis() - start);
    }

}
