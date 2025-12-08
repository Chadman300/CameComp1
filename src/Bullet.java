import java.awt.*;

public class Bullet {
    private double x, y;
    private double vx, vy;
    private static final int SIZE = 8;
    private BulletType type;
    private int warningTime;
    private static final int WARNING_DURATION = 45; // Frames before bullet activates
    private double age; // Frames since activation
    private double spiralAngle; // For spiral bullets
    private boolean hasSplit; // For splitting bullets
    
    public enum BulletType {
        NORMAL,      // Standard bullet
        FAST,        // Faster, smaller bullet
        LARGE,       // Slower, larger bullet
        HOMING,      // Slightly tracks player
        BOUNCING,    // Bounces off walls
        SPIRAL,      // Spirals as it moves
        SPLITTING,   // Splits into smaller bullets
        ACCELERATING,// Speeds up over time
        WAVE         // Moves in a wave pattern
    }
    
    public Bullet(double x, double y, double vx, double vy) {
        this(x, y, vx, vy, BulletType.NORMAL);
    }
    
    public Bullet(double x, double y, double vx, double vy, BulletType type) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.type = type;
        this.warningTime = WARNING_DURATION;
        this.age = 0;
        this.spiralAngle = 0;
        this.hasSplit = false;
    }
    
    public void update() {
        update(null, 0, 0, 1.0);
    }
    
    public void update(Player player, int screenWidth, int screenHeight) {
        update(player, screenWidth, screenHeight, 1.0);
    }
    
    public void update(Player player, int screenWidth, int screenHeight, double deltaTime) {
        if (warningTime > 0) {
            warningTime -= deltaTime;
            return;
        }
        
        age += deltaTime;
        
        // Type-specific behavior
        switch (type) {
            case FAST:
                // Already faster from initial velocity
                break;
            case HOMING:
                if (player != null) {
                    // Slightly adjust direction towards player
                    double angleToPlayer = Math.atan2(player.getY() - y, player.getX() - x);
                    double currentAngle = Math.atan2(vy, vx);
                    double angleDiff = angleToPlayer - currentAngle;
                    // Normalize angle
                    while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;
                    while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;
                    // Turn slightly towards player (scaled by delta time)
                    currentAngle += angleDiff * 0.02 * deltaTime;
                    double speed = Math.sqrt(vx * vx + vy * vy);
                    vx = Math.cos(currentAngle) * speed;
                    vy = Math.sin(currentAngle) * speed;
                }
                break;
            case BOUNCING:
                // Bounce off walls
                if (x < 10 || x > screenWidth - 10) vx *= -1;
                if (y < 10 || y > screenHeight - 10) vy *= -1;
                break;
            case SPIRAL:
                // Rotate velocity vector to create spiral motion
                spiralAngle += 0.08;
                double currentSpeed = Math.sqrt(vx * vx + vy * vy);
                double baseAngle = Math.atan2(vy, vx);
                vx = Math.cos(baseAngle + Math.sin(spiralAngle) * 0.5) * currentSpeed;
                vy = Math.sin(baseAngle + Math.sin(spiralAngle) * 0.5) * currentSpeed;
                break;
            case ACCELERATING:
                // Speed up over time
                double accelFactor = 1 + (age * 0.01);
                vx *= Math.min(accelFactor, 1.05);
                vy *= Math.min(accelFactor, 1.05);
                break;
            case WAVE:
                // Move in sine wave pattern
                double perpAngle = Math.atan2(vy, vx) + Math.PI / 2;
                double waveOffset = Math.sin(age * 0.2) * 2 * deltaTime;
                x += Math.cos(perpAngle) * waveOffset;
                y += Math.sin(perpAngle) * waveOffset;
                break;
            default:
                break;
        }
        
        // Move bullet (scaled by delta time)
        x += vx * deltaTime;
        y += vy * deltaTime;
    }
    
    public void applySlow(double factor) {
        vx *= factor;
        vy *= factor;
    }
    
    public void draw(Graphics2D g) {
        // Draw warning indicator during warning phase
        if (warningTime > 0) {
            float alpha = (float)(warningTime % 20) / 20.0f;
            g.setColor(new Color(191, 97, 106, (int)(alpha * 180))); // Palette red
            int warningSize = 20 + (WARNING_DURATION - warningTime) / 3;
            
            // Draw crosshair warning
            g.setStroke(new BasicStroke(3));
            g.drawLine((int)x - warningSize, (int)y, (int)x + warningSize, (int)y);
            g.drawLine((int)x, (int)y - warningSize, (int)x, (int)y + warningSize);
            
            // Draw warning circle
            g.setStroke(new BasicStroke(2));
            g.drawOval((int)x - warningSize/2, (int)y - warningSize/2, warningSize, warningSize);
            return;
        }
        
        // Calculate rotation angle based on velocity
        double angle = Math.atan2(vy, vx);
        
        // Type-specific appearance
        int size = SIZE;
        
        // Draw rotating projectile based on type
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(x, y);
        g2d.rotate(angle);
        
        switch (type) {
            case FAST:
                // Tracer round
                size = SIZE - 2;
                g2d.setColor(new Color(235, 203, 139, 120)); // Palette yellow glow
                g2d.fillOval(-size*2, -size/2, size*3, size);
                g2d.setColor(new Color(235, 203, 139)); // Palette yellow
                g2d.fillRect(-size/2, -size/3, size*2, size*2/3);
                g2d.setColor(new Color(208, 135, 112)); // Palette orange
                int[] tracerX = {size*3/2, size*2, size*3/2};
                int[] tracerY = {-size/3, 0, size/3};
                g2d.fillPolygon(tracerX, tracerY, 3);
                break;
            case LARGE:
                // Heavy shell
                size = SIZE + 4;
                g2d.setColor(new Color(76, 86, 106)); // Palette dark blue-gray
                g2d.fillRect(-size, -size/2, size*2, size);
                g2d.setColor(new Color(191, 97, 106)); // Palette red
                g2d.fillOval(size/2, -size/2, size, size);
                g2d.setColor(new Color(94, 129, 172)); // Palette blue
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(-size, -size/2, size*2, size);
                break;
            case HOMING:
                // Missile
                size = SIZE;
                g2d.setColor(new Color(180, 142, 173)); // Palette purple
                g2d.fillRect(-size, -size/3, size*2, size*2/3);
                g2d.setColor(new Color(163, 190, 140)); // Palette light green
                int[] missileX = {size, size + size/2, size};
                int[] missileY = {-size/3, 0, size/3};
                g2d.fillPolygon(missileX, missileY, 3);
                // Fins
                g2d.setColor(new Color(143, 188, 187)); // Palette teal
                int[] finX = {-size, -size, -size/2};
                int[] finY1 = {-size/3, -size, -size/3};
                g2d.fillPolygon(finX, finY1, 3);
                int[] finY2 = {size/3, size, size/3};
                g2d.fillPolygon(finX, finY2, 3);
                // Exhaust
                g2d.setColor(new Color(208, 135, 112, 180)); // Palette orange
                g2d.fillOval(-size*3/2, -size/4, size, size/2);
                break;
            case BOUNCING:
                // Explosive round
                size = SIZE;
                g2d.setColor(new Color(163, 190, 140)); // Palette green
                g2d.fillOval(-size/2, -size/2, size, size);
                g2d.setColor(new Color(163, 190, 140)); // Palette green bright
                g2d.fillOval(-size/3, -size/3, size*2/3, size*2/3);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(-size/2, -size/2, size, size);
                break;
            case SPIRAL:
                // Spinning projectile
                size = SIZE;
                g2d.rotate(spiralAngle);
                g2d.setColor(new Color(136, 192, 208)); // Palette cyan
                int[] bladeX = {-size/2, size/2, size/2, -size/2};
                int[] bladeY = {-size/4, -size/8, size/8, size/4};
                g2d.fillPolygon(bladeX, bladeY, 4);
                g2d.setColor(new Color(143, 188, 187)); // Palette teal
                g2d.fillOval(-size/3, -size/3, size*2/3, size*2/3);
                break;
            case SPLITTING:
                // Cluster bomb
                size = SIZE + 4;
                g2d.setColor(new Color(208, 135, 112)); // Palette orange
                g2d.fillRect(-size/2, -size/2, size, size);
                g2d.setColor(new Color(235, 203, 139)); // Palette yellow
                g2d.fillOval(-size/3, -size/3, size*2/3, size*2/3);
                if (age > 40 && age < 60) {
                    g2d.setColor(new Color(235, 203, 139, 150)); // Palette yellow glow
                    int pulse = (int)(Math.sin(age * 0.5) * 4);
                    g2d.drawOval(-size/2 - pulse, -size/2 - pulse, size + pulse*2, size + pulse*2);
                }
                break;
            case ACCELERATING:
                // Rocket
                size = SIZE;
                g2d.setColor(new Color(180, 142, 173)); // Palette purple
                g2d.fillRect(-size, -size/4, size*2, size/2);
                g2d.setColor(new Color(191, 97, 106)); // Palette red
                int[] rocketX = {size, size + size/2, size};
                int[] rocketY = {-size/4, 0, size/4};
                g2d.fillPolygon(rocketX, rocketY, 3);
                // Flame trail
                g2d.setColor(new Color(208, 135, 112, 180)); // Palette orange
                g2d.fillOval(-size*2, -size/3, size, size*2/3);
                break;
            case WAVE:
                // Energy pulse
                size = SIZE;
                g2d.setColor(new Color(136, 192, 208, 150)); // Palette cyan glow
                g2d.fillOval(-size, -size, size*2, size*2);
                g2d.setColor(new Color(143, 188, 187)); // Palette teal
                g2d.fillOval(-size/2, -size/2, size, size);
                break;
            default:
                // Standard cannon round
                g2d.setColor(new Color(191, 97, 106)); // Palette red
                g2d.fillRect(-size/2, -size/3, size, size*2/3);
                g2d.setColor(new Color(208, 135, 112)); // Palette orange
                g2d.fillOval(0, -size/3, size*2/3, size*2/3);
                break;
        }
        
        g2d.dispose();
    }
    
    public boolean isOffScreen(int width, int height) {
        // Bouncing bullets never go off screen
        if (type == BulletType.BOUNCING) return false;
        return x < -20 || x > width + 20 || y < -20 || y > height + 20;
    }
    
    public boolean collidesWith(Player player) {
        // No collision during warning phase
        if (warningTime > 0) return false;
        
        double dx = x - player.getX();
        double dy = y - player.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        int effectiveSize = SIZE;
        if (type == BulletType.FAST) effectiveSize = SIZE - 2;
        if (type == BulletType.LARGE || type == BulletType.SPLITTING) effectiveSize = SIZE + 4;
        
        return distance < effectiveSize/2 + 2; // Using player's small hitbox (2 pixels)
    }
    
    public boolean shouldSplit() {
        return type == BulletType.SPLITTING && !hasSplit && age > 60;
    }
    
    public void markAsSplit() {
        hasSplit = true;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getVX() { return vx; }
    public double getVY() { return vy; }
    
    public boolean isActive() {
        return warningTime <= 0;
    }
}
