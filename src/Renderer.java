import java.awt.*;
import java.util.List;

public class Renderer {
    private GameData gameData;
    private ShopManager shopManager;
    
    // Menu buttons
    private UIButton[] menuButtons;
    private UIButton[] shopButtons;
    private UIButton[] statsButtons;
    private UIButton[] settingsButtons;
    
    public Renderer(GameData gameData, ShopManager shopManager) {
        this.gameData = gameData;
        this.shopManager = shopManager;
        
        // Initialize menu buttons (positions will be updated in drawMenu)
        menuButtons = new UIButton[5];
        menuButtons[0] = new UIButton("Select Level", 0, 0, 300, 50, new Color(143, 188, 187), new Color(163, 190, 140));
        menuButtons[1] = new UIButton("Game Info", 0, 0, 300, 50, new Color(136, 192, 208), new Color(163, 190, 140));
        menuButtons[2] = new UIButton("Stats & Loadout", 0, 0, 300, 50, new Color(180, 142, 173), new Color(163, 190, 140));
        menuButtons[3] = new UIButton("Shop", 0, 0, 300, 50, new Color(235, 203, 139), new Color(163, 190, 140));
        menuButtons[4] = new UIButton("Settings", 0, 0, 300, 50, new Color(191, 97, 106), new Color(163, 190, 140));
        
        // Initialize shop buttons (7 items)
        shopButtons = new UIButton[7];
        for (int i = 0; i < 7; i++) {
            shopButtons[i] = new UIButton("", 0, 0, 800, 50, new Color(76, 86, 106), new Color(180, 142, 173));
        }
        
        // Initialize stats buttons (4 upgrades)
        statsButtons = new UIButton[4];
        String[] statNames = {"Speed Boost", "Bullet Slow", "Lucky Dodge", "Attack Window+"};
        Color[] statColors = {new Color(143, 188, 187), new Color(136, 192, 208), new Color(180, 142, 173), new Color(235, 203, 139)};
        for (int i = 0; i < 4; i++) {
            statsButtons[i] = new UIButton(statNames[i], 0, 0, 840, 70, new Color(59, 66, 82), statColors[i]);
        }
        
        // Initialize settings buttons (4 options)
        settingsButtons = new UIButton[4];
        for (int i = 0; i < 4; i++) {
            settingsButtons[i] = new UIButton("", 0, 0, 700, 80, new Color(76, 86, 106), new Color(235, 203, 139));
        }
    }
    
