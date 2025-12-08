public class ShopManager {
    private GameData gameData;
    private int selectedShopItem;
    
    public ShopManager(GameData gameData) {
        this.gameData = gameData;
        this.selectedShopItem = 0;
    }
    
    public int getSelectedShopItem() {
        return selectedShopItem;
    }
    
    public void setSelectedShopItem(int item) {
        this.selectedShopItem = Math.max(0, Math.min(5, item));
    }
    
    public void selectPrevious() {
        selectedShopItem = Math.max(0, selectedShopItem - 1);
    }
    
    public void selectNext() {
        selectedShopItem = Math.min(5, selectedShopItem + 1);
    }
    
    public int getItemCost(int itemIndex) {
        switch (itemIndex) {
            case 0: return 0; // Free (just continue)
            case 1: return 100 + (gameData.getSpeedUpgradeLevel() * 50);
            case 2: return 150 + (gameData.getBulletSlowUpgradeLevel() * 75);
            case 3: return 200 + (gameData.getLuckyDodgeUpgradeLevel() * 100);
            case 4: return 250; // Fire Rate upgrade
            case 5: return 300; // Score multiplier
            default: return 0;
        }
    }
    
    public boolean purchaseItem(int itemIndex) {
        int cost = getItemCost(itemIndex);
        
        if (itemIndex == 0) {
            return true; // Continue button - free
        }
        
        if (gameData.getTotalMoney() >= cost) {
            gameData.addTotalMoney(-cost);
            
            switch (itemIndex) {
                case 1:
                    gameData.incrementSpeedUpgrade();
                    gameData.setActiveSpeedLevel(gameData.getSpeedUpgradeLevel()); // Auto-select
                    break;
                case 2:
                    gameData.incrementBulletSlowUpgrade();
                    gameData.setActiveBulletSlowLevel(gameData.getBulletSlowUpgradeLevel()); // Auto-select
                    break;
                case 3:
                    gameData.incrementLuckyDodgeUpgrade();
                    gameData.setActiveLuckyDodgeLevel(gameData.getLuckyDodgeUpgradeLevel()); // Auto-select
                    break;
                case 4:
                    // Fire Rate upgrade (not implemented yet)
                    break;
                case 5:
                    // Score multiplier (not implemented yet)
                    break;
            }
            return true;
        }
        return false;
    }
    
    public String[] getShopItems() {
        return new String[] {
            "Continue - Return to level select",
            "Speed Boost - Increases movement speed by 15%",
            "Bullet Slow - Slows enemy bullets by 5%",
            "Lucky Dodge - Small chance to phase through bullets",
            "Fire Rate Up - Faster shooting (Coming Soon)",
            "Score Multiplier - Increases score gain (Coming Soon)"
        };
    }
}
