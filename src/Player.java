import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    private double x, y;
    private double vx, vy; // Velocity
    private static final int SIZE = 20;
    private static final double MAX_SPEED = 6.0;
    private static final double ACCELERATION = 0.5;
    private static final double FRICTION = 0.85;
    private double speedMultiplier;
    private int flickerTimer; // For Lucky Dodge animation
    private static final int FLICKER_DURATION = 15; // Frames to flicker
    
    public Player(double x, double y) {
        this(x, y, 0);
    }
    
    public Player(double x, double y, int speedUpgradeLevel) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.speedMultiplier = 1.0 + (speedUpgradeLevel * 0.15);
        this.flickerTimer = 0;
    }
    
    public void update(boolean[] keys, int screenWidth, int screenHeight) {
        update(keys, screenWidth, screenHeight, 1.0);
    }
    
    public void update(boolean[] keys, int screenWidth, int screenHeight, double deltaTime) {
        // Decrement flicker timer (scaled by delta time)
        if (flickerTimer > 0) flickerTimer -= deltaTime;
        
        // Acceleration-based movement
        double ax = 0, ay = 0;
        
        if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) ay -= ACCELERATION;
        if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) ay += ACCELERATION;
        if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) ax -= ACCELERATION;
        if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) ax += ACCELERATION;
        
        // Normalize diagonal acceleration
        if (ax != 0 && ay != 0) {
            ax *= 0.707; // 1/sqrt(2)
            ay *= 0.707;
        }
        
        // Apply acceleration to velocity (scaled by delta time)
        vx += ax * deltaTime;
        vy += ay * deltaTime;
        
        // Apply friction when no input
        double frictionFactor = Math.pow(FRICTION, deltaTime);
        if (ax == 0) vx *= frictionFactor;
        if (ay == 0) vy *= frictionFactor;
        
        // Clamp velocity to max speed
        double maxSpeed = MAX_SPEED * speedMultiplier;
        double speed = Math.sqrt(vx * vx + vy * vy);
        if (speed > maxSpeed) {
            vx = (vx / speed) * maxSpeed;
            vy = (vy / speed) * maxSpeed;
        }
        
        // Apply velocity to position (scaled by delta time)
        x += vx * deltaTime;
        y += vy * deltaTime;
        
        // Keep player on screen (with bounce)
        if (x < SIZE) {
            x = SIZE;
            vx *= -0.3;
        }
        if (x > screenWidth - SIZE) {
            x = screenWidth - SIZE;
            vx *= -0.3;
        }
        if (y < SIZE) {
            y = SIZE;
            vy *= -0.3;
        }
        if (y > screenHeight - SIZE) {
            y = screenHeight - SIZE;
            vy *= -0.3;
        }
    }
    
    public void draw(Graphics2D g) {
        // Apply flicker effect if Lucky Dodge was triggered
        float alpha = 1.0f;
        if (flickerTimer > 0) {
            // Rapid flicker between visible and semi-transparent
            alpha = (flickerTimer % 3 == 0) ? 0.3f : 1.0f;
        }
        
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        // Calculate rotation angle based on velocity (pointing in movement direction)
        double angle = Math.atan2(vy, vx);
        // If stationary, point upward
        if (vx == 0 && vy == 0) {
            angle = -Math.PI / 2;
        }
        
        // Save original transform
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.translate(x, y);
        g2d.rotate(angle);
        
        // Draw guided missile body (elongated)
        // Main body (gray metallic)
        g2d.setColor(new Color(117, 117, 117)); // Palette gray
        g2d.fillRect(-SIZE/2, -SIZE/4, SIZE, SIZE/2);
        
        // Nose cone (pointed front)
        int[] noseX = {SIZE/2, SIZE/2 + 8, SIZE/2};
        int[] noseY = {-SIZE/4, 0, SIZE/4};
        g2d.setColor(new Color(191, 97, 106)); // Palette red
        g2d.fillPolygon(noseX, noseY, 3);
        
        // Fins (stabilizers)
        g2d.setColor(new Color(76, 86, 106)); // Palette dark blue-gray
        int[] finTopX = {-SIZE/2, -SIZE/2, -SIZE/2 + 5};
        int[] finTopY = {-SIZE/4, -SIZE/2, -SIZE/4};
        g2d.fillPolygon(finTopX, finTopY, 3);
        
        int[] finBottomX = {-SIZE/2, -SIZE/2, -SIZE/2 + 5};
        int[] finBottomY = {SIZE/4, SIZE/2, SIZE/4};
        g2d.fillPolygon(finBottomX, finBottomY, 3);
        
        // Engine exhaust (flame effect when moving)
        double speed = Math.sqrt(vx * vx + vy * vy);
        if (speed > 0.5) {
            g2d.setColor(new Color(208, 135, 112, 150)); // Palette orange
            int flameLength = (int)(speed * 2);
            g2d.fillOval(-SIZE/2 - flameLength, -SIZE/6, flameLength, SIZE/3);
            g2d.setColor(new Color(235, 203, 139, 100)); // Palette yellow
            g2d.fillOval(-SIZE/2 - flameLength/2, -SIZE/8, flameLength/2, SIZE/4);
        }
        
        // Detail lines
        g2d.setColor(new Color(59, 66, 82)); // Palette dark gray
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(-SIZE/2, -SIZE/4, SIZE, SIZE/2);
        g2d.drawLine(-SIZE/4, -SIZE/4, -SIZE/4, SIZE/4);
        g2d.drawLine(0, -SIZE/4, 0, SIZE/4);
        
        g2d.dispose();
        
        // Draw hitbox (small red dot at center)
        g.setColor(Color.RED);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.5f));
        g.fillOval((int)x - 2, (int)y - 2, 4, 4);
        
        // Reset alpha
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    public boolean collidesWith(Boss boss) {
        // Check if player touches boss (instant win)
        if (boss == null) return false;
        double dx = x - boss.getX();
        double dy = y - boss.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < SIZE/2 + boss.getSize()/2;
    }
    
    public void triggerFlicker() {
        flickerTimer = FLICKER_DURATION;
    }
    
    public boolean isFlickering() {
        return flickerTimer > 0;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getSize() { return SIZE; }
    public double getVX() { return vx; }
    public double getVY() { return vy; }
}
