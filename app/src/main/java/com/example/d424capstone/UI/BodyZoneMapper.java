package com.example.d424capstone.UI;


// maps coordinates to the svg body maps to allow it to be tappable
public class BodyZoneMapper {

    public static String getFrontBodyZone(float x, float y) {
        if (x < 60 || x > 960 || y < 10 || y > 1530) {
            return null;
        }

        // head (front)
        if (y < 220 && x >= 360 && x <= 660) {
            return "Head (Front)";
        }
        // neck (front)
        if (y >= 220 && y < 280 && x >= 360 && x <= 660) {
            return "Neck (Front)";
        }
        // arms, chest, abdomen (front)
        if (y >= 240 && y < 920) {
            if (x < 370) {
                return (y < 530) ? "Upper Right Arm (Front)" : "Lower Right Arm (Front)";
            }
            if (x > 650) {
                return (y < 530) ? "Upper Left Arm (Front)" : "Lower Left Arm (Front)";
            }
            if (y < 520) {
                return "Chest";
            }
            if (y < 730) {
                return "Abdomen";
            }
        }
        // legs (front)
        if (y >= 730) {
            if (x <= 512) {
                return (y < 1090) ? "Upper Right Leg (Front)" : "Lower Right Leg (Front)";
            } else {
                return (y < 1090) ? "Upper Left Leg (Front)" : "Lower Left Leg (Front)";
            }
        }

        return null;
    }

    public static String getBackBodyZone(float x, float y) {
        if (x < 60 || x > 960 || y < 10 || y > 1530) {
            return null;
        }

        // head (back)
        if (y < 210 && x >= 360 && x <= 660) {
            return "Head (Back)";
        }
        // arms, upper back, lower back, buttocks (back)
        if (y >= 240 && y < 920) {
            if (x < 370) {
                return (y < 530) ? "Upper Left Arm (Back)" : "Lower Left Arm (Back)";
            }
            if (x > 650) {
                return (y < 530) ? "Upper Right Arm (Back)" : "Lower Right Arm (Back)";
            }
            if (y < 520) {
                return "Upper Back";
            }
            if (y < 710) {
                return "Lower Back";
            }
            if (y < 840) {
                return (x <= 512) ? "Left Buttock" : "Right Buttock";
            }
        }
        // legs (back)
        if (y >= 840) {
            if (x <= 512) {
                return (y < 1090) ? "Upper Left Leg (Back)" : "Lower Left Leg (Back)";
            } else {
                return (y < 1090) ? "Upper Right Leg (Back)" : "Lower Right Leg (Back)";
            }
        }

        return null;
    }
}
