# Customization Guide - J.A.V.A Application

## Quick Start Customization

### 1. Replace the Logo
The easiest way to brand the application:

1. **Create your logo**:
   - Size: 200x200 pixels (recommended)
   - Format: PNG with transparent background
   - Design: Simple, clear, readable

2. **Replace the file**:
   - Delete the existing `logo.png` in the project root
   - Save your logo as `logo.png` in the same location
   - Run the application - your logo will appear on the login screen

### 2. Change Color Theme

Open `VolunteerGUI.java` and modify these constants (around line 50):

```java
// Current theme: Orange and Light Blue
private static final Color PRIMARY_COLOR = new Color(255, 140, 0);      // Orange
private static final Color SECONDARY_COLOR = new Color(173, 216, 230);  // Light Blue
private static final Color BACKGROUND_COLOR = Color.WHITE;
private static final Color TEXT_COLOR = new Color(50, 50, 50);
private static final Color BUTTON_HOVER = new Color(255, 165, 0);       // Lighter Orange
```

#### Alternative Color Schemes

**Professional Blue Theme:**
```java
private static final Color PRIMARY_COLOR = new Color(0, 123, 255);      // Blue
private static final Color SECONDARY_COLOR = new Color(108, 117, 125);  // Gray
private static final Color BACKGROUND_COLOR = Color.WHITE;
private static final Color TEXT_COLOR = new Color(33, 37, 41);
private static final Color BUTTON_HOVER = new Color(0, 105, 217);       // Darker Blue
```

**Green Nature Theme:**
```java
private static final Color PRIMARY_COLOR = new Color(40, 167, 69);      // Green
private static final Color SECONDARY_COLOR = new Color(23, 162, 184);   // Teal
private static final Color BACKGROUND_COLOR = Color.WHITE;
private static final Color TEXT_COLOR = new Color(33, 37, 41);
private static final Color BUTTON_HOVER = new Color(34, 142, 59);       // Darker Green
```

**Purple Modern Theme:**
```java
private static final Color PRIMARY_COLOR = new Color(111, 66, 193);     // Purple
private static final Color SECONDARY_COLOR = new Color(232, 62, 140);   // Pink
private static final Color BACKGROUND_COLOR = Color.WHITE;
private static final Color TEXT_COLOR = new Color(33, 37, 41);
private static final Color BUTTON_HOVER = new Color(95, 56, 165);       // Darker Purple
```

**Red Emergency Theme:**
```java
private static final Color PRIMARY_COLOR = new Color(220, 53, 69);      // Red
private static final Color SECONDARY_COLOR = new Color(255, 193, 7);    // Yellow
private static final Color BACKGROUND_COLOR = Color.WHITE;
private static final Color TEXT_COLOR = new Color(33, 37, 41);
private static final Color BUTTON_HOVER = new Color(200, 35, 51);       // Darker Red
```

### 3. Adjust Font Sizes

Search for these font declarations in `VolunteerGUI.java`:

**Login Header:**
```java
// Current: Large, bold 28px
headerLabel.setFont(new Font("Arial", Font.BOLD, 28));

// Make it bigger:
headerLabel.setFont(new Font("Arial", Font.BOLD, 34));

// Make it smaller:
headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
```

**Dashboard Headers:**
```java
// Current: 22px
headerLabel.setFont(new Font("Arial", Font.BOLD, 22));

// Change to your preference:
headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
```

**Button Text:**
```java
// In createStyledButton() method
button.setFont(new Font("Arial", Font.BOLD, 13));

// Make buttons larger:
button.setFont(new Font("Arial", Font.BOLD, 15));
```

### 4. Change Window Size

In the constructor:
```java
// Current size: 900x650
setSize(900, 650);

// Make it bigger:
setSize(1024, 768);

// Make it smaller:
setSize(800, 600);

// Make it fullscreen:
setExtendedState(JFrame.MAXIMIZED_BOTH);
```

### 5. Customize Button Padding

In `createStyledButton()` method:
```java
// Current padding: 10px top/bottom, 20px left/right
button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

// Make buttons taller:
button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

// Make buttons more compact:
button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
```

### 6. Modify Border Thickness

For text fields:
```java
// Current: 1px border
BorderFactory.createLineBorder(SECONDARY_COLOR, 1)

// Thicker border:
BorderFactory.createLineBorder(SECONDARY_COLOR, 2)

// Very thick border:
BorderFactory.createLineBorder(SECONDARY_COLOR, 3)
```

