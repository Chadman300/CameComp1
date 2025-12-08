public class GameData {
    // Game constants
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    
    // Score and money
    private int score;
    private int totalMoney;
    private int runMoney;
    private int survivalTime;
    
    // Level progression
    private int currentLevel;
    private int maxUnlockedLevel;
    private boolean[] defeatedBosses;
    
    // Upgrades (persistent)
    private int speedUpgradeLevel;
    private int bulletSlowUpgradeLevel;
    private int luckyDodgeUpgradeLevel;
    
    // Active upgrades (allocated from purchased upgrades)
    private int activeSpeedLevel;
    private int activeBulletSlowLevel;
    private int activeLuckyDodgeLevel;
    
    public GameData() {
        score = 0;
        totalMoney = 0;
        runMoney = 0;
        survivalTime = 0;
        defeatedBosses = new boolean[100];
        currentLevel = 1;
        maxUnlockedLevel = 1;
        speedUpgradeLevel = 0;
        bulletSlowUpgradeLevel = 0;
        luckyDodgeUpgradeLevel = 0;
        activeSpeedLevel = 0;
        activeBulletSlowLevel = 0;
        activeLuckyDodgeLevel = 0;
    }
    
    // Getters and setters
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public void addScore(int points) { this.score += points; }
    
    public int getTotalMoney() { return totalMoney; }
    public void setTotalMoney(int totalMoney) { this.totalMoney = totalMoney; }
    public void addTotalMoney(int amount) { this.totalMoney += amount; }
    
    public int getRunMoney() { return runMoney; }
    public void setRunMoney(int runMoney) { this.runMoney = runMoney; }
    public void addRunMoney(int amount) { this.runMoney += amount; }
    
    public int getSurvivalTime() { return survivalTime; }
    public void setSurvivalTime(int survivalTime) { this.survivalTime = survivalTime; }
    public void incrementSurvivalTime() { this.survivalTime++; }
    
    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
    
    public int getMaxUnlockedLevel() { return maxUnlockedLevel; }
    public void setMaxUnlockedLevel(int maxUnlockedLevel) { this.maxUnlockedLevel = maxUnlockedLevel; }
    
    public boolean[] getDefeatedBosses() { return defeatedBosses; }
    public void setBossDefeated(int level, boolean defeated) { 
        if (level >= 0 && level < defeatedBosses.length) {
            defeatedBosses[level] = defeated;
        }
    }
    
    public int getSpeedUpgradeLevel() { return speedUpgradeLevel; }
    public void setSpeedUpgradeLevel(int level) { this.speedUpgradeLevel = level; }
    public void incrementSpeedUpgrade() { this.speedUpgradeLevel++; }
    
    public int getBulletSlowUpgradeLevel() { return bulletSlowUpgradeLevel; }
    public void setBulletSlowUpgradeLevel(int level) { this.bulletSlowUpgradeLevel = level; }
    public void incrementBulletSlowUpgrade() { this.bulletSlowUpgradeLevel++; }
    
    public int getLuckyDodgeUpgradeLevel() { return luckyDodgeUpgradeLevel; }
    public void setLuckyDodgeUpgradeLevel(int level) { this.luckyDodgeUpgradeLevel = level; }
    public void incrementLuckyDodgeUpgrade() { this.luckyDodgeUpgradeLevel++; }
    
    public int getActiveSpeedLevel() { return activeSpeedLevel; }
    public void setActiveSpeedLevel(int level) { this.activeSpeedLevel = Math.max(0, Math.min(speedUpgradeLevel, level)); }
    
    public int getActiveBulletSlowLevel() { return activeBulletSlowLevel; }
    public void setActiveBulletSlowLevel(int level) { this.activeBulletSlowLevel = Math.max(0, Math.min(bulletSlowUpgradeLevel, level)); }
    
    public int getActiveLuckyDodgeLevel() { return activeLuckyDodgeLevel; }
    public void setActiveLuckyDodgeLevel(int level) { this.activeLuckyDodgeLevel = Math.max(0, Math.min(luckyDodgeUpgradeLevel, level)); }
    
    public void adjustUpgrade(int upgradeIndex, int delta) {
        switch (upgradeIndex) {
            case 0: // Speed
                activeSpeedLevel = Math.max(0, Math.min(speedUpgradeLevel, activeSpeedLevel + delta));
                break;
            case 1: // Bullet Slow
                activeBulletSlowLevel = Math.max(0, Math.min(bulletSlowUpgradeLevel, activeBulletSlowLevel + delta));
                break;
            case 2: // Lucky Dodge
                activeLuckyDodgeLevel = Math.max(0, Math.min(luckyDodgeUpgradeLevel, activeLuckyDodgeLevel + delta));
                break;
        }
    }
}
