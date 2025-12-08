import java.awt.*;
import java.util.List;

public class Boss {
    private double x, y;
    private double prevX, prevY; // Previous position for rotation calculation
    private int level;
    private static final int SIZE = 60;
    private int shootTimer;
    private int shootInterval;
    private int patternType;
    private double targetX, targetY; // Target position for smooth movement
    private int moveTimer; // Timer to pick new target
    private static final double MOVE_SPEED = 1.5; // Smooth movement speed
    
    public Boss(double x, double y, int level) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.level = level;
        this.shootTimer = 0;
        this.shootInterval = Math.max(20, 60 - level * 5); // Faster shooting at higher levels
        // Randomize starting pattern so each boss is different
        this.patternType = (int)(Math.random() * 12);
        // Start with current position as target
        this.targetX = x;
        this.targetY = y;
        this.moveTimer = 0;
    }
    
    public void update(List<Bullet> bullets, Player player, int screenWidth, int screenHeight) {
        update(bullets, player, screenWidth, screenHeight, 1.0);
    }
    
    public void update(List<Bullet> bullets, Player player, int screenWidth, int screenHeight, double deltaTime) {
        // Smooth movement to target position
        moveTimer += deltaTime;
        
        // Pick a new random target every 60-120 frames (1-2 seconds)
        if (moveTimer >= 60 + Math.random() * 60) {
            moveTimer = 0;
            
            // Pick random position in the top half of screen with some margin
            targetX = SIZE + Math.random() * (screenWidth - SIZE * 2);
            targetY = SIZE + Math.random() * (screenHeight / 2.5 - SIZE * 2);
        }
        
        // Smoothly move towards target
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > MOVE_SPEED) {
            // Store previous position for rotation
            prevX = x;
            prevY = y;
            // Move towards target at constant speed (scaled by delta time)
            double moveSpeedAdjusted = MOVE_SPEED * (1.0 + level * 0.1) * deltaTime;
            x += (dx / distance) * moveSpeedAdjusted;
            y += (dy / distance) * moveSpeedAdjusted;
        }
        
        // Keep boss within bounds
        x = Math.max(SIZE, Math.min(screenWidth - SIZE, x));
        y = Math.max(SIZE, Math.min(screenHeight / 3, y));
        
        // Shooting pattern (scaled by delta time)
        shootTimer += deltaTime;
        if (shootTimer >= shootInterval) {
            shootTimer = 0;
            shoot(bullets, player);
        }
    }
    
    private void shoot(List<Bullet> bullets, Player player) {
        patternType = (patternType + 1) % (3 + level); // More patterns at higher levels
        
        switch (patternType % 12) {
            case 0: // Spiral pattern
                shootSpiral(bullets);
                break;
            case 1: // Circle pattern
                shootCircle(bullets, 8 + level * 2);
                break;
            case 2: // Aimed at player
                shootAtPlayer(bullets, player, 3);
                break;
            case 3: // Wave pattern
                shootWave(bullets);
                break;
            case 4: // Random spray
                shootRandom(bullets, 5 + level);
                break;
            case 5: // Fast bullets
                shootFast(bullets, player);
                break;
            case 6: // Large bullets
                shootLarge(bullets);
                break;
            case 7: // Mixed attack
                shootMixed(bullets, player);
                break;
            case 8: // Spiral bullets
                shootSpiralBullets(bullets);
                break;
            case 9: // Splitting bullets
                shootSplittingBullets(bullets);
                break;
            case 10: // Accelerating bullets
                shootAcceleratingBullets(bullets, player);
                break;
            case 11: // Wave bullets
                shootWaveBullets(bullets);
                break;
        }
    }
    
    private void shootSpiral(List<Bullet> bullets) {
        int numBullets = 6 + level;
        double angleOffset = shootTimer * 0.1;
        for (int i = 0; i < numBullets; i++) {
            double angle = (Math.PI * 2 * i / numBullets) + angleOffset;
            bullets.add(new Bullet(x, y, Math.cos(angle) * 3, Math.sin(angle) * 3));
        }
    }
    
    private void shootCircle(List<Bullet> bullets, int numBullets) {
        for (int i = 0; i < numBullets; i++) {
            double angle = Math.PI * 2 * i / numBullets;
            bullets.add(new Bullet(x, y, Math.cos(angle) * 2.5, Math.sin(angle) * 2.5));
        }
    }
    
    private void shootAtPlayer(List<Bullet> bullets, Player player, int spread) {
        double angleToPlayer = Math.atan2(player.getY() - y, player.getX() - x);
        for (int i = -spread; i <= spread; i++) {
            double angle = angleToPlayer + (i * 0.2);
            bullets.add(new Bullet(x, y, Math.cos(angle) * 4, Math.sin(angle) * 4));
        }
    }
    
    private void shootWave(List<Bullet> bullets) {
        int numBullets = 10 + level * 2;
        for (int i = 0; i < numBullets; i++) {
            double angle = Math.PI / 4 + (Math.PI / 2 * i / numBullets);
            double speed = 2 + Math.sin(i * 0.5) * 1.5;
            bullets.add(new Bullet(x, y, Math.cos(angle) * speed, Math.sin(angle) * speed));
        }
    }
    
    private void shootRandom(List<Bullet> bullets, int numBullets) {
        for (int i = 0; i < numBullets; i++) {
            double angle = Math.random() * Math.PI * 2;
            double speed = 2 + Math.random() * 2;
            bullets.add(new Bullet(x, y, Math.cos(angle) * speed, Math.sin(angle) * speed));
        }
    }
    
    private void shootFast(List<Bullet> bullets, Player player) {
        double angleToPlayer = Math.atan2(player.getY() - y, player.getX() - x);
        for (int i = 0; i < 5 + level; i++) {
            double angle = angleToPlayer + (Math.random() - 0.5) * 0.5;
            bullets.add(new Bullet(x, y, Math.cos(angle) * 6, Math.sin(angle) * 6, Bullet.BulletType.FAST));
        }
    }
    
    private void shootLarge(List<Bullet> bullets) {
        int numBullets = 4 + level;
        for (int i = 0; i < numBullets; i++) {
            double angle = Math.PI * 2 * i / numBullets;
            bullets.add(new Bullet(x, y, Math.cos(angle) * 1.5, Math.sin(angle) * 1.5, Bullet.BulletType.LARGE));
        }
    }
    
    private void shootMixed(List<Bullet> bullets, Player player) {
        // Combination attack with different bullet types
        double angleToPlayer = Math.atan2(player.getY() - y, player.getX() - x);
        
        // Homing bullets
        for (int i = 0; i < 2; i++) {
            double angle = angleToPlayer + (i - 0.5) * 0.3;
            bullets.add(new Bullet(x, y, Math.cos(angle) * 2.5, Math.sin(angle) * 2.5, Bullet.BulletType.HOMING));
        }
        
        // Circle of bouncing bullets
        if (level >= 3) {
            for (int i = 0; i < 6; i++) {
                double angle = Math.PI * 2 * i / 6;
                bullets.add(new Bullet(x, y, Math.cos(angle) * 3, Math.sin(angle) * 3, Bullet.BulletType.BOUNCING));
            }
        }
    }
    
    private void shootSpiralBullets(List<Bullet> bullets) {
        int numBullets = 5 + level;
        for (int i = 0; i < numBullets; i++) {
            double angle = Math.PI * 2 * i / numBullets;
            bullets.add(new Bullet(x, y, Math.cos(angle) * 2, Math.sin(angle) * 2, Bullet.BulletType.SPIRAL));
        }
    }
    
    private void shootSplittingBullets(List<Bullet> bullets) {
        int numBullets = 3 + level / 2;
        for (int i = 0; i < numBullets; i++) {
            double angle = Math.PI * 2 * i / numBullets;
            bullets.add(new Bullet(x, y, Math.cos(angle) * 2.5, Math.sin(angle) * 2.5, Bullet.BulletType.SPLITTING));
        }
    }
    
    private void shootAcceleratingBullets(List<Bullet> bullets, Player player) {
        double angleToPlayer = Math.atan2(player.getY() - y, player.getX() - x);
        for (int i = -2; i <= 2; i++) {
            double angle = angleToPlayer + i * 0.3;
            bullets.add(new Bullet(x, y, Math.cos(angle) * 1.5, Math.sin(angle) * 1.5, Bullet.BulletType.ACCELERATING));
        }
    }
    
    private void shootWaveBullets(List<Bullet> bullets) {
        int numBullets = 8 + level;
        for (int i = 0; i < numBullets; i++) {
            double angle = Math.PI / 4 + (Math.PI / 2 * i / numBullets);
            bullets.add(new Bullet(x, y, Math.cos(angle) * 2.5, Math.sin(angle) * 2.5, Bullet.BulletType.WAVE));
        }
    }
    
    public void draw(Graphics2D g) {
        // Calculate rotation angle based on movement direction
        double angle = Math.atan2(y - prevY, x - prevX);
        // If stationary, maintain downward facing (towards player)
        if (x == prevX && y == prevY) {
            angle = Math.PI / 2;
        }
        
        // Save original transform
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(x, y);
        g2d.rotate(angle);
        
        // Draw fighter plane
        int planeLength = SIZE;
        int wingSpan = SIZE + 20;
        
        // Main fuselage (dark gray)
        g2d.setColor(new Color(76, 86, 106)); // Palette dark blue-gray
        g2d.fillRect(-planeLength/2, -SIZE/6, planeLength, SIZE/3);
        
        // Cockpit (canopy)
        g2d.setColor(new Color(136, 192, 208, 180)); // Palette cyan
        int[] canopyX = {planeLength/6, planeLength/3, planeLength/3, planeLength/6};
        int[] canopyY = {-SIZE/8, -SIZE/6, SIZE/6, SIZE/8};
        g2d.fillPolygon(canopyX, canopyY, 4);
        
        // Nose cone
        g2d.setColor(new Color(94, 129, 172)); // Palette blue
        int[] noseX = {planeLength/2, planeLength/2 + 12, planeLength/2};
        int[] noseY = {-SIZE/6, 0, SIZE/6};
        g2d.fillPolygon(noseX, noseY, 3);
        
        // Main wings
        g2d.setColor(new Color(59, 66, 82)); // Palette dark gray
        int[] wingX = {-planeLength/4, planeLength/6, planeLength/4, -planeLength/4};
        int[] wingTopY = {-SIZE/6, -SIZE/6, -wingSpan/2, -SIZE/6};
        g2d.fillPolygon(wingX, wingTopY, 4);
        
        int[] wingBottomY = {SIZE/6, SIZE/6, wingSpan/2, SIZE/6};
        g2d.fillPolygon(wingX, wingBottomY, 4);
        
        // Tail fins
        g2d.setColor(new Color(76, 86, 106)); // Palette dark blue-gray
        int[] tailFinX = {-planeLength/2, -planeLength/2 + 8, -planeLength/2};
        int[] tailFinTopY = {-SIZE/6, -SIZE/6, -SIZE/3};
        g2d.fillPolygon(tailFinX, tailFinTopY, 3);
        
        int[] tailFinBottomY = {SIZE/6, SIZE/6, SIZE/3};
        g2d.fillPolygon(tailFinX, tailFinBottomY, 3);
        
        // Engine exhausts (glowing)
        g2d.setColor(new Color(208, 135, 112, 180)); // Palette orange
        g2d.fillOval(-planeLength/2 - 6, -SIZE/8, 8, SIZE/8);
        g2d.fillOval(-planeLength/2 - 6, SIZE/16, 8, SIZE/8);
        
        // Detail lines (panel lines)
        g2d.setColor(new Color(46, 52, 64)); // Palette darkest
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(-planeLength/2, -SIZE/6, planeLength, SIZE/3);
        g2d.drawLine(0, -SIZE/6, 0, SIZE/6);
        g2d.drawLine(-planeLength/4, -SIZE/6, -planeLength/4, SIZE/6);
        
        // Weapons/missiles under wings
        g2d.setColor(new Color(129, 161, 193)); // Palette light blue
        g2d.fillRect(0, -wingSpan/3, 6, 3);
        g2d.fillRect(0, wingSpan/3 - 3, 6, 3);
        
        g2d.dispose();
        
        // Draw level indicator below plane
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String planeName = getPlaneName(level);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(planeName, (int)x - fm.stringWidth(planeName)/2, (int)y + SIZE/2 + 15);
        
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String levelText = "LV " + level;
        fm = g.getFontMetrics();
        g.drawString(levelText, (int)x - fm.stringWidth(levelText)/2, (int)y + SIZE/2 + 32);
    }
    
    private String getPlaneName(int lvl) {
        switch (lvl % 10) {
            case 1: return "✈ MIG-15";
            case 2: return "✈ F-86 SABRE";
            case 3: return "✈ MIG-21";
            case 4: return "✈ F-4 PHANTOM";
            case 5: return "✈ MIG-29";
            case 6: return "✈ F-15 EAGLE";
            case 7: return "✈ SU-27";
            case 8: return "✈ F-22 RAPTOR";
            case 9: return "✈ SU-57";
            default: return "✈ F-35 LIGHTNING";
        }
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getSize() { return SIZE; }
}