For task display areas:
```java
// Current: 2px border
BorderFactory.createLineBorder(SECONDARY_COLOR, 2)

// Change to your preference:
BorderFactory.createLineBorder(SECONDARY_COLOR, 3)
```

### 7. Change Font Family

Replace all `"Arial"` with your preferred font:

```java
// Current:
new Font("Arial", Font.BOLD, 22)

// Try these alternatives:
new Font("Segoe UI", Font.BOLD, 22)      // Modern Windows font
new Font("Helvetica", Font.BOLD, 22)     // Clean, classic
new Font("Verdana", Font.BOLD, 22)       // Highly readable
new Font("Tahoma", Font.BOLD, 22)        // Compact, clear
new Font("Georgia", Font.BOLD, 22)       // Elegant serif
```

## Advanced Customization

### Add Rounded Button Corners

Modify `createStyledButton()` method:

```java
private JButton createStyledButton(String text) {
    JButton button = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2d.setColor(BUTTON_HOVER);
            } else {
                g2d.setColor(getBackground());
            }
            
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2d.dispose();
            
            super.paintComponent(g);
        }
    };
    
    button.setBackground(PRIMARY_COLOR);
    button.setForeground(Color.WHITE);
    button.setFont(new Font("Arial", Font.BOLD, 13));
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    button.setContentAreaFilled(false);
    button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    
    return button;
}
```

### Add Application Icon

Add this to the constructor:
```java
try {
    BufferedImage icon = ImageIO.read(new File("logo.png"));
    setIconImage(icon);
} catch (IOException e) {
    System.err.println("Could not load icon: " + e.getMessage());
}
```

### Change Background Pattern

For a subtle pattern background:
```java
// Create a custom panel with pattern
class PatternPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(240, 240, 240));
        
        // Draw dots pattern
        for (int x = 0; x < getWidth(); x += 20) {
            for (int y = 0; y < getHeight(); y += 20) {
                g2d.fillOval(x, y, 2, 2);
            }
        }
    }
}
```

## Testing Your Changes

After making changes:

1. **Compile**:
   ```bash
   .\compile.bat
   ```

2. **Run**:
   ```bash
   .\run.bat
   ```

3. **Check for errors**: Look at the terminal output

4. **Test all screens**: Login, Elderly dashboard, Volunteer dashboard, Admin dashboard

5. **Verify colors**: Make sure text is readable on all backgrounds

## Tips for Good Design

### Do's ✅
- Keep contrast ratios above 4.5:1 for text
- Test with different screen sizes
- Use consistent spacing throughout
- Limit to 2-3 main colors
- Make buttons obviously clickable
- Use white space effectively

### Don'ts ❌
- Don't use too many colors
- Don't make text too small (minimum 12px)
- Don't use bright colors on bright backgrounds
- Don't forget hover states
- Don't make buttons too small
- Don't use low-contrast combinations

## Troubleshooting

### Colors don't change after recompile
- Make sure you saved the file
- Delete `.class` files and recompile
- Check for syntax errors in the color definitions

### Logo doesn't appear
- Verify `logo.png` exists in the project root
- Check file permissions
- Ensure it's a valid PNG file
- Try a smaller image size

### Buttons look wrong
- Verify all color constants are defined
- Check for missing parentheses or semicolons
- Make sure RGB values are between 0-255

### Window size issues
- Try different sizes for your screen resolution
- Consider using `setMinimumSize()` to prevent too-small windows
- Test on different screen sizes

## Need Help?

If you encounter issues with customization:

1. Check the console/terminal for error messages
2. Verify all RGB color values are valid (0-255)
3. Make sure file paths are correct
4. Test one change at a time
5. Keep a backup of the working version

## Example: Complete Custom Theme

Here's a complete example of a custom "Healthcare" theme:

```java
// Add to VolunteerGUI.java color constants section
private static final Color PRIMARY_COLOR = new Color(0, 150, 136);      // Teal
private static final Color SECONDARY_COLOR = new Color(255, 235, 59);   // Yellow
private static final Color BACKGROUND_COLOR = new Color(250, 250, 250); // Off-white
private static final Color TEXT_COLOR = new Color(33, 33, 33);          // Almost black
private static final Color BUTTON_HOVER = new Color(0, 121, 107);       // Darker teal
```

Save, compile, and run to see your new healthcare-themed interface!
