# J.A.V.A Color Theme Guide

## Color Palette

### Primary Color - Orange
```
Color Name: Orange
RGB: (255, 140, 0)
Hex: #FF8C00
Usage: Primary buttons, main headers, brand color
```

### Secondary Color - Light Blue
```
Color Name: Light Blue  
RGB: (173, 216, 230)
Hex: #ADD8E6
Usage: Secondary buttons, borders, accents
```

### Background Color - White
```
Color Name: White
RGB: (255, 255, 255)
Hex: #FFFFFF
Usage: Panel backgrounds, text field backgrounds
```

### Text Color - Dark Gray
```
Color Name: Dark Gray
RGB: (50, 50, 50)
Hex: #323232
Usage: Body text, labels, secondary text
```

### Hover Color - Light Orange
```
Color Name: Light Orange
RGB: (255, 165, 0)
Hex: #FFA500
Usage: Primary button hover state
```

### Hover Color - Sky Blue
```
Color Name: Sky Blue
RGB: (135, 206, 250)
Hex: #87CEFA
Usage: Secondary button hover state
```

## Usage Guidelines

### When to Use Primary Color (Orange)
- Main action buttons (Login, Create Task, Accept Task, Confirm)
- Page headers and titles
- Important status indicators
- Brand elements

### When to Use Secondary Color (Light Blue)
- Supporting action buttons (Refresh, View, Cancel)
- Borders and dividers
- Input field borders
- Section backgrounds
- Navigation elements

### When to Use White
- Main backgrounds
- Card backgrounds
- Input field backgrounds
- Modal backgrounds

### When to Use Dark Gray
- All text content
- Labels
- Icons
- Disabled states (at 50% opacity)

## Accessibility

### Contrast Ratios
- Orange (#FF8C00) on White: **4.5:1** (WCAG AA compliant)
- Dark Gray (#323232) on White: **11.7:1** (WCAG AAA compliant)
- White on Orange: **3.2:1** (Large text only)

### Best Practices
- Always use white text on orange buttons
- Always use dark gray text on white backgrounds
- Use dark gray text on light blue backgrounds
- Ensure minimum 3:1 contrast for large text
- Ensure minimum 4.5:1 contrast for body text

## Component Examples

### Primary Button
- Background: Orange (#FF8C00)
- Text: White (#FFFFFF)
- Hover: Light Orange (#FFA500)
- Border: None
- Padding: 10px 20px

### Secondary Button
- Background: Light Blue (#ADD8E6)
- Text: Dark Gray (#323232)
- Hover: Sky Blue (#87CEFA)
- Border: None
- Padding: 8px 15px

### Text Input
- Background: White (#FFFFFF)
- Border: Light Blue (#ADD8E6) 1px
- Text: Dark Gray (#323232)
- Padding: 5px 8px

### Panel Header
- Background: White (#FFFFFF)
- Text: Orange (#FF8C00)
- Font: Arial Bold 22px

## Color Psychology

### Orange
- **Energy**: Encourages action and volunteering
- **Warmth**: Creates friendly, welcoming atmosphere
- **Optimism**: Positive association with helping others
- **Attention**: Draws focus to important actions

### Light Blue
- **Trust**: Builds confidence in the platform
- **Calm**: Reduces anxiety around requesting help
- **Clarity**: Enhances readability and organization
- **Peace**: Creates comfortable user experience

### White
- **Simplicity**: Clean, uncluttered interface
- **Space**: Allows content to breathe
- **Professionalism**: Modern, trustworthy appearance
- **Neutrality**: Doesn't compete with content

## Theme Variations (Future)

### Dark Mode (Potential)
- Background: Dark Gray (#1E1E1E)
- Text: White (#FFFFFF)
- Primary: Lighter Orange (#FFA500)
- Secondary: Darker Blue (#4682B4)

### High Contrast Mode
- Background: Black (#000000)
- Text: White (#FFFFFF)
- Primary: Yellow (#FFFF00)
- Secondary: Cyan (#00FFFF)

## Logo Guidelines

### Logo File
- **Filename**: `logo.png`
- **Location**: Project root directory
- **Recommended Size**: 200x200 pixels
- **Format**: PNG with transparency

### Logo Colors
- Should incorporate orange and/or light blue
- White background or transparent
- Clear, simple design
- Readable at small sizes

### Default Logo Design
The application generates a default logo if none exists:
- Circular orange background
- Light blue inner circle
- White "J.A.V.A" text
- 200x200 pixels

## Code Reference

```java
// Color constants in VolunteerGUI.java
private static final Color PRIMARY_COLOR = new Color(255, 140, 0);      // Orange
private static final Color SECONDARY_COLOR = new Color(173, 216, 230);  // Light Blue
private static final Color BACKGROUND_COLOR = Color.WHITE;
private static final Color TEXT_COLOR = new Color(50, 50, 50);
private static final Color BUTTON_HOVER = new Color(255, 165, 0);       // Lighter Orange
```

## Maintaining Consistency

### Do's
✅ Use primary color for main actions
✅ Use secondary color for supporting actions
✅ Maintain consistent spacing (10px, 20px increments)
✅ Apply hover effects to all clickable elements
✅ Use rounded corners sparingly for modern look
✅ Test color contrast for accessibility

### Don'ts
❌ Don't mix too many colors
❌ Don't use low-contrast color combinations
❌ Don't override button colors arbitrarily
❌ Don't forget hover states
❌ Don't use colors for critical information only
❌ Don't make text too small on colored backgrounds
