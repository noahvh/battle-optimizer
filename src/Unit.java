public class Unit {
    private int hp;
    public final int maxCount;
    public final int maxHp;
    public final int attack;
    public final double crit;
    public final boolean flanking;
    public final boolean phaseOne;
    public final boolean phaseTwo;
    public final boolean phaseThree;
    public final boolean trample;

    public final int value;
    public final int tier;
    public final int order;

    public static final int MILITIA = 0;
    public static final int SOLDIER = 1;
    public static final int KNIGHT = 2;
    public static final int CUIRASSIER = 3;
    public static final int MAZOGA = 4;
    public static final int CAVALRY = 5;
    public static final int ARCHER = 6;
    public static final int LONGBOW = 7;
    public static final int CROSSBOW = 8;
    public static final int CANNON = 9;
    public static final int BULA = 10;
    public static final int AGUK = 11;
    public static final int DURGASH = 12;

    private Unit(int count, int maxHp, int attack, double crit, int value, int tier, int order,
                 boolean flanking, boolean trample, boolean phaseOne, boolean phaseTwo, boolean phaseThree) {

        this.maxHp = maxHp;
        this.attack = attack;
        this.crit = crit;
        this.value = value;
        this.tier = tier;
        this.order = order;
        this.flanking = flanking;
        this.phaseOne = phaseOne;
        this.phaseTwo = phaseTwo;
        this.phaseThree = phaseThree;
        this.trample = trample;

        if (order == 4 || order > 9) this.maxCount = Math.min(1, count);
        else this.maxCount = count;

        recover();
    }

    public void damage(int damage) {
        if (damage > hp) {
            System.out.println("Halt!");
        }
        hp -= damage;
    }

    public Attack getAttack() {
        return new Attack(attack, crit, getCount(), flanking, trample);
    }

    public void recover() {
        hp = maxCount * maxHp;
    }

    public int getHp() {
        return hp;
    }

    public int getFlankingPriority() {
        return -(13 * (120000 - maxHp) + order);
    }

    public int getCount() {
        return Math.ceilDiv(hp, maxHp);
    }

    public int getRemainder() {
        if (hp == 0) return 0;
        else if (hp % maxHp == 0) return maxHp;
        else return hp % maxHp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean isActive(int phase) {
        return switch (phase % 3) {
            case 0 -> phaseOne;
            case 1 -> phaseTwo;
            case 2 -> phaseThree;
            default -> false;
        };
    }

    public static Unit createUnit(int type, int count) {
        return switch (type) {
            case MILITIA -> new Unit(count, 15, 5, 0, 1, 1, 0, false, false, false, true, false);
            case SOLDIER -> new Unit(count, 40, 15, 0, 8, 1, 1, false, false, false, true, false);
            case KNIGHT -> new Unit(count, 90, 20, 0, 64, 3, 2, false, false, false, true, false);
            case CUIRASSIER -> new Unit(count, 120, 10, 0, 128, 4, 3, false, false, false, true, false);
            case MAZOGA -> new Unit(count, 120000, 100, 0, 999999, 200, 4, false, true, false, false, true);
            case CAVALRY -> new Unit(count, 5, 5, 0, 16, 2, 5, true, false, true, false, false);
            case ARCHER -> new Unit(count, 10, 20, 0, 4, 1, 6, false, false, false, true, false);
            case LONGBOW -> new Unit(count, 10, 15, 0, 32, 2, 7, false, false, true, false, true);
            case CROSSBOW -> new Unit(count, 15, 90, 0, 64, 3, 8, false, false, false, true, false);
            case CANNON -> new Unit(count, 60, 80, 0, 128, 4, 9, true, true, false, false, true);
            case BULA -> new Unit(count, 5000, 150, 0, 999999, 100, 10, false, true, false, false, true);
            case AGUK -> new Unit(count, 11000, 300, 0, 999999, 150, 11, false, true, false, false, true);
            case DURGASH -> new Unit(count, 40000, 500, 0, 999999, 300, 12, false, true, true, false, false);
            default -> null;
        };
    }
}
