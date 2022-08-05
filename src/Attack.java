public record Attack(int power, double crit, int amount, boolean flanking, boolean trample) {
    public Attack(int power, double crit, int amount, boolean flanking, boolean trample) {
        this.power = trample ? 1 : power;
        this.crit = crit;
        this.amount = trample ? (int) (power * amount * (1 + crit)) : amount;
        this.flanking = flanking;
        this.trample = trample;
    }
}
