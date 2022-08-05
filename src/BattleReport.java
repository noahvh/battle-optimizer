import java.util.ArrayList;

public record BattleReport(double additionalTime, int paragonLoss, int orcLoss) {
    public double cost() {
        return additionalTime + 100 * paragonLoss;
    }

    public String toString() {
        return "Additional time: " + String.format("%.4f", additionalTime) + ", Paragon Loss: " + paragonLoss + ", Orc Loss: " + orcLoss + ", Cost: " + cost();
    }

    public boolean betterThan(BattleReport that) {
        if (this.orcLoss() > that.orcLoss()) return true;
        else if (this.orcLoss() < that.orcLoss()) return false;
        else return this.cost() < that.cost();
    }

    public static BattleReport getBattleReport(Team paragons, Team orcs) {
        int phase = 0;

        double additionalTime =
                Math.min(Math.pow(2. * (paragons.getTotalTiers() + orcs.getTotalTiers()), 1.4), 8 * 60 * 60)
                        - Math.min(Math.pow(2. * orcs.getTotalTiers(), 1.4), 8 * 60 * 60);

        int paragonInitialValue = paragons.getTotalValue();
        int orcInitialValue = orcs.getTotalValue();

        while (paragons.getTotalHp() > 0 && orcs.getTotalHp() > 0) {
            ArrayList<Attack> paragonAttacks = new ArrayList<>();
            for (int i = 0; i < paragons.getSize(); i++) {
                Unit unit = paragons.getUnit(i);
                if (unit.isActive(phase))
                    paragonAttacks.add(unit.getAttack());
            }

            ArrayList<Attack> orcAttacks = new ArrayList<>();
            for (int i = 0; i < orcs.getSize(); i++) {
                Unit unit = orcs.getUnit(i);
                if (unit.isActive(phase))
                    orcAttacks.add(unit.getAttack());
            }

            for (Attack attack : paragonAttacks)
                orcs.takeDamage(attack);

            for (Attack attack : orcAttacks)
                paragons.takeDamage(attack);

            phase++;
        }

        int paragonLoss = paragonInitialValue - paragons.getTotalValue();
        int orcLoss = orcInitialValue - orcs.getTotalValue();

        paragons.recover();
        orcs.recover();

        return new BattleReport(additionalTime, paragonLoss, orcLoss);
    }
}