    public void drawMenu(Graphics2D g, int width, int height, double time, int escapeTimer, int selectedMenuItem) {
        // Draw animated gradient background with palette colors
        drawAnimatedGradient(g, width, height, time, new Color[]{new Color(46, 52, 64), new Color(59, 66, 82), new Color(76, 86, 106)});
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 72));
        String title = "ONE HIT MAN";
        FontMetrics fm = g.getFontMetrics();
        
        // Balatro-style title with holographic shine effect
        int titleX = (width - fm.stringWidth(title)) / 2;
        int titleY = 150;
        
        // Shadow layers
        g.setColor(new Color(0, 0, 0, 100));
        g.drawString(title, titleX + 4, titleY + 4);
        
        // Gradient text effect
        GradientPaint titleGrad = new GradientPaint(
            titleX, titleY - 50, new Color(143, 188, 187), // Palette teal
            titleX, titleY + 20, new Color(136, 192, 208) // Palette cyan
        );
        g.setPaint(titleGrad);
        g.drawString(title, titleX, titleY);
        
        // Holographic shine
        int shineOffset = (int)(Math.sin(time * 2) * 30);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g.setColor(Color.WHITE);
        g.drawString(title, titleX + 2 + shineOffset / 10, titleY - 2);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        // Draw buttons
        int buttonY = 280;
        int buttonSpacing = 70;
        for (int i = 0; i < menuButtons.length; i++) {
            menuButtons[i].setPosition((width - 300) / 2, buttonY + i * buttonSpacing);
            menuButtons[i].update(i == selectedMenuItem, time);
            menuButtons[i].draw(g, time);
        }
        
        // Show money
        g.setColor(new Color(163, 190, 140)); // Palette green
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String money = "Money: $" + gameData.getTotalMoney();
        fm = g.getFontMetrics();
        g.drawString(money, (width - fm.stringWidth(money)) / 2, height - 150);
        
        // Quit hint
        if (escapeTimer > 0) {
            g.setColor(new Color(191, 97, 106)); // Palette red
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String quitText = "Press ESC again to Quit";
            fm = g.getFontMetrics();
            g.drawString(quitText, (width - fm.stringWidth(quitText)) / 2, height - 80);
        }
    }
    
    public void drawInfo(Graphics2D g, int width, int height, double time) {
        // Draw animated gradient with palette colors
        drawAnimatedGradient(g, width, height, time, new Color[]{new Color(46, 52, 64), new Color(59, 66, 82), new Color(76, 86, 106)});
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "GAME INFO";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (width - fm.stringWidth(title)) / 2, 60);
        
        // Game Rules section
        g.setColor(new Color(143, 188, 187)); // Palette teal
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("CORE RULES:", 70, 120);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String[] rules = {
            "• You have 1 HP - One hit = Game Over",
            "• Boss has 1 HP - One hit during attack window = Victory",
            "• Move with WASD or Arrow Keys",
            "• Attack window opens periodically - look for the yellow ring!",
            "• Beam attacks spawn at higher levels with WARNING indicators"
        };
        
        int y = 155;
        for (String line : rules) {
            g.drawString(line, 90, y);
            y += 30;
        }
        
        // Boss types section
        y += 20;
        g.setColor(new Color(235, 203, 139)); // Palette yellow
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("BOSS TYPES:", 70, y);
        y += 35;
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String[] bossInfo = {
            "Level 1-3: Triangle, Square, Pentagon - Basic patterns",
            "Level 4-6: Hexagon, Heptagon, Octagon - Mixed attacks",
            "Level 7-9: Nonagon, Decagon, 11-gon - Advanced patterns",
            "Level 10+: 12+ sided polygons - All attack types + Beams",
            "",
            "Each boss gains 1 side per level with increasingly complex patterns!"
        };
        
        for (String line : bossInfo) {
            g.drawString(line, 90, y);
            y += 28;
        }
        
        // Projectile types section
        y += 20;
        g.setColor(new Color(136, 192, 208)); // Palette cyan
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("PROJECTILE TYPES:", 70, y);
        y += 35;
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String[] projectileInfo = {
            "1. NORMAL - Standard red bullets",
            "2. FAST - Orange bullets with higher speed",
            "3. LARGE - Big blue bullets, easier to see",
            "4. HOMING - Purple bullets that track you",
            "5. BOUNCING - Green bullets that bounce off walls",
            "6. SPIRAL - Pink bullets that rotate as they move",
            "7. SPLITTING - Yellow bullets that split into 3",
            "8. ACCELERATING - Cyan bullets that speed up",
            "9. WAVE - Magenta bullets moving in wave patterns",
            "",
            "All special projectiles show 45-frame warning indicators!"
        };
        
        for (String line : projectileInfo) {
            g.drawString(line, 90, y);
            y += 28;
        }
        
        // Controls hint
        g.setColor(new Color(216, 222, 233));
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press ESC to return to menu | Press R to restart during gameplay | Press P to visit shop", 70, height - 50);
    }
    
    public void drawStats(Graphics2D g, int width, int height, double time) {
        // Draw animated Balatro-style gradient
        drawAnimatedGradient(g, width, height, time, new Color[]{new Color(30, 15, 45), new Color(45, 30, 60), new Color(60, 45, 75)});
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "STATS & LOADOUT";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (width - fm.stringWidth(title)) / 2, 80);
        
        // Show total money
        g.setColor(new Color(163, 190, 140)); // Palette green
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String money = "Total Money: $" + gameData.getTotalMoney();
        fm = g.getFontMetrics();
        g.drawString(money, (width - fm.stringWidth(money)) / 2, 140);
        
        // Show max level reached
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String maxLevel = "Highest Level Unlocked: " + gameData.getMaxUnlockedLevel();
        fm = g.getFontMetrics();
        g.drawString(maxLevel, (width - fm.stringWidth(maxLevel)) / 2, 180);
        
        // Upgrade allocation section
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String allocTitle = "UPGRADE ALLOCATION";
        fm = g.getFontMetrics();
        g.drawString(allocTitle, (width - fm.stringWidth(allocTitle)) / 2, 240);
        
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String allocDesc = "Allocate your purchased upgrades to your loadout";
        fm = g.getFontMetrics();
        g.drawString(allocDesc, (width - fm.stringWidth(allocDesc)) / 2, 270);
        
        // Instructions
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String inst1 = "Use UP/DOWN to select | LEFT/RIGHT or A/D to adjust";
        String inst2 = "Press ESC to return to menu";
        fm = g.getFontMetrics();
        g.drawString(inst1, (width - fm.stringWidth(inst1)) / 2, height - 80);
        g.drawString(inst2, (width - fm.stringWidth(inst2)) / 2, height - 50);
        
        // Show active loadout summary
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String summary = "Current Loadout: Speed +" + (gameData.getActiveSpeedLevel() * 15) + "% | Bullet Slow " + 
                        (gameData.getActiveBulletSlowLevel() * 5) + "% | Luck +" + gameData.getActiveLuckyDodgeLevel();
        fm = g.getFontMetrics();
        g.drawString(summary, (width - fm.stringWidth(summary)) / 2, height - 120);
    }
    
    public void drawStatsUpgrades(Graphics2D g, int width, int selectedStatItem) {
        String[] upgradeNames = {"Speed Boost", "Bullet Slow", "Lucky Dodge", "Attack Window+"};
        
        int y = 340;
        for (int i = 0; i < upgradeNames.length; i++) {
            boolean isSelected = i == selectedStatItem;
            int owned = 0;
            int active = 0;
            
            switch (i) {
                case 0: owned = gameData.getSpeedUpgradeLevel(); active = gameData.getActiveSpeedLevel(); break;
                case 1: owned = gameData.getBulletSlowUpgradeLevel(); active = gameData.getActiveBulletSlowLevel(); break;
                case 2: owned = gameData.getLuckyDodgeUpgradeLevel(); active = gameData.getActiveLuckyDodgeLevel(); break;
                case 3: owned = gameData.getAttackWindowUpgradeLevel(); active = gameData.getActiveAttackWindowLevel(); break;
            }
            
            // Position and draw the main upgrade button
            statsButtons[i].setPosition((width - 840) / 2, y - 30);
            
            // Draw minus button
            int minusX = width / 2 + 50;
            g.setColor(active > 0 ? new Color(191, 97, 106) : new Color(80, 80, 80)); // Red when active, gray when disabled
            g.fillRoundRect(minusX, y - 20, 40, 40, 10, 10);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(minusX, y - 20, 40, 40, 10, 10);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("-", minusX + 13, y + 10);
            
            // Draw progress bar background
            int barX = width / 2 + 110;
            int barWidth = 200;
            
            // Background
            g.setColor(new Color(60, 60, 60));
            g.fillRoundRect(barX, y - 15, barWidth, 30, 8, 8);
            
            // Filled portion with gradient
            if (owned > 0) {
                float fillRatio = (float) active / owned;
                int fillWidth = (int) (barWidth * fillRatio);
                
                GradientPaint barGradient = new GradientPaint(
                    barX, y - 15, new Color(143, 188, 187),
                    barX + fillWidth, y + 15, new Color(163, 190, 140)
                );
                g.setPaint(barGradient);
                g.fillRoundRect(barX, y - 15, fillWidth, 30, 8, 8);
            }
            
            // Border
            g.setColor(isSelected ? new Color(235, 203, 139) : Color.WHITE);
            g.setStroke(new BasicStroke(isSelected ? 3 : 2));
            g.drawRoundRect(barX, y - 15, barWidth, 30, 8, 8);
            
            // Draw text on bar
            g.setFont(new Font("Arial", Font.BOLD, 18));
            String barText = active + " / " + owned;
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.WHITE);
            g.drawString(barText, barX + (barWidth - fm.stringWidth(barText)) / 2, y + 5);
            
            // Draw plus button
            int plusX = width / 2 + 330;
            g.setColor(active < owned ? new Color(163, 190, 140) : new Color(80, 80, 80)); // Green when available, gray when maxed
            g.fillRoundRect(plusX, y - 20, 40, 40, 10, 10);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(plusX, y - 20, 40, 40, 10, 10);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("+", plusX + 11, y + 10);
            
            // Draw the upgrade name and owned count in a styled box
            g.setColor(new Color(40, 40, 40));
            g.fillRoundRect(width / 2 - 410, y - 30, 450, 70, 15, 15);
            
            // Selection indicator
            if (isSelected) {
                g.setColor(new Color(235, 203, 139));
                g.setStroke(new BasicStroke(3));
                g.drawRoundRect(width / 2 - 410, y - 30, 450, 70, 15, 15);
            } else {
                g.setColor(new Color(100, 100, 100));
                g.setStroke(new BasicStroke(1));
                g.drawRoundRect(width / 2 - 410, y - 30, 450, 70, 15, 15);
            }
            
            // Draw upgrade name
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.drawString(upgradeNames[i], width / 2 - 390, y);
            
            // Draw owned count
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(new Color(136, 192, 208)); // Palette cyan
            g.drawString("Owned: " + owned, width / 2 - 390, y + 28);
            
            y += 100;
        }
    }
    
    public void drawLevelSelect(Graphics2D g, int width, int height, int currentLevel, int maxUnlockedLevel, double time, double scrollOffset) {
        // Draw animated Balatro-style gradient
        drawAnimatedGradient(g, width, height, time, new Color[]{new Color(20, 25, 50), new Color(35, 40, 65), new Color(50, 35, 70)});
        
        // Draw title with glow effect
        g.setColor(new Color(255, 255, 255, 100));
        g.setFont(new Font("Arial", Font.BOLD, 52));
        String title = "SELECT LEVEL";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (width - fm.stringWidth(title)) / 2 + 2, 82);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString(title, (width - fm.stringWidth(title)) / 2, 80);
        
        // Draw instructions with icons
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        String instruction = "↑↓ Scroll | ←→ Select | SPACE Start | ESC Back";
        fm = g.getFontMetrics();
        g.setColor(new Color(200, 200, 200));
        g.drawString(instruction, (width - fm.stringWidth(instruction)) / 2, 125);
        
        // Create clipping region for scrollable area
        Shape oldClip = g.getClip();
        g.setClip(0, 160, width, height - 220);
        
        // Draw level grid - 3 columns per row
        int startY = 200 - (int)scrollOffset;
        int levelsPerRow = 3;
        int boxSize = 100;
        int spacing = 50;
        
        for (int i = 1; i <= 20; i++) {
            int row = (i - 1) / levelsPerRow;
            int col = (i - 1) % levelsPerRow;
            int x = width / 2 - (levelsPerRow * (boxSize + spacing)) / 2 + col * (boxSize + spacing);
            int y = startY + row * (boxSize + spacing);
            
            // Skip if outside visible area
            if (y < 140 || y > height - 60) continue;
            
            boolean isUnlocked = i <= maxUnlockedLevel;
            boolean isSelected = i == currentLevel;
            boolean isMegaBoss = (i % 3 == 0); // Every 3rd level
            
            // Draw card shadow
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRoundRect(x + 4, y + 4, boxSize, boxSize, 15, 15);
            
            // Draw box with card-style appearance
            if (isSelected) {
                // Animated glow effect
                float glowPulse = (float)(0.3 + 0.2 * Math.sin(time * 3));
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, glowPulse));
                g.setColor(new Color(255, 255, 150));
                g.setStroke(new BasicStroke(10));
                g.drawRoundRect(x - 8, y - 8, boxSize + 16, boxSize + 16, 20, 20);
                
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                g.setColor(new Color(255, 255, 200));
                g.setStroke(new BasicStroke(4));
                g.drawRoundRect(x - 4, y - 4, boxSize + 8, boxSize + 8, 18, 18);
            }
            
            // Fill color based on type
            if (isUnlocked) {
                if (isMegaBoss) {
                    // Gradient for mega bosses
                    GradientPaint gradient = new GradientPaint(
                        x, y, new Color(150, 50, 150),
                        x, y + boxSize, new Color(200, 100, 200)
                    );
                    g.setPaint(gradient);
                } else {
                    // Gradient for mini bosses
                    GradientPaint gradient = new GradientPaint(
                        x, y, new Color(40, 120, 60),
                        x, y + boxSize, new Color(60, 160, 80)
                    );
                    g.setPaint(gradient);
                }
            } else {
                g.setColor(new Color(60, 60, 60));
            }
            g.fillRoundRect(x, y, boxSize, boxSize, 15, 15);
            
            // Border
            g.setColor(isSelected ? new Color(255, 255, 255) : new Color(150, 150, 150));
            g.setStroke(new BasicStroke(isSelected ? 3 : 2));
            g.drawRoundRect(x, y, boxSize, boxSize, 15, 15);
            
            // Draw level number with shadow
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String levelNum = String.valueOf(i);
            FontMetrics fm2 = g.getFontMetrics();
            int textX = x + (boxSize - fm2.stringWidth(levelNum)) / 2;
            int textY = y + boxSize / 2 + 12;
            
            // Shadow
            g.setColor(new Color(0, 0, 0, 150));
            g.drawString(levelNum, textX + 2, textY + 2);
            
            // Main text
            g.setColor(isUnlocked ? Color.WHITE : new Color(100, 100, 100));
            g.drawString(levelNum, textX, textY);
            
            // Mega boss indicator
            if (isUnlocked && isMegaBoss) {
                g.setFont(new Font("Arial", Font.BOLD, 12));
                g.setColor(new Color(255, 215, 0));
                String megaText = "MEGA";
                fm2 = g.getFontMetrics();
                g.drawString(megaText, x + (boxSize - fm2.stringWidth(megaText)) / 2, y + boxSize - 8);
            }
            
            // Draw lock icon for locked levels
            if (!isUnlocked) {
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.setColor(new Color(200, 50, 50));
                String lockText = "[LOCKED]";
                fm2 = g.getFontMetrics();
                g.drawString(lockText, x + (boxSize - fm2.stringWidth(lockText)) / 2, y + 30);
            }
        }
        
        // Restore clip
        g.setClip(oldClip);
        
        // Draw fade overlay at top and bottom
        GradientPaint topFade = new GradientPaint(0, 160, new Color(20, 25, 50, 200), 0, 220, new Color(20, 25, 50, 0));
        g.setPaint(topFade);
        g.fillRect(0, 160, width, 60);
        
        GradientPaint bottomFade = new GradientPaint(0, height - 120, new Color(20, 25, 50, 0), 0, height - 60, new Color(20, 25, 50, 200));
        g.setPaint(bottomFade);
        g.fillRect(0, height - 120, width, 60);
        
        // Draw scroll indicators
        if (scrollOffset > 0) {
            // Up arrow
            g.setColor(new Color(255, 255, 255, 150));
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("▲", width / 2 - 10, 180);
        }
        
        int maxLevels = 20;
        int totalRows = (maxLevels + 2) / 3;
        int maxScroll = Math.max(0, startY + totalRows * 150 - height + 200);
        if (scrollOffset < maxScroll) {
            // Down arrow
            g.setColor(new Color(255, 255, 255, 150));
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("▼", width / 2 - 10, height - 40);
        }
    }
    
    public void drawGame(Graphics2D g, int width, int height, Player player, Boss boss, List<Bullet> bullets, List<Particle> particles, List<BeamAttack> beamAttacks, int level, double time, boolean bossVulnerable, int vulnerabilityTimer, int dodgeCombo, boolean showCombo, boolean bossDeathAnimation, double bossDeathScale, double bossDeathRotation) {
        // Draw vibrant animated sky gradient
        Color[] colors = getLevelGradientColors(level);
        drawAnimatedGradient(g, width, height, time, colors);
        
        // Draw beam attacks (behind everything else)
        for (BeamAttack beam : beamAttacks) {
            beam.draw(g, width, height);
        }
        
        // Draw particles (behind sprites)
        for (Particle particle : particles) {
            particle.draw(g);
        }
        
        // Draw player (only if not in death animation)
        if (player != null) {
            player.draw(g);
        }
        
        // Draw boss with special handling during death animation
        if (bossDeathAnimation) {
            // Save original transform
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Apply death animation transformations
            g2d.translate(boss.getX(), boss.getY());
            g2d.rotate(bossDeathRotation);
            g2d.scale(bossDeathScale, bossDeathScale);
            g2d.translate(-boss.getX(), -boss.getY());
            
            // Draw boss with transformations
            boss.draw(g2d);
            
            // Add red/orange tint for fire effect
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.setColor(new Color(255, 100, 0));
            double size = boss.getSize() * bossDeathScale;
            g2d.fillOval((int)(boss.getX() - size/2), (int)(boss.getY() - size/2), (int)size, (int)size);
            
            g2d.dispose();
        } else {
            // Normal boss drawing
            boss.draw(g);
            if (bossVulnerable) {
                // Pulsing ring around boss
                // Calculate color based on time remaining (green -> yellow -> red)
                double timeRatio = vulnerabilityTimer / 1200.0; // Normalize to 0-1
                Color circleColor;
                if (timeRatio > 0.5) {
                    // Green to Yellow (first half)
                    int green = 255;
                    int red = (int)(255 * (1 - (timeRatio - 0.5) * 2));
                    circleColor = new Color(red, green, 0, 150);
                } else {
                    // Yellow to Red (second half)
                    int red = 255;
                    int green = (int)(255 * (timeRatio * 2));
                    circleColor = new Color(red, green, 0, 150);
                }
                
                double pulseSize = 70 + Math.sin(time * 10) * 10;
                g.setColor(circleColor);
                g.setStroke(new BasicStroke(4f));
                g.drawOval((int)(boss.getX() - pulseSize/2), (int)(boss.getY() - pulseSize/2), (int)pulseSize, (int)pulseSize);
            }
        }
        
        // Draw bullets
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
        
        // Draw boss health bar at bottom
        if (boss != null) {
            int barWidth = 600;
            int barHeight = 40;
            int barX = (width - barWidth) / 2;
            int barY = height - 110;
            
            // Boss name and type
            String bossName = boss.getVehicleName();
            String bossType = boss.isMegaBoss() ? "[MEGA BOSS]" : "[MINI BOSS]";
            
            // Background panel with shadow
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRoundRect(barX + 3, barY + 3, barWidth, barHeight + 45, 15, 15);
            g.setColor(new Color(20, 20, 30, 200));
            g.fillRoundRect(barX, barY, barWidth, barHeight + 45, 15, 15);
            
            // Boss type label
            g.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g.getFontMetrics();
            Color typeColor = boss.isMegaBoss() ? new Color(255, 50, 50) : new Color(100, 200, 100);
            g.setColor(typeColor);
            g.drawString(bossType, barX + 10, barY + 18);
            
            // Boss name
            g.setFont(new Font("Arial", Font.BOLD, 18));
            fm = g.getFontMetrics();
            g.setColor(boss.isMegaBoss() ? new Color(255, 215, 0) : Color.WHITE);
            g.drawString(bossName, barX + 10, barY + 38);
            
            // Health bar background
            g.setColor(new Color(60, 60, 60));
            g.fillRoundRect(barX + 10, barY + 45, barWidth - 20, 15, 8, 8);
            
            // Health bar fill (always full - boss has no health system, just vulnerability window)
            GradientPaint healthGradient;
            if (boss.isMegaBoss()) {
                healthGradient = new GradientPaint(
                    barX + 10, 0, new Color(200, 50, 50),
                    barX + barWidth - 10, 0, new Color(255, 100, 100)
                );
            } else {
                healthGradient = new GradientPaint(
                    barX + 10, 0, new Color(50, 150, 50),
                    barX + barWidth - 10, 0, new Color(100, 200, 100)
                );
            }
            g.setPaint(healthGradient);
            g.fillRoundRect(barX + 10, barY + 45, barWidth - 20, 15, 8, 8);
            
            // Vulnerability indicator
            if (bossVulnerable) {
                // Calculate color based on time remaining (green -> yellow -> red)
                double timeRatio = vulnerabilityTimer / 1200.0;
                Color textColor;
                if (timeRatio > 0.5) {
                    int green = 255;
                    int red = (int)(255 * (1 - (timeRatio - 0.5) * 2));
                    textColor = new Color(red, green, 0);
                } else {
                    int red = 255;
                    int green = (int)(255 * (timeRatio * 2));
                    textColor = new Color(red, green, 0);
                }
                
                g.setColor(textColor);
                g.setFont(new Font("Arial", Font.BOLD, 14));
                String vulnText = "ATTACK NOW!";
                fm = g.getFontMetrics();
                int vulnX = barX + barWidth - fm.stringWidth(vulnText) - 15;
                g.drawString(vulnText, vulnX, barY + 18);
            }
            
            // Health bar border
            g.setColor(new Color(200, 200, 200));
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect(barX + 10, barY + 45, barWidth - 20, 15, 8, 8);
        }
        
        // Draw UI with better contrast
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(10, 10, 280, 90, 10, 10);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Level: " + level, 20, 35);
        g.drawString("Score: " + gameData.getScore(), 20, 65);
        g.drawString("Money: $" + (gameData.getTotalMoney() + gameData.getRunMoney()), 20, 95);
        
        // Draw combo counter
        if (showCombo && dodgeCombo > 1) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(width - 210, 10, 200, 60, 10, 10);
            
            g.setColor(new Color(163, 190, 140));
            g.setFont(new Font("Arial", Font.BOLD, 32));
            String comboText = "COMBO x" + dodgeCombo;
            g.drawString(comboText, width - 200, 50);
            
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.setColor(Color.WHITE);
            g.drawString("Lucky Dodges!", width - 200, 68);
        }
    }
    
    public void drawShop(Graphics2D g, int width, int height, double time) {
        // Draw animated Balatro-style gradient
        drawAnimatedGradient(g, width, height, time, new Color[]{new Color(45, 15, 55), new Color(60, 30, 70), new Color(75, 45, 85)});
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "UPGRADE SHOP";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (width - fm.stringWidth(title)) / 2, 80);
        
        // Show money
        g.setColor(new Color(163, 190, 140)); // Palette green
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String money = "Money: $" + gameData.getTotalMoney();
        fm = g.getFontMetrics();
        g.drawString(money, (width - fm.stringWidth(money)) / 2, 140);
        
        // Show earnings
        g.setColor(new Color(235, 203, 139)); // Palette yellow
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String earnings = "Earned this run: $" + gameData.getRunMoney();
        fm = g.getFontMetrics();
        g.drawString(earnings, (width - fm.stringWidth(earnings)) / 2, 180);
        
        // Shop items using buttons
        String[] items = shopManager.getShopItems();
        int y = 250;
        int selectedItem = shopManager.getSelectedShopItem();
        
        for (int i = 0; i < items.length; i++) {
            int cost = shopManager.getItemCost(i);
            boolean canAfford = gameData.getTotalMoney() >= cost || i == 3 || i == 5;
            
            // Build button text with cost
            String buttonText = items[i];
            if (i != 3 && i != 5) {
                buttonText += "  -  $" + cost;
            }
            
            // Update button appearance based on affordability
            if (!canAfford) {
                shopButtons[i] = new UIButton(buttonText, 0, 0, 800, 50, new Color(60, 60, 60), new Color(100, 100, 100));
            } else {
                shopButtons[i] = new UIButton(buttonText, 0, 0, 800, 50, new Color(76, 86, 106), new Color(180, 142, 173));
            }
            
            shopButtons[i].setPosition((width - 800) / 2, y - 30);
            shopButtons[i].update(i == selectedItem, time);
            shopButtons[i].draw(g, time);
            
            y += 80;
        }
        
        // Instructions
        g.setColor(new Color(216, 222, 233));
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String inst1 = "Use UP/DOWN to select | SPACE to purchase | ESC to continue";
        fm = g.getFontMetrics();
        g.drawString(inst1, (width - fm.stringWidth(inst1)) / 2, height - 50);
    }
    
    public void drawGameOver(Graphics2D g, int width, int height, double time) {
        // Draw animated Balatro-style gradient
        drawAnimatedGradient(g, width, height, time, new Color[]{new Color(50, 15, 15), new Color(65, 25, 25), new Color(45, 20, 30)});
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 72));
        String gameOver = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(gameOver, (width - fm.stringWidth(gameOver)) / 2, height / 2 - 50);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        String score = "Score: " + gameData.getScore();
        fm = g.getFontMetrics();
        g.drawString(score, (width - fm.stringWidth(score)) / 2, height / 2 + 20);
        
        String money = "Money Earned: $" + gameData.getRunMoney();
        fm = g.getFontMetrics();
        g.drawString(money, (width - fm.stringWidth(money)) / 2, height / 2 + 60);
        
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String retry = "Press SPACE to return to menu";
        fm = g.getFontMetrics();
        g.drawString(retry, (width - fm.stringWidth(retry)) / 2, height / 2 + 120);
    }
    
    public void drawWin(Graphics2D g, int width, int height, double time) {
        // Darker, more subdued gradient for victory screen
        drawAnimatedGradient(g, width, height, time, new Color[]{new Color(30, 40, 30), new Color(40, 50, 45), new Color(50, 60, 55)});
        
        g.setColor(new Color(163, 190, 140)); // Palette green
        g.setFont(new Font("Arial", Font.BOLD, 72));
        String win = "VICTORY!";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(win, (width - fm.stringWidth(win)) / 2, height / 2 - 50);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        String score = "Score: " + gameData.getScore();
        fm = g.getFontMetrics();
        g.drawString(score, (width - fm.stringWidth(score)) / 2, height / 2 + 20);
        
        String money = "Money Earned: $" + gameData.getRunMoney();
        fm = g.getFontMetrics();
        g.drawString(money, (width - fm.stringWidth(money)) / 2, height / 2 + 60);
        
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String inst = "Press SPACE to Visit Shop";
        fm = g.getFontMetrics();
        g.drawString(inst, (width - fm.stringWidth(inst)) / 2, height / 2 + 120);
    }
    
    public void drawSettings(Graphics2D g, int width, int height, int selectedItem, double time) {
        // Draw animated gradient with palette colors
        drawAnimatedGradient(g, width, height, time, new Color[]{new Color(46, 52, 64), new Color(59, 66, 82), new Color(76, 86, 106)});
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "SETTINGS";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (width - fm.stringWidth(title)) / 2, 80);
        
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(new Color(216, 222, 233));
        String subtitle = "Use UP/DOWN to navigate | SPACE or arrows to toggle";
        fm = g.getFontMetrics();
        g.drawString(subtitle, (width - fm.stringWidth(subtitle)) / 2, 120);
        
        // Settings items
        String[] settingNames = {"Gradient Animation", "Gradient Quality", "Grain Effect", "Particle Effects"};
        String[] settingValues = {
            Game.enableGradientAnimation ? "ON" : "OFF",
            Game.gradientQuality == 0 ? "Low" : Game.gradientQuality == 1 ? "Medium" : "High",
            Game.enableGrainEffect ? "ON" : "OFF",
            Game.enableParticles ? "ON" : "OFF"
        };
        
        String[] descriptions = {
            "Animate gradient backgrounds (may affect performance)",
            "Number of gradient layers (higher = better but slower)",
            "Add grain texture overlay (performance impact)",
            "Enable particle effects (trails, explosions, etc.)"
        };
        
        int y = 200;
        for (int i = 0; i < settingNames.length; i++) {
            // Build button text
            String buttonText = settingNames[i] + ": " + settingValues[i];
            settingsButtons[i] = new UIButton(buttonText, 0, 0, 700, 80, new Color(76, 86, 106), new Color(235, 203, 139));
            
            settingsButtons[i].setPosition((width - 700) / 2, y - 20);
            settingsButtons[i].update(i == selectedItem, time);
            settingsButtons[i].draw(g, time);
            
            // Draw description below if selected
            if (i == selectedItem) {
                g.setFont(new Font("Arial", Font.ITALIC, 16));
                g.setColor(new Color(216, 222, 233)); // Palette light gray
                fm = g.getFontMetrics();
                g.drawString(descriptions[i], (width - fm.stringWidth(descriptions[i])) / 2, y + 85);
            }
            
            y += 150;
        }
        
        // Instructions
        g.setColor(new Color(216, 222, 233));
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String inst = "Press ESC to return to menu";
        fm = g.getFontMetrics();
        g.drawString(inst, (width - fm.stringWidth(inst)) / 2, height - 50);
    }
    
    // Optimized Balatro-style animated gradient system
    private void drawAnimatedGradient(Graphics2D g, int width, int height, double time, Color[] colors) {
        // Determine offsets based on animation setting - made much more dramatic
        int offset1 = Game.enableGradientAnimation ? (int)(Math.sin(time * 0.5) * 150) : 0;
        int offset2 = Game.enableGradientAnimation ? (int)(Math.cos(time * 0.4) * 120) : 0;
        int offset3 = Game.enableGradientAnimation ? (int)(Math.sin(time * 0.6) * 130) : 0;
        
        // Base layer (always drawn)
        GradientPaint base = new GradientPaint(
            0, offset1, colors[0],
            0, height + offset1, colors[1]
        );
        g.setPaint(base);
        g.fillRect(0, 0, width, height);
        
        // Draw additional layers based on quality setting
        if (Game.gradientQuality >= 1) {
            // Second layer (Medium and High quality) - increased opacity
            Color accentColor = new Color(
                colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), 160
            );
            GradientPaint accent = new GradientPaint(
                width / 2, offset2, accentColor,
                width / 2, height + offset2, new Color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), 0)
            );
            g.setPaint(accent);
            g.fillRect(0, 0, width, height);
        }
        
        if (Game.gradientQuality >= 2) {
            // Third layer (High quality only) - increased opacity
            Color midColor = new Color(
                colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), 120
            );
            GradientPaint mid = new GradientPaint(
                offset3, 0, new Color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), 0),
                width + offset3, height, midColor
            );
            g.setPaint(mid);
            g.fillRect(0, 0, width, height);
        }
        
        // Optional grain effect
        if (Game.enableGrainEffect) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.03f));
            for (int i = 0; i < 150; i++) {
                int x = (int)(Math.random() * width);
                int y = (int)(Math.random() * height);
                int size = (int)(Math.random() * 2) + 1;
                g.setColor(Color.WHITE);
                g.fillRect(x, y, size, size);
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
    
    private Color[] getLevelGradientColors(int level) {
        // Vibrant sky gradients - much brighter and more colorful
        switch ((level - 1) % 7) {
            case 0: return new Color[]{new Color(135, 206, 250), new Color(100, 180, 255), new Color(70, 130, 220)}; // Bright sky blue
            case 1: return new Color[]{new Color(255, 200, 100), new Color(255, 150, 80), new Color(135, 206, 250)}; // Sunset orange to blue
            case 2: return new Color[]{new Color(255, 120, 150), new Color(180, 100, 200), new Color(100, 150, 255)}; // Pink to purple to blue
            case 3: return new Color[]{new Color(100, 220, 255), new Color(120, 200, 255), new Color(140, 180, 255)}; // Cyan sky
            case 4: return new Color[]{new Color(255, 180, 100), new Color(255, 140, 120), new Color(180, 140, 220)}; // Warm sunset
            case 5: return new Color[]{new Color(200, 230, 255), new Color(150, 200, 255), new Color(120, 170, 240)}; // Clear day sky
            default: return new Color[]{new Color(120, 200, 255), new Color(100, 180, 240), new Color(80, 150, 220)}; // Deep sky blue
        }
    }
    
    private void drawClouds(Graphics2D g, int width, int height, double time) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        
        // Draw multiple layers of clouds
        for (int layer = 0; layer < 3; layer++) {
            double speed = 0.3 + (layer * 0.15);
            int yBase = 50 + (layer * 80);
            int cloudSize = 40 + (layer * 15);
            float alpha = 0.7f - (layer * 0.15f);
            
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            
            // Draw 4-6 clouds per layer
            for (int i = 0; i < 5; i++) {
                double xOffset = ((time * speed * 10) + (i * 250)) % (width + 200);
                int x = (int)xOffset - 100;
                int y = yBase + (int)(Math.sin(time * 0.5 + i) * 20);
                
                drawCloud(g, x, y, cloudSize);
            }
        }
        
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    private void drawCloud(Graphics2D g, int x, int y, int size) {
        g.setColor(Color.WHITE);
        
        // Draw fluffy cloud shape with multiple circles
        g.fillOval(x, y, size, size);
        g.fillOval(x + size / 3, y - size / 4, (int)(size * 1.2), (int)(size * 1.2));
        g.fillOval(x + (int)(size * 0.6), y, size, size);
        g.fillOval(x + size, y + size / 6, (int)(size * 0.8), (int)(size * 0.8));
        g.fillOval(x + size / 2, y + size / 4, (int)(size * 0.9), (int)(size * 0.9));
    }
    
    private void drawScrollingTerrain(Graphics2D g, int width, int height, int level, double time) {
        // Different terrain for each level - top-down view scrolling downward
        int terrainType = (level - 1) % 7;
        double scrollSpeed = 2.0;
        double scrollOffset = (time * scrollSpeed) % 100;
        
        // Apply blur effect to terrain for motion blur
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        switch (terrainType) {
            case 0: // Forest
                drawForest(g, width, height, scrollOffset);
                break;
            case 1: // Ocean
                drawOcean(g, width, height, scrollOffset);
                break;
            case 2: // Desert
                drawDesert(g, width, height, scrollOffset);
                break;
            case 3: // Mountains
                drawMountains(g, width, height, scrollOffset);
                break;
            case 4: // Lakes/Rivers
                drawLakes(g, width, height, scrollOffset);
                break;
            case 5: // City
                drawCity(g, width, height, scrollOffset);
                break;
            case 6: // Tundra
                drawTundra(g, width, height, scrollOffset);
                break;
        }
        
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    private void drawForest(Graphics2D g, int width, int height, double scroll) {
        // Draw trees from top-down view
        for (int row = -2; row < 12; row++) {
            for (int col = 0; col < 15; col++) {
                int x = col * 90 + ((row % 2) * 45);
                int y = (int)(row * 80 - scroll * 8);
                if (y > -50 && y < height + 50) {
                    // Motion blur streak
                    g.setColor(new Color(34, 139, 34, 60));
                    g.fillOval(x, y - 8, 40, 56);
                    
                    // Tree (top-down circular canopy)
                    g.setColor(new Color(34, 139, 34, 180));
                    g.fillOval(x, y, 40, 40);
                    g.setColor(new Color(20, 100, 20, 180));
                    g.fillOval(x + 5, y + 5, 30, 30);
                }
            }
        }
    }
    
    private void drawOcean(Graphics2D g, int width, int height, double scroll) {
        // Draw waves and islands
        for (int row = -1; row < 10; row++) {
            int y = (int)(row * 100 - scroll * 8);
            if (y > -60 && y < height + 60) {
                // Motion blur for waves
                g.setColor(new Color(30, 144, 255, 60));
                g.setStroke(new BasicStroke(3));
                for (int x = 0; x < width; x += 40) {
                    g.drawArc(x, y - 5, 40, 25, 0, 180);
                }
                
                // Waves
                g.setColor(new Color(30, 144, 255, 120));
                g.setStroke(new BasicStroke(3));
                for (int x = 0; x < width; x += 40) {
                    g.drawArc(x, y, 40, 20, 0, 180);
                }
                
                // Occasional islands
                if (row % 3 == 0) {
                    int islandX = (row * 137) % (width - 100);
                    g.setColor(new Color(139, 69, 19, 150));
                    g.fillOval(islandX, y + 30, 80, 50);
                    g.setColor(new Color(34, 139, 34, 150));
                    g.fillOval(islandX + 10, y + 25, 30, 30);
                    g.fillOval(islandX + 40, y + 20, 35, 35);
                }
            }
        }
    }
    
    private void drawDesert(Graphics2D g, int width, int height, double scroll) {
        // Draw sand dunes and cacti
        for (int row = -1; row < 8; row++) {
            int y = (int)(row * 120 - scroll * 8);
            if (y > -80 && y < height + 80) {
                // Sand dunes
                g.setColor(new Color(237, 201, 175, 150));
                int duneX = (row * 200) % width;
                g.fillOval(duneX - 50, y, 150, 60);
                g.fillOval(duneX + 100, y + 20, 200, 80);
                
                // Cacti
                if (row % 2 == 1) {
                    int cactusX = (row * 173) % (width - 40);
                    g.setColor(new Color(107, 142, 35, 180));
                    g.fillRect(cactusX + 15, y + 30, 10, 40);
                    g.fillRect(cactusX + 5, y + 40, 10, 15);
                    g.fillRect(cactusX + 25, y + 45, 10, 15);
                }
            }
        }
    }
    
    private void drawMountains(Graphics2D g, int width, int height, double scroll) {
        // Draw mountain peaks from above
        for (int row = -1; row < 6; row++) {
            int y = (int)(row * 150 - scroll * 8);
            if (y > -100 && y < height + 100) {
                int baseX = (row * 117) % (width - 200);
                // Mountain mass
                g.setColor(new Color(105, 105, 105, 150));
                int[] xPoints = {baseX, baseX + 100, baseX + 200, baseX + 150, baseX + 50};
                int[] yPoints = {y + 100, y, y + 100, y + 80, y + 80};
                g.fillPolygon(xPoints, yPoints, 5);
                
                // Snow cap
                g.setColor(new Color(255, 255, 255, 180));
                int[] snowX = {baseX + 70, baseX + 100, baseX + 130};
                int[] snowY = {y + 30, y, y + 30};
                g.fillPolygon(snowX, snowY, 3);
            }
        }
    }
    
    private void drawLakes(Graphics2D g, int width, int height, double scroll) {
        // Draw lakes and rivers
        for (int row = -1; row < 10; row++) {
            int y = (int)(row * 90 - scroll * 8);
            if (y > -60 && y < height + 60) {
                // Rivers (winding)
                g.setColor(new Color(30, 144, 255, 130));
                int riverX = width / 3 + (int)(Math.sin(row * 0.5) * 100);
                g.fillRoundRect(riverX, y, 80, 100, 30, 30);
                
                // Lakes
                if (row % 3 == 0) {
                    int lakeX = (row * 211) % (width - 150);
                    g.setColor(new Color(64, 164, 223, 140));
                    g.fillOval(lakeX, y + 20, 120, 80);
                    
                    // Grass around lake
                    g.setColor(new Color(34, 139, 34, 120));
                    g.fillOval(lakeX - 10, y + 10, 140, 100);
                }
            }
        }
    }
    
    private void drawCity(Graphics2D g, int width, int height, double scroll) {
        // Draw buildings from above (top-down)
        for (int row = -1; row < 15; row++) {
            for (int col = 0; col < 10; col++) {
                int x = col * 130 + ((row % 2) * 65);
                int y = (int)(row * 60 - scroll * 8);
                if (y > -50 && y < height + 50) {
                    // Buildings
                    int buildingSize = 40 + ((row + col) % 3) * 15;
                    g.setColor(new Color(128, 128, 128, 180));
                    g.fillRect(x, y, buildingSize, buildingSize);
                    
                    // Windows/details
                    g.setColor(new Color(255, 255, 200, 150));
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            g.fillRect(x + 5 + i * 12, y + 5 + j * 12, 8, 8);
                        }
                    }
                }
            }
        }
    }
    
    private void drawTundra(Graphics2D g, int width, int height, double scroll) {
        // Draw snowy tundra with rocks and ice
        for (int row = -1; row < 12; row++) {
            int y = (int)(row * 70 - scroll * 8);
            if (y > -50 && y < height + 50) {
                // Snow patches
                g.setColor(new Color(255, 255, 255, 140));
                for (int i = 0; i < 5; i++) {
                    int x = (row * 83 + i * 230) % width;
                    g.fillOval(x, y, 60 + i * 10, 40 + i * 5);
                }
                
                // Rocks
                if (row % 2 == 0) {
                    int rockX = (row * 149) % (width - 50);
                    g.setColor(new Color(105, 105, 105, 160));
                    g.fillOval(rockX, y + 15, 35, 25);
                    g.fillOval(rockX + 20, y + 20, 30, 20);
                }
            }
        }
    }
}
