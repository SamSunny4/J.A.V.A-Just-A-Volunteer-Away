# Quick Reference - UI Customization

## 🎨 Change Colors (Fast)

Open `VolunteerGUI.java`, find line ~50, and change these:

```java
// CURRENT THEME: Orange & Light Blue
private static final Color PRIMARY_COLOR = new Color(255, 140, 0);      
private static final Color SECONDARY_COLOR = new Color(173, 216, 230);  

// TRY THESE:

// Blue Theme
private static final Color PRIMARY_COLOR = new Color(0, 123, 255);      
private static final Color SECONDARY_COLOR = new Color(108, 117, 125);  

// Green Theme
private static final Color PRIMARY_COLOR = new Color(40, 167, 69);      
private static final Color SECONDARY_COLOR = new Color(23, 162, 184);   

// Purple Theme
private static final Color PRIMARY_COLOR = new Color(111, 66, 193);     
private static final Color SECONDARY_COLOR = new Color(232, 62, 140);   

// Red Theme
private static final Color PRIMARY_COLOR = new Color(220, 53, 69);      
private static final Color SECONDARY_COLOR = new Color(255, 193, 7);
```

After changing, run:
```bash
.\compile.bat
.\run.bat
```

## 🖼️ Replace Logo (Fast)

1. Create/get your logo image (200x200 pixels recommended)
2. Save as PNG format
3. Replace the existing `logo.png` file in project root
4. Run `.\run.bat` - your logo appears!

## 📏 Resize Window (Fast)

Open `VolunteerGUI.java`, find line ~63:

```java
// Current: 900x650
setSize(900, 650);

// Try these:
setSize(1024, 768);    // Larger
setSize(800, 600);     // Smaller
setSize(1200, 800);    // Extra large

// Or fullscreen:
setExtendedState(JFrame.MAXIMIZED_BOTH);
```

## 🔤 Change Fonts (Fast)

Search `VolunteerGUI.java` for `"Arial"` and replace with:
- `"Segoe UI"` - Modern Windows look
- `"Verdana"` - Highly readable
- `"Tahoma"` - Compact and clear

## 📚 Documentation Files

| File | What's Inside |
|------|---------------|
| **UI-SUMMARY.md** | Overview of all improvements |
| **UI-IMPROVEMENTS.md** | Detailed technical changes |
| **COLOR-THEME-GUIDE.md** | Color usage guidelines |
| **CUSTOMIZATION-GUIDE.md** | Complete customization instructions |

## ⚡ Quick Commands

```bash
# Compile after changes
.\compile.bat

# Run application
.\run.bat

# Both at once
.\compile.bat ; .\run.bat
```

## 🎯 Most Common Customizations

1. **Logo** → Replace `logo.png` ✅
2. **Colors** → Edit `PRIMARY_COLOR` and `SECONDARY_COLOR` ✅
3. **Window Size** → Change `setSize(900, 650)` ✅
4. **Font Size** → Search for `Font.BOLD, 28` and adjust numbers ✅

## 🔍 Where to Find Things

**Colors defined:** Line ~50 in `VolunteerGUI.java`
**Window size:** Line ~63 in `VolunteerGUI.java`
**Logo file:** `logo.png` in project root
**Button styles:** Lines ~90-170 in `VolunteerGUI.java`

## ✅ Quick Test

After making changes:
1. Save file
2. Run `.\compile.bat`
3. Run `.\run.bat`
4. Check login screen first
5. Test all dashboards

## 🆘 Quick Fixes

**Colors not changing?**
- Save the file
- Delete `.class` files
- Recompile

**Logo not showing?**
- Check `logo.png` exists
- Verify it's in project root
- Try a smaller image

**Window too small/large?**
- Adjust `setSize(width, height)`
- Try different values
- Test on your screen

## 💡 Pro Tips

✅ Make one change at a time
✅ Test after each change
✅ Keep backup of working version
✅ Use consistent RGB values (0-255)
✅ Test color contrast for readability

## 🎨 Color Picker

Use these RGB values:

| Color | RGB | Use For |
|-------|-----|---------|
| Orange | 255, 140, 0 | Primary actions |
| Light Blue | 173, 216, 230 | Secondary actions |
| Blue | 0, 123, 255 | Professional |
| Green | 40, 167, 69 | Success/Nature |
| Red | 220, 53, 69 | Important/Alert |
| Purple | 111, 66, 193 | Modern/Creative |
| Yellow | 255, 193, 7 | Warning/Bright |
| Teal | 0, 150, 136 | Calming/Healthcare |

## 📝 Example: Complete Change

Want blue theme with custom logo?

```bash
# 1. Copy your logo
copy C:\path\to\mylogo.png logo.png

# 2. Open VolunteerGUI.java
# 3. Change lines ~50-51:
#    PRIMARY_COLOR = new Color(0, 123, 255)
#    SECONDARY_COLOR = new Color(108, 117, 125)

# 4. Save and run
.\compile.bat
.\run.bat
```

Done! Your app now has a blue theme and custom logo.

---

For more details, see **CUSTOMIZATION-GUIDE.md**
