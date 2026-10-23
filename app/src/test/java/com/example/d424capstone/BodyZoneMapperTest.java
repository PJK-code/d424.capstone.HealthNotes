package com.example.d424capstone;

import com.example.d424capstone.UI.BodyZoneMapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BodyZoneMapperTest {

    @Test
    public void testFrontBodyZones() {
        assertEquals("Head (Front)", BodyZoneMapper.getFrontBodyZone(500, 100));
        assertEquals("Neck (Front)", BodyZoneMapper.getFrontBodyZone(500, 250));
        assertEquals("Chest", BodyZoneMapper.getFrontBodyZone(500, 400));
        assertEquals("Abdomen", BodyZoneMapper.getFrontBodyZone(500, 650));
        assertEquals("Upper Right Arm (Front)", BodyZoneMapper.getFrontBodyZone(200, 400));
        assertEquals("Lower Right Arm (Front)", BodyZoneMapper.getFrontBodyZone(200, 700));
        assertEquals("Upper Left Arm (Front)", BodyZoneMapper.getFrontBodyZone(800, 400));
        assertEquals("Lower Left Arm (Front)", BodyZoneMapper.getFrontBodyZone(800, 700));
        assertEquals("Upper Right Leg (Front)", BodyZoneMapper.getFrontBodyZone(400, 900));
        assertEquals("Lower Right Leg (Front)", BodyZoneMapper.getFrontBodyZone(400, 1200));
        assertEquals("Upper Left Leg (Front)", BodyZoneMapper.getFrontBodyZone(600, 900));
        assertEquals("Lower Left Leg (Front)", BodyZoneMapper.getFrontBodyZone(600, 1200));
        assertNull(BodyZoneMapper.getFrontBodyZone(0, 0));
    }

    @Test
    public void testBackBodyZones() {
        assertEquals("Head (Back)", BodyZoneMapper.getBackBodyZone(500, 100));
        assertEquals("Upper Back", BodyZoneMapper.getBackBodyZone(500, 350));
        assertEquals("Lower Back", BodyZoneMapper.getBackBodyZone(500, 600));
        assertEquals("Left Buttock", BodyZoneMapper.getBackBodyZone(400, 750));
        assertEquals("Right Buttock", BodyZoneMapper.getBackBodyZone(600, 750));
        assertEquals("Upper Left Arm (Back)", BodyZoneMapper.getBackBodyZone(200, 400));
        assertEquals("Lower Left Arm (Back)", BodyZoneMapper.getBackBodyZone(200, 700));
        assertEquals("Upper Right Arm (Back)", BodyZoneMapper.getBackBodyZone(800, 400));
        assertEquals("Lower Right Arm (Back)", BodyZoneMapper.getBackBodyZone(800, 700));
        assertEquals("Upper Left Leg (Back)", BodyZoneMapper.getBackBodyZone(400, 950));
        assertEquals("Lower Left Leg (Back)", BodyZoneMapper.getBackBodyZone(400, 1200));
        assertEquals("Upper Right Leg (Back)", BodyZoneMapper.getBackBodyZone(600, 950));
        assertEquals("Lower Right Leg (Back)", BodyZoneMapper.getBackBodyZone(600, 1200));
        assertNull(BodyZoneMapper.getBackBodyZone(0, 0));
    }
}
