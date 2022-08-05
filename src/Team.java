import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Team {
    private final ArrayList<Unit> originalUnits = new ArrayList<>();
    private final ArrayList<Unit> units = new ArrayList<>();
    private final ArrayList<Unit> flankingTargets = new ArrayList<>();
    private int totalHp = 0;
    private boolean weak = true;
    public String name = "";

    public Team(int[] playerTeam) {
        if (playerTeam[0] > 0) originalUnits.add(Unit.createUnit(Unit.MILITIA, playerTeam[0]));
        if (playerTeam[1] > 0) originalUnits.add(Unit.createUnit(Unit.SOLDIER, playerTeam[1]));
        if (playerTeam[2] > 0) originalUnits.add(Unit.createUnit(Unit.KNIGHT, playerTeam[2]));
        if (playerTeam[3] > 0) originalUnits.add(Unit.createUnit(Unit.CUIRASSIER, playerTeam[3]));
        if (playerTeam[4] > 0) originalUnits.add(Unit.createUnit(Unit.CAVALRY, playerTeam[4]));
        if (playerTeam[5] > 0) originalUnits.add(Unit.createUnit(Unit.ARCHER, playerTeam[5]));
        if (playerTeam[6] > 0) originalUnits.add(Unit.createUnit(Unit.LONGBOW, playerTeam[6]));
        if (playerTeam[7] > 0) originalUnits.add(Unit.createUnit(Unit.CROSSBOW, playerTeam[7]));
        if (playerTeam[8] > 0) originalUnits.add(Unit.createUnit(Unit.CANNON, playerTeam[8]));

        recover();
    }

    public Team(HashMap<Integer, Integer> allocation) {
        for (var entry : allocation.entrySet()) {
            if (entry.getValue() == 0) continue;
            Unit newUnit = Unit.createUnit(entry.getKey(), entry.getValue());
            originalUnits.add(newUnit);
            units.add(newUnit);
            flankingTargets.add(newUnit);
            totalHp += newUnit.getHp();
        }

        flankingTargets.sort(Comparator.comparing(Unit::getFlankingPriority));
    }

    public void recover() {
        units.clear();
        flankingTargets.clear();
        totalHp = 0;

        for (Unit unit : originalUnits) {
            unit.recover();
            units.add(unit);
            flankingTargets.add(unit);
            totalHp += unit.getHp();
        }

        flankingTargets.sort(Comparator.comparing(Unit::getFlankingPriority));
    }

    public void setWeak(boolean weak) {
        this.weak = weak;
    }

    public void takeDamage(Attack attack) {
        if (totalHp == 0) return;

        int power = attack.power();
        int amount = attack.amount();

        // if (weak && attack.flanking()) amount *= 2;
        // if (weak && !attack.flanking()) power *= 2;

        boolean flanking = attack.flanking();
        Unit target = getTarget(flanking);

        while (target != null && amount > 0) {
            int frontHP = target.getRemainder();

            // First attack the combatant in the front if it's damaged
            if (frontHP < target.maxHp) {
                // Attack either until frontline combatant is dead, or amount runs out
                if (power * amount <= frontHP) { // not enough attack to get through first guy
                    damage(target, power * amount);
                    break;
                } else { // get through first guy
                    amount -= Math.ceilDiv(frontHP, power);
                    damage(target, frontHP);
                }

                if (target.isDead()) {
                    target = removeUnitAndGetNext(target, flanking);
                    continue;
                }
            }

            // Then attack everybody else.
            // Either attack until back combatant is dead, or amount runs out
            int attacksPerKill = Math.ceilDiv(target.maxHp, power);
            int targetPop = target.getCount();

            if (amount / attacksPerKill < targetPop) { // not enough attack to get through target
                damage(target, (amount / attacksPerKill * target.maxHp) + (amount % attacksPerKill) * power);
                if (target.isDead()) removeUnit(target);
                break;
            } else { // wiped unit
                amount -= attacksPerKill * targetPop;
                damage(target, target.getHp());

                target = removeUnitAndGetNext(target, flanking);
            }
        }
    }

    private void damage(Unit target, int dmg) {
        target.damage(dmg);
        totalHp -= dmg;
    }

    private void removeUnit(Unit unit) {
        units.remove(unit);
        flankingTargets.remove(unit);
    }

    private Unit removeUnitAndGetNext(Unit unit, boolean flanking) {
        units.remove(unit);
        flankingTargets.remove(unit);

        if (units.size() == 0) return null;
        else if (flanking) return flankingTargets.get(0);
        else return units.get(0);
    }

    public Unit getTarget(boolean flanking) {
        if (flanking && flankingTargets.size() > 0)
            return flankingTargets.get(0);
        else if (units.size() > 0)
            return units.get(0);
        else
            return null;
    }

    public Unit getUnit(int i) {
        return units.get(i);
    }

    public int getSize() {
        return units.size();
    }

    public int getTotalHp() {
        return totalHp;
    }

    public int getTotalValue() {
        int totalCost = 0;
        for (Unit unit : units) totalCost += unit.value * unit.getCount();

        return totalCost;
    }

    public int getTotalTiers() {
        int totalTiers = 0;
        for (Unit unit : units) totalTiers += unit.tier * unit.getCount();

        return totalTiers;
    }

    public int getPopulation() {
        int pop = 0;
        for (Unit unit : units) pop += unit.getCount();
        return pop;
    }
}
